package station;

public interface IStation_Voter {
    
    void enterStation(int id) throws InterruptedException; //Voter
    void leaveStation(int id); //Voter
    boolean present(int id); //Voter
}
