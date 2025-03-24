package threads;

import exitpoll.IExitPoll_Pollster;

// Classe que representa um entrevistador (pollster) como uma thread
public class TPollster extends Thread {

    // Instância única da classe (Singleton)
    private static TPollster instance;

    // Referência para a interface IExitPoll_Pollster, que define as operações do entrevistador
    private final IExitPoll_Pollster exitPoll;

    // Construtor privado para evitar instanciação direta (Singleton)
    private TPollster(IExitPoll_Pollster exitPoll) {
        this.exitPoll = exitPoll;
    }

    // Método principal da thread
    @Override
    public void run() {
        try {
            while (true) {
                // Realiza a operação de inquérito (polling) continuamente
                exitPoll.inquire();
            }
        } catch (InterruptedException e) {
            // Trata a interrupção da thread
            System.out.println("Pollster interrupted");
        }
    }

    // Método estático para obter a instância única da classe (Singleton)
    public static TPollster getInstance(IExitPoll_Pollster exitpoll) {
        if (instance == null) {
            // Cria a instância se não existir
            instance = new TPollster(exitpoll);
        }
        // Retorna a instância existente
        return instance;
    }
}