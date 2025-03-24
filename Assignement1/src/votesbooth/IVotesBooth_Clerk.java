package votesbooth;

// Interface que define o contrato para um funcionário (clerk) obter a contagem de votos
public interface IVotesBooth_Clerk {

    /**
     * Método para obter a contagem de votos.
     * 
     * @return Um array de inteiros onde:
     *         - A primeira posição contém o número de votos para a opção A.
     *         - A segunda posição contém o número de votos para a opção B.
     */
    public int[] getVotes();
}
