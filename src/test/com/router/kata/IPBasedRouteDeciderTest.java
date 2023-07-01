package com.router.kata;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class IPBasedRouteDeciderTest {
    static {
        BasicConfigurator.configure();
    }

    @Test
    public void validateRouteDecider() {
        List<String> blockedIPs = Arrays.asList(
                "192.168.0.250",
                "192.168.0.251",
                "192.168.0.252",
                "192.168.0.253",
                "192.168.0.254",
                "192.168.0.255",
                "10.10.10.10");

        IPBasedRouteDecider decider = new IPBasedRouteDecider(new HashSet<>(blockedIPs));

        for (String blockedIP : blockedIPs) {
            Assertions.assertFalse(decider.shouldRoute(new Router.Packet("192.168.0.1", blockedIP)));
        }
        for (int idx = 100; idx < 150; idx++) {
            Assertions.assertTrue(decider.shouldRoute(new Router.Packet("192.168.0.1", String.format("192.168.0.%d", idx))));
        }
        for (int idx = 100; idx < 150; idx++) {
            Assertions.assertTrue(decider.shouldRoute(new Router.Packet("192.168.0.1", String.format("10.10.10.%d", idx))));
        }

    }
}
