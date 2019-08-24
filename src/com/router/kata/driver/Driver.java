/**
 * 
 */
package com.router.kata.driver;

import java.util.HashSet;
import java.util.Set;

import com.router.kata.MainRouter;
import com.router.kata.RouteDecider;
import com.router.kata.Router;
import com.router.kata.Router.Packet;
import com.router.kata.StatsCollector;

/**
 * @author muhammadali
 *
 */
public class Driver {

	public static MainRouter createRouter() {

		MainRouter mainRouter = new MainRouter();

		mainRouter.addRouter("192.168", new Router() {

			@Override
			public void route(Packet packet) {
				System.out.println("This is KHI");

			}
		});
		mainRouter.addRouter("172.13", new Router() {

			@Override
			public void route(Packet packet) {
				System.out.println("This is LHR");

			}
		});
		mainRouter.addRouter("10.10", new Router() {

			@Override
			public void route(Packet packet) {
				System.out.println("This is ISB");

			}
		});

		StatsCollector defaultStatsCollector = new StatsCollector() {

			@Override
			public void collectStatistics(Packet packet) {
				System.out.println("Packet Arrived with IP :: " + packet.getPacketIp());
			}
		};
		mainRouter.addStatsCollector(defaultStatsCollector);

		mainRouter.addRouteDecider(new RouteDecider() {
			private Set<String> reservedIps = new HashSet<>();
			{
				reservedIps.add("192.168.0.250");
				reservedIps.add("192.168.0.251");
				reservedIps.add("192.168.0.252");
				reservedIps.add("192.168.0.253");
				reservedIps.add("192.168.0.254");
				reservedIps.add("192.168.0.255");
				reservedIps.add("10.10.10.10");
			}

			@Override
			public boolean shouldRoute(Packet p) {
				if (reservedIps.contains(p.getPacketIp())) {
					System.out.println("IP is reserved returning " + p.getPacketIp());
					return false;
				}
				return true;
			}
		});

		return mainRouter;
	}

	public static void main(String[] args) {
		MainRouter mainRouter = createRouter();

		String[] ips = new String[] { "192.168.0.1", "10.10.10.0", "172.13.10.10", "192.168.0.251" };
		for (String ip : ips) {
			Packet packet = new Packet(ip);
			mainRouter.route(packet);
		}
	}

}
