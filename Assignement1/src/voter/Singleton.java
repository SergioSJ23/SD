package voter;

public class Singleton {
    private static Singleton instance;
    public Clerk clerk;
    public ExitPoll exitPoll;
    public Pollster pollster;
    public Station station;
    public VotingBooth votingBooth;

    private Singleton(){
        this.clerk = Clerk.getInstance(50);
        this.exitPoll = ExitPoll.getInstance();
        this.pollster = Pollster.getInstance(50, 50, 50);
        this.station = Station.getInstance(0,0,0);
        this.votingBooth = VotingBooth.getInstance();
    }

    public static Singleton getInstance(){
        if (instance == null){
            instance = new Singleton();
        }
        return instance;
    }
}
