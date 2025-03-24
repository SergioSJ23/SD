package votersId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MVotersId implements IVotersId_all {

    // Instância Singleton
    private static MVotersId instance;

    // Constantes
    private final int repeatVoter = 30; // Probabilidade de um eleitor reutilizar um ID
    private final static ArrayList<Integer> idList = new ArrayList<>(); // Lista de IDs gerados
    private final static Set<Integer> currentIds = new HashSet<>(); // Conjunto de IDs atuais registados

    // Construtor privado para garantir o padrão Singleton
    private MVotersId() {}

    // Método para obter a instância Singleton
    public static MVotersId getInstance() {
        if (instance == null) {
            instance = new MVotersId();
        }
        return instance;
    }

    // Método para gerar ou reutilizar um ID de eleitor
    @Override
    public int reborn(int id) {
        synchronized (currentIds) { // Sincronização para garantir a consistência dos dados
            if (shouldGenerateNewId() || id == 0) {
                return generateNewId(id); // Gera um novo ID se necessário
            } else {
                return reuseExistingId(id); // Reutiliza o ID existente, se possível
            }
        }
    }

    // Método auxiliar para decidir se deve gerar um novo ID ou reutilizar um existente
    private boolean shouldGenerateNewId() {
        // Gera um número aleatório entre 0 e 99 e verifica se é maior ou igual à probabilidade
        return ThreadLocalRandom.current().nextInt(0, 100) >= this.repeatVoter;
    }

    // Método auxiliar para gerar um novo ID único
    private int generateNewId(int oldId) {
        int newId;
        synchronized (idList) { // Sincronização para acesso seguro à lista de IDs
            newId = generateUniqueId();
            idList.add(newId); // Adiciona o novo ID à lista
        }
        updateCurrentIds(oldId, newId); // Atualiza o conjunto de IDs atuais
        return newId;
    }

    // Método auxiliar para reutilizar um ID existente
    private int reuseExistingId(int oldId) {
        synchronized (idList) { // Sincronização para garantir a consistência da lista
            if (!idList.isEmpty()) {
                return oldId; // Retorna o ID antigo se existir na lista
            }
        }
        return generateNewId(oldId); // Gera um novo ID se não houver IDs reutilizáveis
    }

    // Método auxiliar para gerar um ID único
    private int generateUniqueId() {
        int newId;
        do {
            // Gera um ID aleatório entre 1 e 999 (4 dígitos)
            newId = ThreadLocalRandom.current().nextInt(1, 1000);
        } while (idList.contains(newId) || currentIds.contains(newId)); // Garante que o ID seja único
        return newId;
    }

    // Método auxiliar para atualizar o conjunto de IDs atuais
    private void updateCurrentIds(int oldId, int newId) {
        if (currentIds.contains(oldId)) {
            currentIds.remove(oldId); // Remove o ID antigo, se presente
        }
        currentIds.add(newId); // Adiciona o novo ID ao conjunto
    }
}
