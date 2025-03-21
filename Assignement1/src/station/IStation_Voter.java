package station;

public interface IStation_Voter {
    
    void enterStation(int id); //Voter
    void leaveStation(int id); //Voter
    boolean validate(int id); //Voter
}
