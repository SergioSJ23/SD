package votesbooth;

// Interface que define o contrato para um eleitor (voter) poder votar
public interface IVotesBooth_Voter {

    /**
     * Método para registar um voto.
     * 
     * @param vote    O voto do eleitor, representado por um carácter ('A', 'B', etc.).
     * @param voterId O identificador único do eleitor.
     */
    void vote(char vote, int voterId);
}