package com.psk.application;

import com.psk.domain.Server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BugInjector {
    public List<Server> clearRandomServerTime(List<Server> servers) {
        int length = servers.size();
        int randomNo;
        randomNo = randomInt(length);
        System.out.println("BUG INJECTOR - I'm cleaning time of server no " + randomNo);
        servers.get(randomNo).setTime(0);

        return servers;
    }

    public Long setEpsilonNegative() {
        System.out.println("BUG INJECTOR - I'm setting negative value to epsilon");
        return -1L;
    }

    public List<Server> setAbnormalServerTime(List<Server> servers) {
        for(Server s : servers)
            if(s == null || s.getTime() == null) {
                System.out.println("BUG INJECTOR - servers or their times are not set yet.");
                return servers;
            }
        int randomId = randomInt(servers.size());
        long randomTime = randomLong(servers.get(randomId).getTime() * randomLong(3, 13));
        servers.get(randomId).setTime(randomTime);
        System.out.println("BUG INJECTOR - The " + randomId + " server time was set to " + servers.get(randomId).getTime());
        return servers;
    }

    private int randomInt(int max) { return randomInt(0, max); }

    private int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private long randomLong(long max) { return randomLong(0L, max); }

    private long randomLong(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }
}
