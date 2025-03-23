package station;

public interface IStation_Clerk {
    
    void close(); //Clerk
    boolean getStatus(); //Clerk
    void validateAndAdd() throws InterruptedException; //Clerk
    boolean isStationEmpty(); //Clerk
    boolean countVotes();
    void openStation();
    void announceEnding();
}
