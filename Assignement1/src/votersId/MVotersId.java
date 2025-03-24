package votersId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MVotersId implements IVotersId_all {

    // Instância única da classe (Singleton)
    private static MVotersId instance;

    // Variável para armazenar o ID atual
    private final int repeatVoter = 80;
    private final static ArrayList<Integer> idList = new ArrayList<>();
    private final static Set<Integer> currentIds = new HashSet<>(); // Register of current IDs

    private MVotersId() {
        // Private constructor to enforce singleton pattern
    }

    // Método estático para obter a instância única da classe (Singleton)
    public static MVotersId getInstance() {

                if (instance == null) {
                    instance = new MVotersId();
        }
        return instance; // Retorna a instância existente
    }

    // Método para gerar ou reutilizar um ID de eleitor
    @Override
    public int reborn(int id) {
    synchronized (currentIds) {
        // Remove the old ID from the currentIds set if it exists

        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.repeatVoter) {
            // Generate a new unique ID and add it to the list and currentIds set
            int newId;
            synchronized (idList) {
                do {
                    newId = ThreadLocalRandom.current().nextInt(1, 1000); // IDs of 4 digits
                } while (idList.contains(newId) || currentIds.contains(newId)); // Ensure the ID is unique
                idList.add(newId);
            }
            if (currentIds.contains(id)) {
                currentIds.remove(id);
            }
            currentIds.add(newId);
            return newId;
        } else {
            // Reuse an existing ID from the list, but ensure it's not already in use
            synchronized (idList) {
                if (!idList.isEmpty()) {
                    return id;
                }

                // If no reusable ID is available, generate a new ID
                int newId;
                do {
                    newId = ThreadLocalRandom.current().nextInt(1, 1000); // Generate a new 4-digit ID
                } while (idList.contains(newId) || currentIds.contains(newId)); // Ensure uniqueness
                idList.add(newId);
                if (currentIds.contains(id)) {
                    currentIds.remove(id);
                }
                currentIds.add(newId);
                return newId;
            }
        }
    }
}
}
