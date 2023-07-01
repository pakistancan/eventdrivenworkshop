# Event Driven Architecture Workshop

Router Kata was implemented using a MainRouter
Where MainRouter is an EventBus, Routers are subscribers

## Requirement
_______
We have a main router, it is connected to 3 other routers with following networks

* 192.168.x.x
* 172.13.x.x
* 10.10.x.x

Whenever it receives a request starting with IP 192.168.x.x, it forwards it to router in KHI, 172.13.x.x is forwarded to LHR and request to 10.10.x.x is forwarded to ISB
Main router maintains the statistics like how many requests were forwarded to KHI, LHE and ISB.

### Additional Requirement
_________________

We need to add a list of reserved IP’s

These IP’s must not get any hit from outside world 

Any hit to these IP’s should be discarded and logged to an alert log

* 192.168.255.250-192.168.255.255
* 172.13.10.10,172.13.11.11,172.13.13.13
* 10.10.10.9

