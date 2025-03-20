package monitoring;

public class ExitPoll {

    private static ExitPoll instance;
    private final MVotes votesMonitor;

    private ExitPoll() {
        this.votesMonitor = new MVotes();
    }

    public void increment(int voteId) {
        votesMonitor.increment(voteId);
    }

    public int getVotesA() {
        return votesMonitor.getVotesA();
    }

    public int getVotesB() {
        return votesMonitor.getVotesB();
    }

    // Optional: Return both votes in one method
    public int[] getVotes() {
        return votesMonitor.getVotes();
    }

    public static ExitPoll getInstance() {
        if (instance == null) {
            instance = new ExitPoll();
        }
        return instance;
    }
}