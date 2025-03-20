package votelimit;

public interface IVoteLimit_all  {
    
    boolean validateAndAdd(int id); //Voter & Clerk
    boolean isLimitReached(); //Clerk
    
}
