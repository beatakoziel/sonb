package com.psk;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    static boolean loop = true;
    static Long epsilon = 0L;
    static List<ResultGroup> groups = new ArrayList<>();
    static List<Server> servers = new ArrayList<>();

    //TODO ustawic zmienna zawierajaca liczbe wszystkich glosow
    //ustawic wartosc progowa rozstzygajaca w przypadku jakby wiecej niz 1 wartosc miala wiecej niz 50% glosow
    //zrobic wylonienie grupy zwycieskiej
    //zrobic wylonienie zwyciezcy z grupy finalowej poprzez srednia

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

        servers.stream().filter(s -> s.getId().equals(weightAssignChoice))
                .findFirst()
                .ifPresent(s -> s.setWeight(weightFromUser));

        pauseLoopUntilEnterPressed();
    }

    private static List<Server> createServers() {
        List<Server> servers = new ArrayList<>();
        for (short i = 1; i <= 8; i++) {
            Server s = new Server(i);
            servers.add(s);
        }
        return servers;
    }

    private static void printServersTimes() {
        servers.forEach(s -> System.out.printf("Time of server nr %s: %s%n", s.getId(), s.getTime()));
        pauseLoopUntilEnterPressed();
    }

    private static void printServersWeights() {
        servers.forEach(s -> System.out.printf("Weight of server nr %s: %s%n", s.getId(), s.getWeight()));
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
        System.out.println(groups);
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
