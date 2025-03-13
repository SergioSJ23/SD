package voter;


public class ElectionSimulation {
    public static void main(String args[]) {
        // Verifica se foram passados pelo menos 2 argumentos
        if (args.length < 2) {
            System.out.println("Erro: Você deve fornecer dois argumentos.");
            System.out.println("Uso: java ElectionSimulation <num_voters> <num_dem_votes>");
            return;
        }

        try {
            Integer voters = Integer.valueOf(args[0]);
            Integer demVotes = Integer.valueOf(args[1]);

            System.out.println("Number of voters: " + voters);
            System.out.println("Number of Democratic votes: " + demVotes);
        } catch (NumberFormatException e) {
            System.out.println("Erro: Os argumentos devem ser números inteiros.");
        }



    }
}
