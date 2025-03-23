package main;

import exitpoll.*;
import gui.VoterObserver;
import gui.VotingStationGUI;
import java.util.List;
import java.util.Scanner;
import station.*;
import threads.*;
import votersId.*;
import votesbooth.*;

public class Main {
    private static int numVoters = 0;
    private static int capacityCap = 0;
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
            String input;

            
            do{
                try
                {
                    System.out.println("Enter the number of voters(min 3, max 10): ");
                    input = sc.nextLine();
                    numVoters = Integer.parseInt(input);
                } catch(NumberFormatException e){
                    System.out.println("Invalid input");            
                }
            }while(numVoters  < 3 || numVoters > 10);

            do{
                try
                {
                    System.out.println("Enter the capacity cap(min 2, max 5): ");
                    input = sc.nextLine();
                    capacityCap = Integer.parseInt(input);
                } catch(NumberFormatException e){
                    System.out.println("Invalid input");
                }
            }while(capacityCap  < 2 || capacityCap > 5);
            

        }

        //monitors
        IStation_all station;
        IExitPoll_all exitPoll;
        IVotesBooth_all votesBooth;
        IVotersId_all votersID;

        // Register the observer
        VoterObserver observer = new VotingStationGUI();
        


        //threads
        Thread tpollster;
        Thread tclerk;
        
        
        
        // Instantiate the variables with the correct values
        station = MStation.getInstance(capacityCap);
        exitPoll = MExitPoll.getInstance();
        votesBooth = MVotesBooth.getInstance();
        votersID = MVotersId.getInstance();

        // Create the threads
        tclerk = new Thread(TClerk.getInstance());
        tclerk.start();
        tpollster = new Thread(TPollster.getInstance((IExitPoll_Pollster)exitPoll));
        tpollster.start();

        // Start the GUI
        //VotingStationGUI.run();

        for (int i = 0; i < numVoters; i++) {
            TVoter voter = new TVoter((IStation_Voter) station, (IVotesBooth_Voter) votesBooth, (IExitPoll_Voter) exitPoll, (IVoterId_Voter) votersID);
            voter.registerObserver(observer);
            voter.start();
            voters.add(voter); // Adiciona a thread à lista
        }

        // Espera que todas as threads terminem
        for (TVoter voter : voters) {
            try {
                voter.join(); // Espera a thread terminar
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
