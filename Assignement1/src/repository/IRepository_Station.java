package repository;

public interface IRepository_Station {

    void Sopen();
    void SaddId(int id);
    void Sclose();
    void SannounceEnding();
    void Senter(int id);
    void Sleave(int id);
    void Swait(int id);
    void Spresent(int id);
    void Srejected(int id);
    void Svalidated(int id);
    
}
