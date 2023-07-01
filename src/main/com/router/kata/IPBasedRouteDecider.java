package com.router.kata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IPBasedRouteDecider implements RouteDecider {
    private static final Logger logger = LoggerFactory.getLogger(IPBasedRouteDecider.class);

    private final Set<String> reservedIps;
    private final List<Router.Packet> blockedPackets = new ArrayList<>();

    public List<Router.Packet> getBlockedPackets() {
        return blockedPackets;
    }

    public IPBasedRouteDecider(Set<String> blockedIPs) {
        if (null != blockedIPs) {
            this.reservedIps = blockedIPs;
        } else {
            this.reservedIps = new HashSet<>();
        }
    }

    @Override
    public boolean shouldRoute(Router.Packet p) {
        if (reservedIps.contains(p.getTargetIp())) {
            logger.warn("Request received to restricted IP: {} from IP: {}", p.getTargetIp(), p.getSourceIp());
            blockedPackets.add(p);
            return false;
        }
        return true;
    }

}