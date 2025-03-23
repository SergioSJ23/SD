package main;

import exitpoll.*;
import gui.VoterObserver;
import gui.VotingStationGUI;
import java.util.List;
import java.util.Scanner;
import station.*;
import threads.*;
import votesbooth.*;

public class Main {
    private static int numVoters;
    private static int capacityCap;
    private static List<TVoter> voters = new java.util.ArrayList<>();

    public static int getNumVoters(){
        return numVoters;
    } 

    public static int getCapacityCap(){
        return capacityCap;
    }

    public static List<TVoter> getVoters(){
        return voters;
    }

    public static void main(String args[]) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter the number of voters(min 3, max 10): ");
            String input = sc.nextLine();
            if(input.isEmpty()){
                numVoters = 5;
            }else{
                numVoters = Integer.parseInt(input);
            }
            System.out.println("What is the capacity of the station for voters: ");
            input = sc.nextLine();
            if(input.isEmpty()){
                capacityCap = 3;
            }else{
                capacityCap = Integer.parseInt(input);
            }
        }

        //monitors
        IStation_all station;
        IExitPoll_all exitPoll;
        IVotesBooth_all votesBooth;

        //threads
        Thread tpollster;
        Thread tclerk;
        
        // Register the observer
        VoterObserver observer = new VotingStationGUI();
        
        // Instantiate the variables with the correct values
        station = MStation.getInstance(capacityCap);
        exitPoll = MExitPoll.getInstance();
        votesBooth = MVotesBooth.getInstance();

        // Create the threads
        tclerk = new Thread(TClerk.getInstance());
        tclerk.start();

        tpollster = new Thread(TPollster.getInstance((IExitPoll_Pollster)exitPoll));
        tpollster.start();

        // Start the GUI
        //VotingStationGUI.run();

        for (int i = 0; i < numVoters; i++) {
            TVoter voter = new TVoter((IStation_Voter) station, (IVotesBooth_Voter) votesBooth, (IExitPoll_Voter) exitPoll);
            voter.registerObserver(observer);
            voter.start();
            voters.add(voter); // Adiciona a thread à lista
        }

        // Espera que todas as threads terminem
        for (TVoter voter : voters) {
            try {
                voter.join(); // Espera a thread terminar
                System.out.println("Voter " + voter.getId() + " is DOOOOOOONNEEEE");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        tclerk.interrupt();
        tpollster.interrupt();

        // Wait for the threads to finish
        try {
            tclerk.join();
            tpollster.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}
