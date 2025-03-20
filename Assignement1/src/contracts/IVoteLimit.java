package contracts;

public interface IVoteLimit {
    
    boolean validateAndAdd(int id);
    boolean isLimitReached();
    
}
