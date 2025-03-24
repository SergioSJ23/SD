package main;

import exitpoll.*;
import gui.*;
import java.util.List;
import java.util.Scanner;
import repository.MRepository;
import station.*;
import threads.*;
import votersId.*;
import votesbooth.*;

// Classe principal que inicia e gere o sistema de votação
public class Main {
    // Variáveis estáticas para armazenar o número de eleitores e a capacidade máxima da estação
    private static int numVoters = 0; // Número de eleitores
    private static int capacityCap = 0; // Capacidade máxima da estação de votação
    private final static List<TVoter> voters = new java.util.ArrayList<>(); // Lista de threads dos eleitores

    // Método para obter o número de eleitores
    public static int getNumVoters() {
        return numVoters;
    }

    // Método para obter a capacidade máxima da estação
    public static int getCapacityCap() {
        return capacityCap;
    }

    // Método para obter a lista de threads dos eleitores
    public static List<TVoter> getVoters() {
        return voters;
    }

    // Método principal
    public static void main(String args[]) {
        // Scanner para ler a entrada do utilizador
        try (Scanner sc = new Scanner(System.in)) {
            String input;

            // Solicita o número de eleitores (entre 3 e 10)
            do {
                try {
                    System.out.println("Introduza o número de eleitores (mín 3, máx 10): ");
                    input = sc.nextLine();
                    numVoters = Integer.parseInt(input); // Converte a entrada para inteiro
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida"); // Trata entradas inválidas
                }
            } while (numVoters < 3 || numVoters > 10); // Repete até que a entrada seja válida

            // Solicita a capacidade máxima da estação (entre 2 e 5)
            do {
                try {
                    System.out.println("Introduza a capacidade máxima da estação (mín 2, máx 5): ");
                    input = sc.nextLine();
                    capacityCap = Integer.parseInt(input); // Converte a entrada para inteiro
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida"); // Trata entradas inválidas
                }
            } while (capacityCap < 2 || capacityCap > 5); // Repete até que a entrada seja válida
        }

        // Instâncias dos monitores (estação, sondagem de saída, urna de votação e gerador de IDs)
        IStation_all station;
        IExitPoll_all exitPoll;
        IVotesBooth_all votesBooth;
        IVotersId_all votersID;

        // Regista a interface gráfica (GUI) como observadora no repositório
        VotingStationGUI gui = new VotingStationGUI();
        MRepository.getInstance().addObserver(gui);

        // Threads do funcionário (clerk) e do entrevistador (pollster)
        Thread tpollster;
        Thread tclerk;

        // Inicializa os monitores com as instâncias corretas
        station = MStation.getInstance(capacityCap); // Estação de votação com capacidade definida
        exitPoll = MExitPoll.getInstance(); // Sondagem de saída
        votesBooth = MVotesBooth.getInstance(); // Urna de votação
        votersID = MVotersId.getInstance(); // Gerador de IDs de eleitores

        // Cria e inicia as threads do funcionário e do entrevistador
        tclerk = new Thread(TClerk.getInstance((IStation_Clerk) station, (IVotesBooth_Clerk) votesBooth, (IExitPoll_Clerk) exitPoll));
        tclerk.start();
        tpollster = new Thread(TPollster.getInstance((IExitPoll_Pollster)exitPoll));
        tpollster.start();

        // Inicia a interface gráfica (GUI)
        VotingStationGUI.run();

        // Cria e inicia as threads dos eleitores
        for (int i = 0; i < numVoters; i++) {
           TVoter voter = TVoter.getInstance((IStation_Voter) station, (IVotesBooth_Voter) votesBooth, (IExitPoll_Voter) exitPoll, (IVoterId_Voter) votersID);
            voter.start(); // Inicia a thread do eleitor
            voters.add(voter); // Adiciona a thread à lista de eleitores
        }

        // Espera que todas as threads dos eleitores terminem
        for (TVoter voter : voters) {
            try {
                voter.join(); // Espera a thread terminar
            } catch (InterruptedException e) {
               System.err.println(e); // Log da interrupção
            }
        }

        // Interrompe as threads do funcionário e do entrevistador
        tclerk.interrupt();
        tpollster.interrupt();

        // Espera que as threads do funcionário e do entrevistador terminem
        try {
            tclerk.join();
            tpollster.join();
        } catch (InterruptedException e) {
            System.err.println(e); // Log da interrupção
        }
    }
}