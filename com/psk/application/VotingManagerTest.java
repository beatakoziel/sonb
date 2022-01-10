package com.psk.application;

import com.psk.domain.ResultGroup;
import com.psk.domain.Server;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class VotingManagerTest {

    VotingManager votingManager = new VotingManager();

    public static final Server SERVER_1 = new Server((short) 1, 1641811175613L, 2);
    public static final Server SERVER_2 = new Server((short) 2, 1641811175623L, 3);
    public static final Server SERVER_3 = new Server((short) 3, 1641811175624L, 4);
    public static final Server SERVER_4 = new Server((short) 3, 1641811175625L, 4);

    @Test
    void shouldGetElementWinnerWithMoreVotes() {
        //given
        ResultGroup resultGroup = new ResultGroup(7, new HashSet<>(Arrays.asList(SERVER_2, SERVER_3)));
        //when
        Long result = votingManager.getElementWinnerFromGroup(resultGroup);
        //then
        assertEquals(SERVER_3.getTime(), result);
    }

    @Test
    void shouldGetOneOfEqualWeightElementAsAWinner() {
        //given
        ResultGroup resultGroup = new ResultGroup(8, new HashSet<>(Arrays.asList(SERVER_2, SERVER_4, SERVER_3)));
        //when
        Long result = votingManager.getElementWinnerFromGroup(resultGroup);
        //then
        assertTrue(result.equals(SERVER_3.getTime()) || result.equals(SERVER_4.getTime()));
    }

    @Test
    void shouldGetWinnerResultGroupWithMoreVotes() {
        //given
        List<ResultGroup> resultGroups = getResultGroupsList();
        ResultGroup expectedResultGroup = new ResultGroup(7, new HashSet<>(Arrays.asList(SERVER_2, SERVER_3)));
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
        servers.add(SERVER_1);
        servers.add(SERVER_2);
        servers.add(SERVER_3);
        return servers;
    }

    private List<ResultGroup> getResultGroupsList() {
        List<ResultGroup> expectedResult = new ArrayList<>();
        expectedResult.add(new ResultGroup(2, Collections.singleton(SERVER_1)));
        expectedResult.add(new ResultGroup(7, new HashSet<>(Arrays.asList(SERVER_2, SERVER_3))));
        return expectedResult;
    }
}