package com.psk.application;

import com.psk.domain.ResultGroup;
import com.psk.domain.Server;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VotingManagerTest {

    VotingManager votingManager = new VotingManager();

    @Test
    void getGroupWinner() {
        //given
        //when
        //Double result = votingManager.getElementWinnerFromGroup()
        //then
    }

    @Test
    void shouldGetWinnerResultGroupWithMoreVotes() {
        //given
        List<ResultGroup> resultGroups = getResultGroupsList();
        ResultGroup expectedResultGroup = new ResultGroup(4, new HashSet<>(Arrays.asList(1641811175623L, 1641811175624L)));
        //when
        ResultGroup result = votingManager.getGroupWinner(resultGroups);
        //then
        assertEquals(expectedResultGroup, result);
    }

    @Test
    void shouldCreateTwoResultGroups() {
        //given
        List<Server> servers = getServers();
        List<ResultGroup> expectedResult = getResultGroupsList();
        //when
        List<ResultGroup> result = votingManager.getResultGrouped(servers, new ArrayList<>(), 1L);
        //then
        assertEquals(expectedResult, result);
    }

    private List<Server> getServers() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server((short) 1, 1641811175613L, 2));
        servers.add(new Server((short) 1, 1641811175624L, 2));
        servers.add(new Server((short) 1, 1641811175623L, 2));
        return servers;
    }

    private List<ResultGroup> getResultGroupsList() {
        List<ResultGroup> expectedResult = new ArrayList<>();
        expectedResult.add(new ResultGroup(2, Collections.singleton(1641811175613L)));
        expectedResult.add(new ResultGroup(4, new HashSet<>(Arrays.asList(1641811175623L, 1641811175624L))));
        return expectedResult;
    }
}