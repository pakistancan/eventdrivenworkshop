/**
 * 
 */
package com.router.kata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author muhammadali
 *
 */

public class MainRouter implements Router {

	public List<RouteDecider> routeDeciders = new ArrayList<>();

	public List<StatsCollector> statsCollectors = new ArrayList<>();

	public Map<String, List<Router>> routers = new HashMap<>();

	public void addRouter(String prefix, Router router) {
		if (routers.containsKey(prefix)) {
			routers.put(prefix, new ArrayList<>());
		}
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

		String[] octates = packet.getPacketIp().split("\\.");
		String prefix = octates[0] + "." + octates[1];
		boolean shouldRoute = routeDeciders.parallelStream().allMatch(x -> x.shouldRoute(packet));
		if (shouldRoute) {
			List<Router> routerList = routers.get(prefix);
			if (null != routerList && !routerList.isEmpty()) {
				for (Router router : routerList) {
					if (null != router) {
						router.route(packet);
					}
				}
			}

		}

	}
}
