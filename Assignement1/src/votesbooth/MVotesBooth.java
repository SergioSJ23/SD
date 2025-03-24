package votesbooth;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import repository.IRepository_VotesBooth;
import repository.MRepository;

// Classe principal que implementa a interface IVotesBooth_all
public class MVotesBooth implements IVotesBooth_all {

    // Variáveis estáticas para armazenar o número total de votos e os votos para as opções A e B
    private static int numVotes = 0;
    private static int votesA = 0;
    private static int votesB = 0;

    // Instância única da classe (Singleton)
    private static IVotesBooth_all instance;

    // Lock para garantir sincronização em ambiente multi-thread
    private final ReentrantLock lock = new ReentrantLock();

    // Repositório para persistência dos dados
    private final IRepository_VotesBooth repository = MRepository.getInstance();

    // Gerador de números aleatórios para simular atrasos
    private final Random rand = new Random();

    // Método estático para obter a instância única da classe (Singleton)
    public static IVotesBooth_all getInstance() {
        if (instance == null) {
            instance = new MVotesBooth(); // Cria a instância se não existir
        }
        return instance; // Retorna a instância existente
    }

    // Método para registar um voto
    @Override
    public void vote(char vote, int voterId) {
        lock.lock(); // Bloqueia o acesso para garantir exclusão mútua
        try {
            // Simula um atraso aleatório entre 0 e 15 milissegundos
            Thread.sleep(rand.nextInt(16));

            // Verifica o voto e incrementa a contagem correspondente
            if (vote == 'A') {
                incrementA(); // Incrementa votos para A
            } else {
                incrementB(); // Incrementa votos para B
            }

            // Regista o voto no repositório
            repository.VBvote(vote, voterId);
        } catch (InterruptedException e) {
            // Trata a interrupção da thread
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock(); // Liberta o lock, garantindo que outros threads possam aceder
        }
    }

    // Método privado para incrementar votos para a opção A
    private void incrementA() {
        votesA++; // Incrementa votos para A
        numVotes++; // Incrementa o total de votos
        System.out.println("Votos: " + numVotes); // Exibe o total de votos no console
        repository.VBincrementA(); // Atualiza o repositório
    }

    // Método privado para incrementar votos para a opção B
    private void incrementB() {
        votesB++; // Incrementa votos para B
        numVotes++; // Incrementa o total de votos
        System.out.println("Votos: " + numVotes); // Exibe o total de votos no console
        repository.VBincrementB(); // Atualiza o repositório
    }

    // Método para obter a contagem de votos
    @Override
    public int[] getVotes() {
        lock.lock(); // Bloqueia o acesso para garantir exclusão mútua
        try {
            // Atualiza o repositório com os votos atuais
            repository.VBgetVotes(votesA, votesB);
            // Retorna um array com os votos de A e B
            return new int[]{votesA, votesB};
        } finally {
            lock.unlock(); // Liberta o lock
        }
    }
}