package voter;

public class Station {

    private static int status;
    private static int capacity;
    private static int numVoters;
    private int numVotes = 0;

    public Station(int status, int capacity, int numVoters){
        this.status = status;
        this.capacity = capacity;
        this.numVoters = numVoters;
    }

    public int close(){
        numVotes += 1;
        if(numVotes >= numVoters){
            status = 1;
        }
        else{
            status = 0;
        }

        return status;   
    }

    public boolean checkCapacity(int numPersons){
        return numPersons < capacity;
    } 
}
