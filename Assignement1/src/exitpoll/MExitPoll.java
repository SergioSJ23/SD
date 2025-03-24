package exitpoll;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_ExitPoll;
import repository.MRepository;

// Classe que implementa a interface IExitPoll_all para gerir a sondagem de saída
public class MExitPoll implements IExitPoll_all {

    // Contadores de votos para as opções A e B
    private int votesA = 0;
    private int votesB = 0;

    // Variáveis para armazenar o voto e o ID do eleitor atual
    private char vote;
    private int voterId;
    private final int lie = 20;
    private final int noResponse = 40;
    private final int approached = 10;
    private final ReentrantLock lock = new ReentrantLock();

    // Instância única da classe (Singleton)
    private static IExitPoll_all instance;

    // Condições para sincronização entre threads
    private final Condition pollsterCondition = lock.newCondition(); // Para o entrevistador
    private final Condition voterCondition = lock.newCondition(); // Para o eleitor

    // Flag para indicar se o entrevistador está pronto
    private boolean pollsterReady = false;

    // Repositório para persistência dos dados
    private final IRepository_ExitPoll repository = MRepository.getInstance();

    // Gerador de números aleatórios
    private final Random rand = new Random();

    // Flag para indicar se a sondagem de saída está fechada
    private static boolean isClosed = false;

    // Método estático para obter a instância única da classe (Singleton)
    public static IExitPoll_all getInstance() {
        if (instance == null) {
            instance = new MExitPoll(); // Cria a instância se não existir
        }
        return instance; // Retorna a instância existente
    }

    // Método privado para incrementar os votos
    private void increment(int voteId) {
        lock.lock();
        try {
            if (voteId == 0) {
                votesA++; // Incrementa votos para A
                repository.EPincrementA(); // Atualiza o repositório
            } else {
                votesB++; // Incrementa votos para B
                repository.EPincrementB(); // Atualiza o repositório
            }
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método para fechar a sondagem de saída
    @Override
    public void stationIsClosed() {
        lock.lock();
        try {
            isClosed = true; // Marca a sondagem como fechada
            repository.EPclose(); // Atualiza o repositório
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método para um eleitor entrar na sondagem de saída
    @Override
    public void enterExitPoll(char vote, int voterId) {
        if (isClosed) {
            leaveExitPoll(voterId); // Se a sondagem estiver fechada, o eleitor sai
            return;
        }
        lock.lock();
        try {
            this.vote = vote; // Armazena o voto do eleitor
            this.voterId = voterId; // Armazena o ID do eleitor
            pollsterReady = true; // Marca o entrevistador como pronto
            repository.EPenter(vote, voterId); // Atualiza o repositório
            pollsterCondition.signalAll(); // Notifica o entrevistador
            voterCondition.await(); // Coloca o eleitor em espera
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura o estado de interrupção
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }

    // Método para um eleitor sair da sondagem de saída
    @Override
    public void leaveExitPoll(int voterId) {
        if (isClosed) {
            Thread.currentThread().interrupt(); // Interrompe a thread se a sondagem estiver fechada
            return;
        }
        repository.EPleave(voterId); // Atualiza o repositório
    }

    // Método para o entrevistador realizar a sondagem
    @Override
    public void inquire() throws InterruptedException {
        lock.lock();
        try {
            // Espera até que o entrevistador esteja pronto
            while (!pollsterReady) {
                pollsterCondition.await();
            }

            // Verifica se o eleitor foi abordado (100% de chance neste exemplo)
            if (rand.nextInt(100) < approached) {
                repository.EPapproached(voterId); // Atualiza o repositório

                // Incrementa os votos com base no voto do eleitor
                increment(vote == 'A' ? 0 : 1);

                // Verifica se o eleitor respondeu (neste exemplo, sempre responde)
                if (rand.nextInt(100) > noResponse) {
                    // Verifica se o eleitor mentiu (20% de chance)
                    if (rand.nextInt(100) < lie) {
                        repository.EPlied(voterId, vote); // Atualiza o repositório (mentiu)
                    } else {
                        repository.EPtruth(voterId, vote); // Atualiza o repositório (disse a verdade)
                    }
                }
            }

            pollsterReady = false; // Reseta a flag do entrevistador
            voterCondition.signalAll(); // Notifica o eleitor para continuar
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura o estado de interrupção
            throw e; // Re-lança a exceção
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }
}