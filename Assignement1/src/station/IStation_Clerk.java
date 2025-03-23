package station;

public interface IStation_Clerk {
    
    void close(); //Clerk
    void validateAndAdd() throws InterruptedException; //Clerk
    boolean lastVotes();
    boolean countVotes();
    void openStation();
    void announceEnding();
}
