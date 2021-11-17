package com.psk;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static boolean loop = true;

    public static void main(String[] args) {
        List<Server> servers = createServers();
        Scanner sc = new Scanner(System.in);

        while (loop) {
            printMainMenu();
            int menuChoice = sc.nextInt();

            switch (menuChoice) {
                case 1:
                    createThreadsForServersWithCurrentTime(servers);
                    break;
                case 2:
                    assignWeightToServerFromUser(servers, sc);
                    break;
                case 3:
                    printServersTimes(servers, sc);
                    break;
                case 4:
                    printServersWeights(servers);
                    break;
                case 5:
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
        System.out.println("3. Print servers times.");
        System.out.println("4. Print servers weights.");
        System.out.println("5. Exit.");
        System.out.println("Choose option from menu:");
    }

    private static void assignWeightToServerFromUser(List<Server> servers, Scanner sc) {
        System.out.println("Write number of server you would like to assign weight to:");
        short weightAssignChoice = sc.nextShort();

        System.out.println("Write weight:");
        int weightFromUser = sc.nextInt();

        servers.stream().filter(s -> s.getId().equals(weightAssignChoice))
                .findFirst()
                .ifPresent(s->s.setWeight(weightFromUser));

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

    private static void printServersTimes(List<Server> servers, Scanner sc) {
        servers.forEach(s -> System.out.printf("Time of server nr %s: %s%n", s.getId(), s.getTime()));
        pauseLoopUntilEnterPressed();
    }

    private static void printServersWeights(List<Server> servers) {
        servers.forEach(s -> System.out.printf("Weight of server nr %s: %s%n", s.getId(), s.getWeight()));
        pauseLoopUntilEnterPressed();
    }

    private static void createThreadsForServersWithCurrentTime(List<Server> servers) {
        servers.forEach(s -> {
            Thread thread = new Thread(() -> s.setTime(LocalDateTime.now()));
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
