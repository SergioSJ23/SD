package main;

import capacity.*;
import exitpoll.*;
import java.util.Scanner;
import station.*;
import threads.*;
import votelimit.*;
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

        // Instantiate the variables with the correct values
        IStation_all station = MStation.getInstance(capacityCap);
        ICapacity capacity = MCapacity.getInstance(capacityCap);
        IVoteLimit_all voteLimit = MVoteLimit.getInstance();
        IExitPoll_all exitPoll = MExitPoll.getInstance();
        IVotesBooth_all votesBooth = MVotesBooth.getInstance();

        new Thread(TClerk.getInstance()).start();
        new Thread(TPollster.getInstance()).start();

        for (int i = 0; i < numVoters; i++) {
            new TVoter(i).start(station, votesBooth, exitPoll);
        }

        
    }
}
