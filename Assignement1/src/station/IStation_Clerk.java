package station;

public interface IStation_Clerk {
    
    void close(); //Clerk
    int getStatus(); //Clerk
    void validateAndAdd() throws InterruptedException; //Clerk
    boolean isStationEmpty(); //Clerk
}
