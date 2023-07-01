/**
 *
 */
package com.router.kata.driver;

import com.router.kata.*;
import com.router.kata.Router.Packet;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author muhammadali
 */

public class Driver {
    private static final Logger logger = LoggerFactory.getLogger(Driver.class);

    static {
        BasicConfigurator.configure();
    }

    public static MainRouter createRouter() {
        MainRouter mainRouter = new MainRouter();

        mainRouter.addRouter("192.168", packet -> logger.info("This is KHI"));
        mainRouter.addRouter("172.13", packet -> logger.info("This is LHE"));
        mainRouter.addRouter("10.10", packet -> logger.info("This is ISB"));

        StatsCollector defaultStatsCollector = new GenericStatsCollector();
        mainRouter.addStatsCollector(defaultStatsCollector);


        mainRouter.addRouteDecider(new IPBasedRouteDecider(new HashSet<>(Arrays.asList(
                "192.168.0.250",
                "192.168.0.251",
                "192.168.0.252",
                "192.168.0.253",
                "192.168.0.254",
                "192.168.0.255",
                "10.10.10.10"))));

        return mainRouter;
    }

    public static void main(String[] args) {
        MainRouter mainRouter = createRouter();
        String sourceIp = "172.16.10.11";
        String[] ips = new String[]{"192.168.0.1", "192.168.0.1", "192.168.0.1", "10.10.10.0", "172.13.10.10", "192.168.0.251", "192.168.0.252", "192.168.0.253", "192.168.0.241", "192.168.0.231", "10.10.10.100"};
        for (String ip : ips) {
            Packet packet = new Packet(sourceIp, ip);
            mainRouter.route(packet);
        }
        mainRouter.printStats();
        mainRouter.shutdown();

    }

}
