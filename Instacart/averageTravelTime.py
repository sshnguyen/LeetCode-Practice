# Given shoppers picking up orders from one location and delivering to another location, calculate the average travel time between locations.

# public void pickup(int timestamp, String pickUpLocation, int shopperId)
# public void deliver(int timestamp, String, DeliverLocation, int shopperId)
# public double calculateAverage(String locationB, String locationB)

# Example:
# Test case:
# pickup(0, "A", 1);    // shopper1 picks up at location A at time 0
# deliver(5, "B", 1);   // shopper1 delivers at location B at time 5
# pickup(0, "A", 2);    // shopper2 picks up at location A at time 0
# deliver(6, "B", 2);   // shopper2 delivers at location B at time 6
# calculateAverage("A", "B") -> 5.5

#✅✅✅✅
  #1. Can there be invalid inputs like deliver(5, "B", 1) when shopper 1 hasn't pick up any package
  #1. Can a shopper pick up more than 1 deliveries?
  #2. What happens when calculate average is called before a delivery for that route is complete?
  #   can we assume that the still in progress delivery won't be included in the calculation.
  #3.What should calculateAverage return if the route hasn't has any complete delivery before?

from collections import *
from typing import *

class Solution:
    deliveriesInProgress = {} # shopperId -> (timestamp, pickupLocation)
    routeDeliveryTime = defaultdict(lambda: (0, 0)) # map (pickupLocation, deliverlocation) -> (totalTime, numTrips)

    def pickup(self, timestamp : int, pickUpLocation: str, shopperId : int) -> bool:
      # check if shopper already has pickup, assuming shopper can only do 1 delivery at a time
      if shopperId in self.deliveriesInProgress:
        return False
      
      self.deliveriesInProgress[shopperId] = (timestamp, pickUpLocation)
       
      return True

    def deliver(self, timestamp: int, deliverLocation: str, shopperId: int) -> bool:
      if shopperId not in self.deliveriesInProgress:
        return False
      
      pickupTimestamp, pickUpLocation = self.deliveriesInProgress.pop(shopperId)
      timeTaken = timestamp - pickupTimestamp
      routeTotalTime, routeNumTrips = self.routeDeliveryTime[(pickUpLocation, deliverLocation)]
      self.routeDeliveryTime[(pickUpLocation, deliverLocation)] = (routeTotalTime + timeTaken, routeNumTrips + 1)

      return True

    def calculateAverage(self, pickUpLocation: str, deliverLocation: str) -> float:
        if (pickUpLocation, deliverLocation) not in self.routeDeliveryTime:
           return 0.0
        routeTotalTime, routeNumTrips = self.routeDeliveryTime[(pickUpLocation, deliverLocation)]
        return routeTotalTime / float(routeNumTrips)

    
if __name__ == "__main__":
  # Create an instance of Solution
  solution = Solution()

  solution.pickup(0, "A", 1);    # shopper1 picks up at location A at time 0
  solution.deliver(5, "B", 1);   # shopper1 delivers at location B at time 5
  solution.pickup(0, "A", 2);    # shopper2 picks up at location A at time 0
  solution.deliver(6, "B", 2);   # shopper2 delivers at location B at time 6
  res = solution.calculateAverage("A", "B") # 5.5

  # Optional: assert expected output for testing
  expected = 5.5
  assert res == expected, "Test failed!"
  print("Test passed!")