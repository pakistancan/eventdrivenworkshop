package com.router.kata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class GenericStatsCollector implements StatsCollector {
    private static final Logger logger = LoggerFactory.getLogger(GenericStatsCollector.class);
    private final Map<String, Map<String, Stats>> stats = new HashMap<>();

    public Map<String, Map<String, Stats>> getStats() {
        return stats;
    }

    @Override
    public void collectStatistics(Router.Packet packet) {

        logger.info("Request received with IP: {}", packet.getTargetIp());

        Map<String, Stats> routerHits = stats.computeIfAbsent(packet.getTargetIp(), k -> new HashMap<>());
        Stats stat = routerHits.computeIfAbsent(packet.getSourceIp(), k -> new Stats(0));
        stat.increment();
    }

    public void printStats() {
        for (String tip :
                this.stats.keySet()) {
            Map<String, Stats> statInfo = this.stats.get(tip);
            for (String sip : statInfo.keySet()) {
                logger.info("Router {} was hit by IP {} for {} times",tip,sip,statInfo.get(sip).getHits());
            }
        }
    }

    static class Stats {
        int hits;

        public Stats(int hits) {
            this.hits = hits;
        }

        public void increment() {
            this.hits++;
        }

        public int getHits() {
            return hits;
        }

    }

}
