/**
 * 
 */
package com.router.kata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author muhammadali
 *
 */

public class MainRouter implements Router {
	interface StatsCollector {
		public void collectStatistics(Packet packet);
	}

	interface RouteDecider {
		public boolean shouldRoute(Packet p);
	}

	Set<String> reservedIps = new HashSet<>();

	{
		reservedIps.add("192.168.0.250");
		reservedIps.add("192.168.0.251");
		reservedIps.add("192.168.0.252");
		reservedIps.add("192.168.0.253");
		reservedIps.add("192.168.0.254");
		reservedIps.add("192.168.0.255");
		reservedIps.add("10.10.10.10");
	}

	StatsCollector defaultStatsCollector = new StatsCollector() {

		@Override
		public void collectStatistics(Packet packet) {
			System.out.println("Packet Arrived with IP :: " + packet.getPacketIp());
		}
	};
	public RouteDecider routeDecider = new RouteDecider() {

		@Override
		public boolean shouldRoute(Packet p) {
			if (reservedIps.contains(p.getPacketIp())) {
				System.out.println("IP is reserved returning " + p.getPacketIp());
				return false;
			}
			return true;
		}
	};

	public List<StatsCollector> statsCollectors = Arrays.asList(defaultStatsCollector);

	public Map<String, Router> routers = new HashMap<>();
	{
		routers.put("192.168", new Router() {

			@Override
			public void route(Packet packet) {
				System.out.println("This is KHI");

			}
		});
		routers.put("172.13", new Router() {

			@Override
			public void route(Packet packet) {
				System.out.println("This is LHR");

			}
		});
		routers.put("10.10", new Router() {

			@Override
			public void route(Packet packet) {
				System.out.println("This is ISB");

			}
		});
	}

	@Override
	public void route(Packet packet) {

		for (StatsCollector statsCollector : statsCollectors) {
			statsCollector.collectStatistics(packet);
		}

		String[] octates = packet.getPacketIp().split("\\.");
		String prefix = octates[0] + "." + octates[1];
		if (routeDecider.shouldRoute(packet)) {

			Router router = routers.get(prefix);
			if (null != router) {
				router.route(packet);
			}

		}

	}

	public static void main(String[] args) {
		MainRouter mainRouter = new MainRouter();
		String[] ips = new String[] { "192.168.0.1", "10.10.10.0", "172.13.10.10", "192.168.0.251" };
		for (String ip : ips) {
			Packet packet = new Packet(ip);
			mainRouter.route(packet);
		}
	}
}
