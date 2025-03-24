package station;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_Station;
import repository.MRepository;

// Classe que implementa a interface IStation_all para gerir a estação de votação
public class MStation implements IStation_all {

    // Flag para indicar se a estação está fechada
    private boolean closen = true;

    // Flag para indicar se o dia de eleições terminou
    private static boolean electionDayEnded = false;

    // Limite de votos permitidos na estação
    private int limitVotes = 50;

    // Lock para sincronização em ambiente multi-thread
    private final ReentrantLock lock = new ReentrantLock();

    // Conjunto para armazenar IDs de eleitores que já votaram
    private final HashSet<Integer> idSet = new HashSet<>();

    // Instância única da classe (Singleton)
    private static MStation instance;

    // Condições para sincronização entre threads
    private final Condition voterCondition = lock.newCondition(); // Para eleitores
    private final Condition clerkCondition = lock.newCondition(); // Para funcionários
    private final Condition statusCondition = lock.newCondition(); // Para estado da estação

    // Repositório para persistência dos dados
    private final IRepository_Station repository = MRepository.getInstance();

    // Fila de eleitores esperando para votar
    private final BlockingQueue<Integer> queue;

    // Gerador de números aleatórios para simular atrasos
    private final Random rand = new Random();

    // Flag para notificar o eleitor se o ID foi validado
    private boolean isIdValid = false;

    // Flag para evitar deadlock, indicando se o funcionário está pronto
    private boolean clerkReady = false;

    // Construtor privado para evitar instanciação direta (Singleton)
    private MStation(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity); // Inicializa a fila com capacidade definida
    }

    // Método estático para obter a instância única da classe (Singleton)
    public static IStation_all getInstance(int capacity) {
        if (instance == null) {
            instance = new MStation(capacity); // Cria a instância se não existir
        }
        return instance; // Retorna a instância existente
    }

    // Método para abrir a estação de votação
    @Override
    public void openStation() {
        lock.lock();
        try {
            closen = false; // Marca a estação como aberta
            repository.Sopen(); // Atualiza o repositório
            statusCondition.signalAll(); // Notifica todas as threads em espera
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método para um eleitor entrar na estação de votação
    @Override
    public void enterStation(int id) throws InterruptedException {
        lock.lock();
        try {
            // Espera enquanto a estação estiver fechada e o dia de eleições não tiver terminado
            while (closen && !electionDayEnded) {
                try {
                    statusCondition.await(); // Coloca a thread em espera
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restaura o estado de interrupção
                    return;
                }
            }
        } finally {
            lock.unlock(); // Liberta o lock
        }

        // Se o dia de eleições terminou, interrompe a thread
        if (electionDayEnded) {
            Thread.currentThread().interrupt();
            return;
        }

        try {
            queue.put(id); // Adiciona o ID do eleitor à fila
            if (electionDayEnded) {
                queue.remove(id); // Remove o ID se o dia de eleições terminou
                Thread.currentThread().interrupt();
                return;
            }
            repository.Senter(id); // Atualiza o repositório
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura o estado de interrupção
            throw e; // Re-lança a exceção
        }
    }

    // Método para verificar se o eleitor está presente na estação
    @Override
    public boolean present(int id) {
        // Espera até que o eleitor seja o próximo na fila
        while (id != queue.peek()) {}

        lock.lock();
        try {
            repository.Spresent(id); // Atualiza o repositório

            clerkReady = true; // Marca o funcionário como pronto
            clerkCondition.signalAll(); // Notifica o funcionário
            voterCondition.await(); // Coloca o eleitor em espera
            return this.isIdValid; // Retorna se o ID é válido
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura o estado de interrupção
            return false;
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método para validar e adicionar o voto do eleitor
    @Override
    public void validateAndAdd() throws InterruptedException {
        lock.lock();
        try {
            // Espera até que o funcionário esteja pronto
            while (!clerkReady) {
                clerkCondition.await();
            }

            int id = queue.peek(); // Obtém o ID do próximo eleitor na fila
            Thread.sleep(rand.nextInt(6) + 5); // Simula o tempo de validação

            // Verifica se o ID já foi usado
            if (idSet.contains(id)) {
                repository.Srejected(id); // Atualiza o repositório (ID rejeitado)
                this.isIdValid = false; // Marca como inválido
            } else {
                System.out.println("validated " + id); // Log de validação
                idSet.add(id); // Adiciona o ID ao conjunto
                repository.Svalidated(id); // Atualiza o repositório (ID validado)
                this.isIdValid = true; // Marca como válido
                decrementLimit(); // Decrementa o limite de votos
                repository.SaddId(id); // Atualiza o repositório
            }

            clerkReady = false; // Reseta a flag do funcionário
            voterCondition.signalAll(); // Notifica o eleitor para continuar
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método para processar os últimos votos
    @Override
    public boolean lastVotes() {
        lock.lock();
        try {
            // Espera até que o funcionário esteja pronto
            while (!clerkReady) {
                clerkCondition.await();
            }

            int id = queue.peek(); // Obtém o ID do próximo eleitor na fila
            Thread.sleep(rand.nextInt(6) + 5); // Simula o tempo de validação

            // Verifica se o ID já foi usado
            if (idSet.contains(id)) {
                repository.Srejected(id); // Atualiza o repositório (ID rejeitado)
                this.isIdValid = false; // Marca como inválido
            } else {
                idSet.add(id); // Adiciona o ID ao conjunto
                repository.Svalidated(id); // Atualiza o repositório (ID validado)
                this.isIdValid = true; // Marca como válido
                decrementLimit(); // Decrementa o limite de votos
                repository.SaddId(id); // Atualiza o repositório
            }

            // Verifica se a fila está vazia
            if (queue.size() <= 0) {
                return false; // Não há mais votos para processar
            }

            clerkReady = false; // Reseta a flag do funcionário
            voterCondition.signalAll(); // Notifica o eleitor para continuar
            return true; // Ainda há votos para processar
        } catch (InterruptedException e) {
            // Trata a interrupção da thread
        } finally {
            lock.unlock(); // Liberta o lock
        }
        return false; // Retorna falso em caso de erro
    }

    // Método para o eleitor sair da estação
    @Override
    public void leaveStation(int id) {
        try {
            queue.remove(id); // Remove o ID da fila
            repository.Sleave(id); // Atualiza o repositório
        } catch (Exception e) {
            Thread.currentThread().interrupt(); // Restaura o estado de interrupção
        }
    }

    // Método para fechar a estação
    @Override
    public void close() {
        lock.lock();
        try {
            closen = true; // Marca a estação como fechada
            repository.Sclose(); // Atualiza o repositório
            voterCondition.signalAll(); // Notifica todos os eleitores
            clerkCondition.signalAll(); // Notifica o funcionário
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método para verificar se o limite de votos foi atingido
    @Override
    public boolean countVotes() {
        return limitVotes <= 0; // Retorna verdadeiro se o limite de votos foi atingido
    }

    // Método para anunciar o fim do dia de eleições
    @Override
    public void announceEnding() {
        lock.lock();
        try {
            electionDayEnded = true; // Marca o fim do dia de eleições
            repository.SannounceEnding(); // Atualiza o repositório
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método privado para decrementar o limite de votos
    private void decrementLimit() {
        lock.lock();
        try {
            limitVotes--; // Decrementa o limite de votos
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }
}