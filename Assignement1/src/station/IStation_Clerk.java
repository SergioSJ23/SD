package station;

public interface IStation_Clerk {
    
    void close(); //Clerk
    int getStatus(); //Clerk
    boolean validateAndAdd(int id);
}
