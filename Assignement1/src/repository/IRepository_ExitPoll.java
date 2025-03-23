package repository;

public interface IRepository_ExitPoll {

    void EPincrementA();
    void EPincrementB();
    void EPclose();
    void EPenter(char vote, int id);
    void EPleave(int id);
    void EPapproached(int id);
    void EPlied(int id, char vote);
    void EPtruth(int id, char vote);
    

}
