package votersId;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


public class MVotersId implements IVotersId_all {

    private static MVotersId instance;
    private int id;
    private final int repeatVoter = 10;
    private final static ArrayList<Integer> idList = new ArrayList<>();
    
    private MVotersId() {
        
    }

    public static MVotersId getInstance() {

        if (instance == null) {
            instance = new MVotersId();
        }
        return instance;
        
    }

    @Override
    public int reborn() {
    if (ThreadLocalRandom.current().nextInt(0, 100) >= this.repeatVoter) {
        // Gera um novo ID aleatório e adiciona à lista
        int newId;
        synchronized (idList) {
            do {
                newId = ThreadLocalRandom.current().nextInt(0, 1000); // IDs de 4 dígitos
            } while (idList.contains(newId)); // Garante que o ID seja único
            idList.add(newId);
            return newId;
        }
    } else {
        // Reutiliza um ID existente aleatório da lista
        synchronized (idList) {
            if (!idList.isEmpty()) {
                int randomIndex = ThreadLocalRandom.current().nextInt(idList.size());
                this.id = idList.get(randomIndex);
                return this.id;
            } else {
                // Caso a lista esteja vazia, gera um novo ID
                int newId = ThreadLocalRandom.current().nextInt(1000, 10000);
                idList.add(newId);
                return newId;

            }
        }
    }
}
    
}
