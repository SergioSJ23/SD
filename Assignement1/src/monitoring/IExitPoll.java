package monitoring;

public class IExitPoll {

    private static IExitPoll instance;
    private final MVotes votesMonitor;

    private IExitPoll() {
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

    public static IExitPoll getInstance() {
        if (instance == null) {
            instance = new IExitPoll();
        }
        return instance;
    }
}