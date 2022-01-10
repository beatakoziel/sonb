package com.psk;

import com.psk.application.MenuManager;
import com.psk.application.VotingManager;
import com.psk.domain.ResultGroup;
import com.psk.domain.Server;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

/*    static boolean loop = true;
    static boolean timesAlreadySet = false;
    static Long epsilon = 1L;
    static List<ResultGroup> groups = new ArrayList<>();
    static List<Server> servers = new ArrayList<>();*/
    //static Double votesPartToAchieveWhenTie = 0.6; //how much of the value of all votes to be achieved in case of a
    // tie - when more than 1 set exceeds 50% of the votes
    //static Integer allAssignedWeightsNum = 0;

    public static void main(String[] args) {
        boolean loop = true;
        Long epsilon = 1L;
        List<ResultGroup> groups = new ArrayList<>();
        List<Server> servers = new ArrayList<>();
        servers = createServers();
        Scanner sc = new Scanner(System.in);
        MenuManager menuManager = new MenuManager();
        VotingManager votingManager = new VotingManager();
        while (loop) {
            menuManager.printMainMenu();
            int menuChoice = sc.nextInt();

            switch (menuChoice) {
                case 1:
                    servers = menuManager.createThreadsForServersWithCurrentTime(servers);
                    break;
                case 2:
                    servers = menuManager.assignWeightToServerFromUser(sc, servers);
                    break;
                case 3:
                    epsilon = menuManager.getEpsilonFromUser(sc);
                    break;
                case 4:
                    menuManager.printServersTimes(servers);
                    break;
                case 5:
                    menuManager.printServersWeights(servers);
                    break;
                case 6:
                    menuManager.printEpsilon(epsilon);
                    break;
                case 7:
                    groups = new ArrayList<>();
                    votingManager.processVoting(servers, groups, epsilon, menuManager);
                    break;
                case 8:
                    loop = false;
                    break;
                default:
                    System.out.println("Make sure that you selected correct number from the menu.");
                    menuManager.pauseLoopUntilEnterPressed();
            }
        }
    }

    private static List<Server> createServers() {
        List<Server> servers = new ArrayList<>();
        for (short i = 1; i <= 8; i++) {
            int weight = 1;
            Server s = new Server(i, weight);
            servers.add(s);
            //allAssignedWeightsNum += weight;
        }
        return servers;
    }


}
