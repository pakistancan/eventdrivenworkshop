/**
 *
 */
package com.router.kata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * @author muhammadali
 */

public class MainRouter implements Router {

    private final static Logger logger = LoggerFactory.getLogger(MainRouter.class);

    private final Queue<Packet> queue = new ConcurrentLinkedQueue<>();
    private final Consumer consumer = new Consumer(queue);
    public List<RouteDecider> routeDeciders = new ArrayList<>();
    public List<StatsCollector> statsCollectors = new ArrayList<>();
    public ConcurrentMap<String, List<Router>> routers = new ConcurrentHashMap<>();

    public MainRouter() {

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }

    public void addRouter(String prefix, Router router) {
        if (!routers.containsKey(prefix)) {
            routers.put(prefix, new ArrayList<>());
        }
        routers.get(prefix).add(router);
    }


    public void printStats() {

        for (StatsCollector collector : statsCollectors) {
            collector.printStats();
        }
    }

    public void shutdown() {
        consumer.close();
    }

    public void addStatsCollector(StatsCollector collector) {
        statsCollectors.add(collector);
    }

    public void addRouteDecider(RouteDecider routeDecider) {
        routeDeciders.add(routeDecider);
    }

    @Override
    public void route(Packet packet) {

        for (StatsCollector statsCollector : statsCollectors) {
            statsCollector.collectStatistics(packet);
        }

        boolean shouldRoute = routeDeciders.parallelStream().allMatch(x -> x.shouldRoute(packet));

        if (shouldRoute) {
            queue.add(packet);
        }

    }

    private class Consumer implements Runnable {
        private final Queue<Packet> consumerQueue;

        private Boolean closed = false;

        public Consumer(Queue<Packet> queue) {
            this.consumerQueue = queue;
        }

        public void close() {
            this.closed = true;
        }

        @Override
        public void run() {
            try {

                do {
                    Packet packet = consumerQueue.poll();//1, TimeUnit.MILLISECONDS);
                    if (null != packet) {
                        String[] octets = packet.getTargetIp().split("\\.");
                        String prefix = String.format("%s.%s", octets[0], octets[1]);
                        List<Router> routerList = routers.get(prefix);
                        if (null != routerList && !routerList.isEmpty()) {
                            for (Router router : routerList) {
                                if (null != router) {
                                    router.route(packet);
                                }
                            }
                        }
                    }
                } while (!closed || !consumerQueue.isEmpty());
            } catch (Exception e) {
                logger.error("Exception", e);
            }

        }
    }
}
