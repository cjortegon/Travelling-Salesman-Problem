# Travelling Salesman Problem

## Welcome

The Travelling Salesman Problem has no optimal solution, but here we proposed a solution that takes advantage from the Google Maps Api to find one of the best routes, which is not necessary the best.
The algorithm generates a connected graph and test some of the posible ways to travel arround.

## Traffic consideration

This algorithm was developed to work inside real life cities, so it takes in count the traffic.
It tries to make the travel in different schedules, which adds a grade of difficulty to the problem.
Our solution finds some of the best solutions without traffic and after it test the different solutions between a given space of time, the 'schedule'.

## How algorithm works

- Using the distance with no traffic between the nodes, a connected graph is built.
- To allow circuits around the graph the 'Divided groups' and 'Bow tie' problems are solved creating additional connections.
- Then all the posible ways to travel around the graph are generated.
- Finally routes are tested at the given starting time to discover how much time it takes to pass thru all.

## How to use it?

First you need to get a Google Maps Api Authentication Key:
https://developers.google.com/maps/documentation/javascript/get-api-key

Check the included test cases.
For more information in how to use the code, go to this repository wiki.
