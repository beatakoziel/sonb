package com.psk;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static boolean loop = true;
    static Long epsilon = 0L;
    static List<ResultGroup> groups = new ArrayList<>();
    static List<Server> servers = new ArrayList<>();
    static Double votesPartToAchieveWhenTie = 0.6; //how much of the value of all votes to be achieved in case of a tie - when more than 1 set exceeds 50% of the votes
    static Integer allAssignedWeightsNum = 0;
    // co w przypadku remisu grup
    // co w przypadku braku wylonienia zwyciezcy
    // zrobic wylonienie zwyciezcy z grupy finalowej poprzez srednia?

    public static void main(String[] args) {
        servers = createServers();
        Scanner sc = new Scanner(System.in);

        while (loop) {
            printMainMenu();
            int menuChoice = sc.nextInt();

            switch (menuChoice) {
                case 1:
                    createThreadsForServersWithCurrentTime();
                    break;
                case 2:
                    assignWeightToServerFromUser(sc);
                    break;
                case 3:
                    assignEpsilon(sc);
                    break;
                case 4:
                    printServersTimes();
                    break;
                case 5:
                    printServersWeights();
                    break;
                case 6:
                    printEpsilon();
                    break;
                case 7:
                    groupValues();
                    break;
                case 8:
                    loop = false;
                    break;
                default:
                    System.out.println("Make sure that you selected correct number from the menu.");
                    pauseLoopUntilEnterPressed();
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("========= MENU =========");
        System.out.println("1. Set servers times.");
        System.out.println("2. Set server weight.");
        System.out.println("3. Set epsilon - allowed difference between signals.");
        System.out.println("4. Print servers times.");
        System.out.println("5. Print servers weights.");
        System.out.println("6. Print epsilon.");
        System.out.println("7. Group signals.");
        System.out.println("8. Exit.");
        System.out.println("Choose option from menu:");
    }

    private static void assignWeightToServerFromUser(Scanner sc) {
        System.out.println("Write number of server you would like to assign weight to:");
        short weightAssignChoice = sc.nextShort();

        System.out.println("Write weight:");
        int weightFromUser = sc.nextInt();

        int previousWeight = servers.stream().filter(s -> s.getId().equals(weightAssignChoice))
                .findFirst()
                .orElse(new Server((short) 0, 0))
                .getWeight();
        servers.stream().filter(s -> s.getId().equals(weightAssignChoice))
                .findFirst()
                .ifPresent(s -> s.setWeight(weightFromUser));
        allAssignedWeightsNum = allAssignedWeightsNum - previousWeight + weightFromUser;
        pauseLoopUntilEnterPressed();
    }

    private static List<Server> createServers() {
        List<Server> servers = new ArrayList<>();
        for (short i = 1; i <= 8; i++) {
            int weight = (int) ((Math.random() * (5 - 1)) + 1);
            Server s = new Server(i, weight);
            servers.add(s);
            allAssignedWeightsNum += weight;
        }
        return servers;
    }

    private static void printServersTimes() {
        servers.forEach(s -> System.out.printf("Time of server nr %s: %s%n", s.getId(), s.getTime()));
        pauseLoopUntilEnterPressed();
    }

    private static void printServersWeights() {
        servers.forEach(s -> System.out.printf("Weight of server nr %s: %s%n", s.getId(), s.getWeight()));
        System.out.println("Sum of weights: " + allAssignedWeightsNum);
        System.out.println("Votes sum to achieve when tie: " + allAssignedWeightsNum * votesPartToAchieveWhenTie);
        pauseLoopUntilEnterPressed();
    }

    private static void createThreadsForServersWithCurrentTime() {
        servers.forEach(s -> {
            Thread thread = new Thread(() -> s.setTime(Timestamp.valueOf(LocalDateTime.now()).getTime()));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                loop = false;
            }
        });

        System.out.println("Created threads and assigned time for each server.");
        pauseLoopUntilEnterPressed();
    }

    private static void assignEpsilon(Scanner sc) {
        System.out.println("Write epsilon:");
        epsilon = sc.nextLong();
        pauseLoopUntilEnterPressed();
    }

    private static void printEpsilon() {
        System.out.println(epsilon);
        pauseLoopUntilEnterPressed();
    }

    private static void groupValues() {
        List<Server> serversCopy = new ArrayList<>(servers);
        setResultGrouped(serversCopy);
        Integer maxVotesNum = getMaxVotesNum();
        List<ResultGroup> result = getBestResultGroups(maxVotesNum);
        System.out.println(groups);
        ResultGroup winner = getWinner(result);
        if (winner != null) {
            System.out.println("Voting group winner => " + winner);
            Double elementWinner = getElementWinnerFromGroup(winner);
            if (elementWinner != null && elementWinner != 0) {
                System.out.println("Voting group element winner => " + elementWinner.longValue());
            }
        }
    }

    private static ResultGroup getWinner(List<ResultGroup> result) {
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() == 2) {
            //ktory ma najwieksza srednia wag
            List<ResultGroup> tieResult = groups.stream()
                    .filter(g -> g.getVotesNumber() >= (allAssignedWeightsNum * votesPartToAchieveWhenTie))
                    .collect(Collectors.toList());
            if (tieResult.size() == 1) {
                return tieResult.get(0);
            } else {
                System.out.println("You should change value of votes part to achieve when tie.");
            }
        } else {
            System.out.println("Something went wrong.");
        }
        return null;
    }

    private static Double getElementWinnerFromGroup(ResultGroup group) {
        List<Long> groupList = new ArrayList<>(group.getGroupElements());
        if (groupList.size() == 1) {
            return Double.valueOf(groupList.get(0));
        } else if (groupList.size() > 1) {
            return groupList.stream().mapToLong(e -> e).average().orElse(0);
        } else {
            System.out.println("Something went wrong.");
        }
        return null;
    }

    private static List<ResultGroup> getBestResultGroups(Integer maxVotesNum) {
        return groups.stream()
                .filter(g -> g.getVotesNumber().equals(maxVotesNum))
                .collect(Collectors.toList());
    }

    private static Integer getMaxVotesNum() {
        return groups.stream()
                .map(ResultGroup::getVotesNumber)
                .mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
    }

    private static void setResultGrouped(List<Server> servers) {
        if (servers.size() != 0) {
            for (int i = 0; i < servers.size(); i++) {
                Long time = servers.get(i).getTime();
                List<Server> groupServer = getGroupServers(servers, time);
                Integer votesNumber = getVotesNumber(groupServer);
                Set<Long> groupElements = getGroupElements(groupServer);
                groups.add(new ResultGroup(votesNumber, groupElements));
                servers.removeAll(groupServer);
                setResultGrouped(servers);
            }
        }
    }

    private static Set<Long> getGroupElements(List<Server> groupServer) {
        return groupServer.stream()
                .map(Server::getTime)
                .collect(Collectors.toSet());
    }

    private static Integer getVotesNumber(List<Server> groupServer) {
        return groupServer.stream()
                .map(Server::getWeight)
                .mapToInt(Integer::intValue).sum();
    }

    private static List<Server> getGroupServers(List<Server> servers, Long time) {
        return servers.stream()
                .filter(s -> ((s.getTime() <= (time + epsilon)) && (s.getTime() >= (time - epsilon))))
                .collect(Collectors.toList());
    }

    private static void pauseLoopUntilEnterPressed() {
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
            loop = false;
        }
    }
}
