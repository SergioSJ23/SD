package monitoring;

public class IVotingBooth {

    private static IVotingBooth instance;
    private final MVotesBooth votesMonitor;

    private IVotingBooth() {
        this.votesMonitor = new MVotesBooth();
    }

    public void vote(int id, char vote) {
        System.out.println("Voter " + id + " voted for " + vote);
        votesMonitor.vote(vote);
    }

    public int[] getVotes() {
        return votesMonitor.getVotes();
    }

    public static IVotingBooth getInstance() {
        if (instance == null) {
            instance = new IVotingBooth();
        }
        return instance;
    }
}