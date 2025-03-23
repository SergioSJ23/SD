package repository;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MRepository implements IRepository_all {

    private static MRepository instance;
    private final ReentrantLock lock = new ReentrantLock();
    private final Random rand = new Random();

    // Atributos do VotesBooth
    private int numVotes = 0;
    private int votesA = 0;
    private int votesB = 0;

    // Atributos do ExitPoll
    private int exitVotesA = 0;
    private int exitVotesB = 0;
    private char exitVote;
    // Configurações de simulação
    private final int lie = 20;         // 20% chance de mentir
    private final int noResponse = 60;  // 60% chance de não responder
    private final int approached = 10;   // 10% de chance de ser abordado

    // Variáveis de controle do ExitPoll
    private boolean pollsterReady = false;
    private boolean isClosed = false;
    private final Condition pollsterCondition = lock.newCondition();
    private final Condition voterCondition = lock.newCondition();
    private final Condition willAnswer = lock.newCondition();
    private final Condition hasAnswered = lock.newCondition();

    private MRepository() { }

    public static MRepository getInstance() {
        if (instance == null) {
            instance = new MRepository();
        }
        return instance;
    }

    // ================= Métodos do VotesBooth =================

    private void incrementA() {
        votesA++;
        numVotes++;
    }

    private void incrementB() {
        votesB++;
        numVotes++;
    }

    // ================= Métodos do ExitPoll =================

    /**
     * Incrementa a contagem de votos do exit poll com base no ID do voto:
     * 0 para voto 'A' e 1 para voto 'B'.
     */
    @Override
    public void incrementExit(int voteId) {
        lock.lock();
        try {
            System.out.println("Incrementing exit poll votes");
            if (voteId == 0) {
                exitVotesA++;
            } else {
                exitVotesB++;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int[] getExitVotes() {
        lock.lock();
        try {
            return new int[]{exitVotesA, exitVotesB};
        } finally {
            lock.unlock();
        }
    }

    /**
     * Indica que a estação foi fechada e notifica que o exit poll deve encerrar suas operações.
     */
    @Override
    public void stationIsClosed() {
        lock.lock();
        try {
            isClosed = true;
            System.out.println("Exit poll knows the station is closed");
        } finally {
            lock.unlock();
        }
    }

    /**
     * Método chamado quando o eleitor entra no exit poll.
     * Se a estação estiver fechada, a thread é interrompida.
     */
    @Override
    public void enterExitPoll(char vote) {
        System.out.println("Voter entered exit poll");
        if (isClosed) {
            Thread.currentThread().interrupt();
            return;
        }
        lock.lock();
        try {
            System.out.println("Voter entered exit poll");
            this.exitVote = vote;
            pollsterReady = true;
            pollsterCondition.signalAll();
            try {
                // Aguarda o exit poll processar a resposta
                voterCondition.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Simula a abordagem do exit poll ao eleitor.
     * Dependendo das chances configuradas, incrementa o voto, simula mentira ou resposta verdadeira.
     */
    @Override
    public void inquire() throws InterruptedException {
        lock.lock();
        try {
            while (!pollsterReady) {
                pollsterCondition.await();
            }
            if (rand.nextInt(100) < approached) { // 10% de chance de ser abordado
                System.out.println("Voter was approached by the exit poll with vote " + this.exitVote);
                incrementExit(exitVote == 'A' ? 0 : 1);
                if (rand.nextInt(100) > noResponse) { // 60% de chance de não responder
                    if (rand.nextInt(100) < lie) { // 20% de chance de mentir
                        System.out.println("Voter lied about voting for party " + (exitVote == 'A' ? "B" : "A"));
                    } else {
                        System.out.println("Voter told the truth and voted for party " + exitVote);
                    }
                }
                System.out.println("Voter has answered the exit poll");
            }
            pollsterReady = false;
            voterCondition.signalAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        } finally {
            lock.unlock();
        }
    }
}
