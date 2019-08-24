/**
 * 
 */
package com.router.kata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author muhammadali
 *
 */

public class MainRouter implements Router {
	private BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();
	private Consumer consumer = new Consumer(queue);
	private Thread consumerThread;

	public MainRouter() {

		consumerThread = new Thread(consumer);
		consumerThread.start();
	}

	public List<RouteDecider> routeDeciders = new ArrayList<>();

	public List<StatsCollector> statsCollectors = new ArrayList<>();

	public Map<String, List<Router>> routers = new HashMap<>();

	public void addRouter(String prefix, Router router) {
		if (!routers.containsKey(prefix)) {
			routers.put(prefix, new ArrayList<>());
		}
		routers.get(prefix).add(router);
	}

	public void addStatsCollector(StatsCollector collector) {
		statsCollectors.add(collector);
	}

	public void addRouteDecider(RouteDecider routeDecider) {
		routeDeciders.add(routeDecider);
	}

	private class Consumer implements Runnable {
		private BlockingQueue<Packet> consumerQueue;

		public Consumer(BlockingQueue<Packet> queue) {
			this.consumerQueue = queue;
		}

		@Override
		public void run() {
			try {

				while (true) {
					Packet packet = consumerQueue.take();
					String[] octates = packet.getTargetIp().split("\\.");
					String prefix = octates[0] + "." + octates[1];
					List<Router> routerList = routers.get(prefix);
					if (null != routerList && !routerList.isEmpty()) {
						for (Router router : routerList) {
							if (null != router) {
								router.route(packet);
							}
						}
					}

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void route(Packet packet) {

		for (StatsCollector statsCollector : statsCollectors) {
			statsCollector.collectStatistics(packet);
		}

		boolean shouldRoute = routeDeciders.parallelStream().allMatch(x -> x.shouldRoute(packet));

		if (shouldRoute) {
			try {
				queue.put(packet);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
