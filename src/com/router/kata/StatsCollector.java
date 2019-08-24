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
	public void collectStatistics(Packet packet);
}
