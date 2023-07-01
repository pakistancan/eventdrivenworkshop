/**
 *
 */
package com.router.kata;

import com.router.kata.Router.Packet;

/**
 * @author muhammadali
 *
 */
public interface StatsCollector {
    void collectStatistics(Packet packet);
    void printStats();
}
