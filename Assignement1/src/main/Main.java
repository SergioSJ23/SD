package main;

import exitpoll.*;
import java.util.Scanner;
import station.*;
import threads.*;
import votesbooth.*;

public class Main {
    public static void main(String args[]) {


        int numVoters;
        int capacityCap;

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


        // Instantiate the variables with the correct values
        station = MStation.getInstance(capacityCap);
        exitPoll = MExitPoll.getInstance();
        votesBooth = MVotesBooth.getInstance();

        // Create the threads
        tclerk = new Thread(TClerk.getInstance());
        tclerk.start();

        tpollster = new Thread(TPollster.getInstance((IExitPoll_Pollster)exitPoll));
        tpollster.start();



        for (int i = 0; i < numVoters; i++) {
            new TVoter(i,(IStation_Voter)station,(IVotesBooth_Voter)votesBooth, (IExitPoll_Voter)exitPoll).start();
        }

        
        
    }
}
