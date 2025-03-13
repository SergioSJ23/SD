package voter;

import java.util.Random;
import java.util.Scanner;

public class ElectionSimulation {
    public static void main(String args[]) {

        Random rand = new Random();

        int numVoters;
        int waitingQueue;
        int numVotersInquired;
        int answerPollester;
        int liePollester;
        int repeatVoter;
        char vote;
        int partyAodds;

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of voters(min 3, max 10): ");
        numVoters = sc.nextInt();
        System.out.println("Enter max number of waiting voters(min 2, max 5): ");
        waitingQueue = sc.nextInt();
        System.out.println("Enter the percentage of votes party A will get(min 0, max 100): ");
        partyAodds = sc.nextInt();
        System.out.println("Enter the percentage of voters the pollster will inquire(min 0, max 100): ");
        numVotersInquired = sc.nextInt();
        System.out.println("Enter the percentage of people that answer the pollster(min 0, max 100): ");
        answerPollester = sc.nextInt();
        System.out.println("Enter the percentage of people that lie to the pollster(min 0, max 100): ");
        liePollester = sc.nextInt();
        System.out.println("Enter the percentage of people that vote more than once(min 0, max 100): ");
        repeatVoter = sc.nextInt();

        sc.close();

        for (int i = 0; i < numVoters; i++) {
            vote = ' ';
            new Voter(i, vote, answerPollester, liePollester, repeatVoter, partyAodds).start();
        }
    }
}
