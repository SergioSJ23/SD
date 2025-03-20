package main;

import java.util.Scanner;
import station.IStation_all;
import station.MStation;
import threads.TClerk;
import threads.TPollster;
import threads.TVoter;
import votesbooth.IVotesBooth;
import votesbooth.MVotesBooth;

public class Main {
    public static void main(String args[]) {


        int numVoters;
        int numVotersInquired;
        int answerPollester;
        int liePollester;
        int repeatVoter;
        int partyAodds;
        int maxVoters;
        int capacity;

        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Enter the number of voters(min 3, max 10): ");
            String input = sc.nextLine();
            if(input.isEmpty()){
                numVoters = 5;
            }else{
                numVoters = Integer.parseInt(input);
            }
            System.out.println("Enter the max number of voters: ");
            input = sc.nextLine();
            if(input.isEmpty()){
                maxVoters = 10;
            }else{
                maxVoters = Integer.parseInt(input);
            }
            System.out.println("What is the capacity of the station for voters: ");
            input = sc.nextLine();
            if(input.isEmpty()){
                capacity = 3;
            }else{
                capacity = Integer.parseInt(input);
            }
            System.out.println("Enter the percentage of votes party A will get(min 0, max 100): ");
            input = sc.nextLine();
            if(input.isEmpty()){
                partyAodds = 50;
            }else{
                partyAodds = Integer.parseInt(input);
            }
            System.out.println("Enter the percentage of voters the pollster will inquire(min 0, max 100): ");
            input = sc.nextLine();
            if(input.isEmpty()){
                numVotersInquired = 50;
            }else{
                numVotersInquired = Integer.parseInt(input);
            }
            System.out.println("Enter the percentage of people that answer the pollster(min 0, max 100): ");
            input = sc.nextLine();
            if(input.isEmpty()){
                answerPollester = 50;
            }else{
                answerPollester= Integer.parseInt(input);
            }
            System.out.println("Enter the percentage of people that lie to the pollster(min 0, max 100): ");
            input = sc.nextLine();
            if(input.isEmpty()){
                liePollester = 50;
            }else{
                liePollester = Integer.parseInt(input);
            }
            System.out.println("Enter the percentage of people that vote more than once(min 0, max 100): ");
            input = sc.nextLine();
            if(input.isEmpty()){
                repeatVoter = 50;
            }else{
                repeatVoter = Integer.parseInt(input);
            }
        }

        // Instantiate the variables with the correct values
        IStation_all station = MStation.getInstance(capacity);
        TClerk clerk = TClerk.getInstance(maxVoters);
        TPollster pollster = TPollster.getInstance(numVotersInquired, answerPollester, liePollester);
        
        IVotesBooth votingBooth = MVotesBooth.getInstance();

        for (int i = 0; i < numVoters; i++) {
            new TVoter(i, repeatVoter, partyAodds, maxVoters).start();
        }
    }
}
