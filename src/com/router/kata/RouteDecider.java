/**
 * 
 */
package com.router.kata;

import com.router.kata.Router.Packet;

/**
 * @author muhammadali
 *
 */
public interface RouteDecider {
	public boolean shouldRoute(Packet p);
}