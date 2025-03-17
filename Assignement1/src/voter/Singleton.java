package voter;

public class Singleton {
    private static Singleton instance;
    public Clerk clerk;
    public ExitPoll exitPoll;
    public Pollster pollster;
    public Station station;
    public VotingBooth votingBooth;

    private Singleton(){
        this.clerk = new Clerk(50);
        this.exitPoll = new ExitPoll();
        this.pollster = new Pollster(50);
        this.station = new Station(0, 0, 0);
        this.votingBooth = new VotingBooth();
    }

    //TODO: Verify in all files, where we can change the constructor to private
    public static Singleton getInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }
}
