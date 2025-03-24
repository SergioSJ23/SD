package repository;

// Interface que combina as funcionalidades de IRepository_VotesBooth, IRepository_ExitPoll e IRepository_Station
public interface IRepository_all extends IRepository_VotesBooth, IRepository_ExitPoll, IRepository_Station {
    // Esta interface não define novos métodos, apenas herda os métodos das interfaces que estende.
}