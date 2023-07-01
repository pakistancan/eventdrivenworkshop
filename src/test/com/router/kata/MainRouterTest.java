package com.router.kata;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class MainRouterTest {

    static {
        BasicConfigurator.configure();
    }

    @Test
    public void validateRoutingAndBlocking() {

        MainRouter mainRouter = new MainRouter();
        MockRouter khiRouter = new MockRouter();
        MockRouter lheRouter = new MockRouter();
        MockRouter isbRouter = new MockRouter();
        List<String> blockedIPs = Arrays.asList(
                "192.168.0.250",
                "192.168.0.251",
                "192.168.0.252",
                "192.168.0.253",
                "192.168.0.254",
                "192.168.0.255",
                "10.10.10.10");
        IPBasedRouteDecider routerDecider = new IPBasedRouteDecider(new HashSet<>(blockedIPs));

        mainRouter.addRouter("192.168", khiRouter);
        mainRouter.addRouter("172.13", lheRouter);
        mainRouter.addRouter("10.10", isbRouter);
        mainRouter.addRouteDecider(routerDecider);

        String sourceIp = "172.16.10.11";
        String[] ips = new String[]{"192.168.0.1", "192.168.0.1", "192.168.0.1", "10.10.10.0", "172.13.10.10", "192.168.0.251", "192.168.0.252", "192.168.0.253", "192.168.0.241", "192.168.0.231", "10.10.10.100", "10.10.10.10"};
        for (String ip : ips) {
            Router.Packet packet = new Router.Packet(sourceIp, ip);
            mainRouter.route(packet);
        }
        mainRouter.shutdown();

        Map<String, List<Router.Packet>> packetsMap = khiRouter.packetsReceived.stream().collect(Collectors.groupingBy(Router.Packet::getTargetIp));
        Assertions.assertEquals(packetsMap.get("192.168.0.1").size(), 3);
        Assertions.assertEquals(packetsMap.get("192.168.0.231").size(), 1);
        Assertions.assertEquals(packetsMap.get("192.168.0.241").size(), 1);
        Assertions.assertNull(packetsMap.get("192.168.0.251"));
        Assertions.assertNull(packetsMap.get("192.168.0.252"));
        Assertions.assertNull(packetsMap.get("192.168.0.253"));


        packetsMap = lheRouter.packetsReceived.stream().collect(Collectors.groupingBy(Router.Packet::getTargetIp));

        Assertions.assertEquals(packetsMap.get("172.13.10.10").size(), 1);

        packetsMap = isbRouter.packetsReceived.stream().collect(Collectors.groupingBy(Router.Packet::getTargetIp));
        Assertions.assertEquals(packetsMap.get("10.10.10.100").size(), 1);
        Assertions.assertNull(packetsMap.get("10.10.10.10"));
        Assertions.assertNotNull(packetsMap.get("10.10.10.0"));


        packetsMap = routerDecider.getBlockedPackets().stream().collect(Collectors.groupingBy(Router.Packet::getTargetIp));
        Assertions.assertEquals(packetsMap.get("192.168.0.251").size(), 1);
        Assertions.assertEquals(packetsMap.get("192.168.0.252").size(), 1);
        Assertions.assertEquals(packetsMap.get("192.168.0.253").size(), 1);
        Assertions.assertEquals(packetsMap.get("10.10.10.10").size(), 1);
    }


    static class MockRouter implements Router {
        final List<Packet> packetsReceived = new ArrayList<>();

        @Override
        public void route(Packet packet) {
            packetsReceived.add(packet);
        }
    }

}
