package votersId;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

// Classe que implementa a interface IVotersId_all para gerir IDs de eleitores
public class MVotersId implements IVotersId_all {

    // Instância única da classe (Singleton)
    private static MVotersId instance;

    // Variável para armazenar o ID atual
    private int id;

    // Probabilidade de reutilizar um ID existente (30%)
    private final int repeatVoter = 30;

    // Lista estática para armazenar todos os IDs gerados
    private final static ArrayList<Integer> idList = new ArrayList<>();

    // Construtor privado para evitar instanciação direta (Singleton)
    private MVotersId() {
    }

    // Método estático para obter a instância única da classe (Singleton)
    public static MVotersId getInstance() {
        if (instance == null) {
            instance = new MVotersId(); // Cria a instância se não existir
        }
        return instance; // Retorna a instância existente
    }

    // Método para gerar ou reutilizar um ID de eleitor
    @Override
    public int reborn() {
        // Verifica se deve reutilizar um ID existente (com base na probabilidade de 30%)
        if (ThreadLocalRandom.current().nextInt(0, 100) >= this.repeatVoter) {
            // Gera um novo ID aleatório e adiciona-o à lista
            int newId;
            synchronized (idList) { // Bloqueia a lista para evitar concorrência
                do {
                    newId = ThreadLocalRandom.current().nextInt(0, 1000); // Gera um ID de 4 dígitos
                } while (idList.contains(newId)); // Garante que o ID seja único
                idList.add(newId); // Adiciona o novo ID à lista
                return newId; // Retorna o novo ID
            }
        } else {
            // Reutiliza um ID existente aleatório da lista
            synchronized (idList) { // Bloqueia a lista para evitar concorrência
                if (!idList.isEmpty()) {
                    // Escolhe um ID aleatório da lista
                    int randomIndex = ThreadLocalRandom.current().nextInt(idList.size());
                    this.id = idList.get(randomIndex); // Atualiza o ID atual
                    return this.id; // Retorna o ID reutilizado
                } else {
                    // Caso a lista esteja vazia, gera um novo ID
                    int newId = ThreadLocalRandom.current().nextInt(1000, 10000); // Gera um ID de 4 dígitos
                    idList.add(newId); // Adiciona o novo ID à lista
                    return newId; // Retorna o novo ID
                }
            }
        }
    }
}
