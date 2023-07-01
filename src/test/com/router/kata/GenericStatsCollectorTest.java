package com.router.kata;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenericStatsCollectorTest {

    static {
        BasicConfigurator.configure();
    }

    @Test
    public void testCollectStatistics(){
        GenericStatsCollector collector = new GenericStatsCollector();
        for (int i = 0; i < 10; i++) {
            collector.collectStatistics(new Router.Packet("192.168.10.10", "172.13.10.10"));
        }
        Assertions.assertNotNull(collector.getStats().get("172.13.10.10"));
        Assertions.assertNotNull(collector.getStats().get("172.13.10.10").get("192.168.10.10"));
        Assertions.assertEquals(collector.getStats().get("172.13.10.10").get("192.168.10.10").hits, 10);


        for (int i = 1; i < 10; i++) {
            collector.collectStatistics(new Router.Packet(String.format("192.167.10.%s", i), "172.13.10.10"));
            Assertions.assertNotNull(collector.getStats().get("172.13.10.10"));
            Assertions.assertNotNull(collector.getStats().get("172.13.10.10").get(String.format("192.167.10.%s",i)));
            Assertions.assertEquals(collector.getStats().get("172.13.10.10").get(String.format("192.167.10.%s",i)).hits, 1);
        }
    }

}
