package com.psk.application;

import com.psk.domain.Server;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BugInjector {
    public List<Server> clearRandomServerTime(List<Server> servers) {
        int length = servers.size();
        int randomNo;
        randomNo = ThreadLocalRandom.current().nextInt(0, length);
        System.out.println("BUG INJECTOR - I'm cleaning time of server no " + randomNo);
        servers.get(randomNo).setTime(0);

        return servers;
    }

    public Long setEpsilonNegative() {
        System.out.println("BUG INJECTOR - I'm setting negative value to epsilon");
        return -1L;
    }
}
