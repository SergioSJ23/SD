package voter;

public class Station {

    private int status;
    private final int capacity;
    private final int numVoters;
    private int numVotes = 0;

    public Station(int status, int capacity, int numVoters){
        this.status = status;
        this.capacity = capacity;
        this.numVoters = numVoters;
    }

    public int close(){
        this.numVotes += 1;
        if(this.numVotes >= this.numVoters){
            this.status = 1;
        }
        else{
            this.status = 0;
        }
        return this.status;   
    }

    public boolean checkCapacity(int numPersons){
        return numPersons < this.capacity;
    }
    
}
