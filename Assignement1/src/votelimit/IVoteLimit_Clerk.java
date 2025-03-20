package votelimit;

public interface IVoteLimit_Clerk {
    
    boolean validateAndAdd(int id); //Voter & Clerk
    boolean isLimitReached(); //Clerk
    
}