package com.psk.application;

import com.psk.domain.ResultGroup;
import com.psk.domain.Server;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuManager {
    public void printMainMenu() {
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

    public List<Server> assignWeightToServerFromUser(Scanner sc, List<Server> servers) {
/*        if (timesAlreadySet) {
            System.out.println("Times are already set");
            return;
        }*/
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
        //allAssignedWeightsNum = allAssignedWeightsNum - previousWeight + weightFromUser;
        //timesAlreadySet = true;
        pauseLoopUntilEnterPressed();
        return servers;
    }

    public void printServersTimes(List<Server> servers) {
        servers.forEach(s -> System.out.printf("Time of server nr %s: %s%n", s.getId(), s.getTime()));
        pauseLoopUntilEnterPressed();
    }

    public void printServersWeights(List<Server> servers) {
        servers.forEach(s -> System.out.printf("Weight of server nr %s: %s%n", s.getId(), s.getWeight()));
        pauseLoopUntilEnterPressed();
    }

    public List<Server> createThreadsForServersWithCurrentTime(List<Server> servers) {
        servers.forEach(s -> {
            long finalTimestamp = getTimestamp(servers);
            Thread thread = new Thread(() -> s.setTime(finalTimestamp));
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                //loop = false;
            }
        });

        System.out.println("Created threads and assigned time for each server.");
        pauseLoopUntilEnterPressed();
        return servers;
    }

    public Long getEpsilonFromUser(Scanner sc) {
        System.out.println("Write epsilon:");
        Long epsilon = sc.nextLong();
        pauseLoopUntilEnterPressed();
        return epsilon;
    }

    public void printEpsilon(Long epsilon) {
        System.out.println(epsilon);
        pauseLoopUntilEnterPressed();
    }

    public boolean pauseLoopUntilEnterPressed() {
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static long getTimestamp(List<Server> servers) {
        long timestamp;
        do {
            timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
        } while (timestampExists(timestamp, servers));
        return timestamp;
    }

    private static boolean timestampExists(Long timestamp, List<Server> servers) {
        return servers.stream().anyMatch(server -> timestamp.equals(server.getTime()));
    }

}
