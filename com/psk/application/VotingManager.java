package com.psk.application;

import com.psk.domain.ResultGroup;
import com.psk.domain.Server;

import java.util.*;
import java.util.stream.Collectors;

public class VotingManager {

    public void processVoting(List<Server> servers, List<ResultGroup> groups, Long epsilon, MenuManager menuManager) {
        List<Server> serversCopy = new ArrayList<>(servers),
                toRemove = new ArrayList<>();
        List<Long> timeOutliners = getTimeOutliers(serversCopy);

        // fighting against first bug injector
        boolean elementRemoved = serversCopy.removeIf(el -> (el.getTime() == 0));
        if (elementRemoved) {
            System.out.printf("BUG DETECTED! Removed servers with time = 0\n");
        }
        serversCopy.stream()
                .filter(sc -> timeOutliners.contains(sc.getTime()))
                .forEach(s -> {
                    System.out.println("BUG DETECTED! Removed server " + s.getId() + " with time = " + s.getTime());
                    toRemove.add(s);
                });
        servers.removeAll(toRemove);

        groups = getResultGrouped(serversCopy, groups, epsilon);
        System.out.println(groups);
        ResultGroup winner = getGroupWinner(groups);
        if (winner != null) {
            System.out.println("Voting group winner => " + winner);
            Long elementWinner = getElementWinnerFromGroup(winner);
            if (elementWinner != null && elementWinner != 0) {
                System.out.println("Voting group element winner => " + elementWinner);
            }
        }
        menuManager.pauseLoopUntilEnterPressed();
    }

    protected ResultGroup getGroupWinner(List<ResultGroup> result) {
        if (result.size() == 1) {
            return result.get(0);
        } else if (result.size() > 1) {
            return result.stream()
                    .max(Comparator.comparing(ResultGroup::getVotesNumber))
                    .orElse(null);
        } else {
            System.out.println("Something went wrong.");
            return null;
        }
    }

    protected Long getElementWinnerFromGroup(ResultGroup group) {
        List<Server> groupList = new ArrayList<>(group.getGroupElements());
        if (groupList.size() == 1) {
            return groupList.get(0).getTime();
        } else if (groupList.size() > 1) {
            return group.getGroupElements().stream()
                    .max(Comparator.comparing(Server::getWeight))
                    .orElseThrow(NoSuchElementException::new)
                    .getTime();
        } else {
            System.out.println("Something went wrong.");
            return null;
        }
    }

    protected List<ResultGroup> getResultGrouped(List<Server> servers, List<ResultGroup> groups, Long epsilon) {
        if (epsilon <= 0) {
            System.out.printf("BUG DETECTED! Epsilon is a negative value so I reset to value 1\n");
            epsilon = 1L;
        }
        if (servers.size() != 0) {
            for (int i = 0; i < servers.size(); i++) {
                Long time = servers.get(i).getTime();
                List<Server> groupServer = getGroupServers(servers, time, epsilon);
                Integer votesNumber = getVotesNumber(groupServer);
                groups.add(new ResultGroup(votesNumber, new HashSet<>(groupServer)));
                servers.removeAll(groupServer);
                getResultGrouped(servers, groups, epsilon);
            }
        }
        return groups;
    }

    private Integer getVotesNumber(List<Server> groupServer) {
        return groupServer.stream()
                .map(Server::getWeight)
                .mapToInt(Integer::intValue).sum();
    }

    private List<Server> getGroupServers(List<Server> servers, Long time, Long epsilon) {
        return servers.stream()
                .filter(s -> ((s.getTime() <= (time + epsilon)) && (s.getTime() >= (time - epsilon))))
                .collect(Collectors.toList());
    }

    private List<Long> getTimeOutliers(List<Server> servers) {
        List<Long> output = new ArrayList<>(),
                input = new ArrayList<>();
        List<Long> data1, data2;
        // create input list
        servers.forEach(server -> input.add(server.getTime()));
        // sort input
        Collections.sort(input);
        // split into 2 parts by middle
        if (input.size() % 2 == 0) {
            data1 = input.subList(0, input.size() / 2);
            data2 = input.subList(input.size() / 2, servers.size());
        } else {
            data1 = input.subList(0, input.size() / 2);
            data2 = input.subList(input.size() / 2 + 1, input.size());
        }
        // get quartile1 and quartile3 (1/4 and 3/4 medians)
        double q1 = getMedian(data1);
        double q3 = getMedian(data2);
        double iqr = q3 - q1;
        double lowerFence = q1 - 1.5 * iqr;
        double upperFence = q3 + 1.5 * iqr;
        // loop through all of original elements, and get outliers
        for (int i = 0; i < input.size(); i++) {
            if (input.get(i) < lowerFence || input.get(i) > upperFence)
                output.add(input.get(i));
        }
        return output;
    }

    private Long getMedian(List<Long> data) {
        if (data.size() % 2 == 0)
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        else
            return data.get(data.size() / 2);
    }
}
