package com.psk.application;

import com.psk.domain.ResultGroup;
import com.psk.domain.Server;

import java.util.*;
import java.util.stream.Collectors;

public class VotingManager {

    public void processVoting(List<Server> servers, List<ResultGroup> groups, Long epsilon, MenuManager menuManager) {
        List<Server> serversCopy = new ArrayList<>(servers);
        groups = getResultGrouped(serversCopy, groups, epsilon);
        System.out.println(groups);
        ResultGroup winner = getGroupWinner(groups);
        if (winner != null) {
            System.out.println("Voting group winner => " + winner);
            Double elementWinner = getElementWinnerFromGroup(winner);
            if (elementWinner != null && elementWinner != 0) {
                System.out.println("Voting group element winner => " + elementWinner.longValue());
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

    protected Double getElementWinnerFromGroup(ResultGroup group) {
        List<Long> groupList = new ArrayList<>(group.getGroupElements());
        if (groupList.size() == 1) {
            return Double.valueOf(groupList.get(0));
        } else if (groupList.size() > 1) {
            return groupList.stream().mapToLong(e -> e).average().orElse(0);
        } else {
            System.out.println("Something went wrong.");
            return null;
        }
    }

/*    private Integer getMaxVotesNum(List<ResultGroup> groups) {
        return groups.stream()
                .map(ResultGroup::getVotesNumber)
                .mapToInt(v -> v)
                .max().orElseThrow(NoSuchElementException::new);
    }*/

    protected List<ResultGroup> getResultGrouped(List<Server> servers, List<ResultGroup> groups, Long epsilon) {
        if (servers.size() != 0) {
            for (int i = 0; i < servers.size(); i++) {
                Long time = servers.get(i).getTime();
                List<Server> groupServer = getGroupServers(servers, time, epsilon);
                Integer votesNumber = getVotesNumber(groupServer);
                Set<Long> groupElements = getGroupElements(groupServer);
                groups.add(new ResultGroup(votesNumber, groupElements));
                servers.removeAll(groupServer);
                getResultGrouped(servers, groups, epsilon);
            }
        }
        return groups;
    }

    private Set<Long> getGroupElements(List<Server> groupServer) {
        return groupServer.stream()
                .map(Server::getTime)
                .collect(Collectors.toSet());
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