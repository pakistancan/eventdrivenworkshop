/**
 *
 */
package com.router.kata;

import com.router.kata.Router.Packet;

/**
 * @author muhammadali
 */
public interface RouteDecider {
    boolean shouldRoute(Packet p);
}