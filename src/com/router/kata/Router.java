package com.router.kata;

public interface Router {
	public static class Packet {

		public Packet(String ip) {
			this.targetIp = ip;
		}

		private String sourceIp;
		private String targetIp;
		private Object payload;

		public Object getPayload() {
			return payload;
		}

		public void setPayload(Object payload) {
			this.payload = payload;
		}

		public String getSourceIp() {
			return sourceIp;
		}

		public void setSourceIp(String sourceIp) {
			this.sourceIp = sourceIp;
		}

		public String getTargetIp() {
			return targetIp;
		}

		public void setTargetIp(String targetIp) {
			this.targetIp = targetIp;
		}

	}

	public void route(Packet packet);
}
