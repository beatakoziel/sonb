package com.psk.application;

import com.psk.domain.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class BugInjector {
    public List<Server> choseBug(Scanner sc, List<Server> servers) {
        List<Server> resultServers = new ArrayList<>();
        System.out.println("BUG INJECTIONS");
        System.out.println("1. Clear random servers time");
        System.out.println("2. xxx");
        System.out.println("3. xxx");

        System.out.println("Chose bug");
        short bug = sc.nextShort();
        switch (bug){
            case 1:
                resultServers = clearRandomServer(servers);
                break;
        }

        return resultServers;
    }

    public List<Server> clearRandomServer(List<Server> servers) {
        int length = servers.size();
        int randomNo;
        randomNo = ThreadLocalRandom.current().nextInt(0, length);
        System.out.println("BUG INJECTOR - I'm cleaning time of server no " + randomNo);
        servers.get(randomNo).setTime(0);

        return servers;
    }
}
