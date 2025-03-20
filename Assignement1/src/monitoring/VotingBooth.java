package monitoring;

public class VotingBooth {

    private static VotingBooth instance;
    private final MVotesBooth votesMonitor;

    private VotingBooth() {
        this.votesMonitor = new MVotesBooth();
    }

    public void vote(int id, char vote) {
        System.out.println("Voter " + id + " voted for " + vote);
        votesMonitor.vote(vote);
    }

    public int[] getVotes() {
        return votesMonitor.getVotes();
    }

    public static VotingBooth getInstance() {
        if (instance == null) {
            instance = new VotingBooth();
        }
        return instance;
    }
}