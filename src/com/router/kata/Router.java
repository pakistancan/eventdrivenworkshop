package com.router.kata;

public interface Router {
	public static class Packet {

		public Packet(String ip) {
			this.packetIp = ip;
		}

		private String packetIp;

		public String getPacketIp() {
			return packetIp;
		}

		public void setPacketIp(String packetIp) {
			this.packetIp = packetIp;
		}

	}

	public void route(Packet packet);
}
