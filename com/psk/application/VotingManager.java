package com.psk.application;

import com.psk.domain.ResultGroup;
import com.psk.domain.Server;

import java.util.*;
import java.util.stream.Collectors;

public class VotingManager {

    public void processVoting(List<Server> servers, List<ResultGroup> groups, Long epsilon, MenuManager menuManager) {
        List<Server> serversCopy = new ArrayList<>(servers);

        // fighting against first bug injector
        boolean elementRemoved = serversCopy.removeIf(el -> (el.getTime() == 0));
        if (elementRemoved) {
            System.out.printf("BUG DETECTED! Removed servers with time = 0\n");
        }

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


}
