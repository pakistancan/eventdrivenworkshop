package com.router.kata;

public interface Router {
    void route(Packet packet);

    class Packet {

        private String sourceIp;
        private String targetIp;
        private Object payload;

        public Packet(String sourceIp, String targetIp) {
            this.sourceIp = sourceIp;
            this.targetIp = targetIp;
        }

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
}
