+--------------------+
|  ParkingLot       |
+--------------------+
| - List<Spot> spots|
| - List<EntryGate> |
| - List<ExitGate>  |
+--------------------+
| + parkVehicle()   |
| + leaveParking()  |
| + findSpot()      |
+--------------------+
         |
         v
+------------------+
| ParkingSpot      |
+------------------+
| - size          |
| - isOccupied    |
| - vehicleType   |
+------------------+
| + assignVehicle()|
| + removeVehicle()|
+------------------+



+----------------+
| Vehicle       |
+----------------+
| - licensePlate |
| - size         |
| - type         |
+----------------+
| + park()       |
| + unpark()     |
+----------------+
      ^
      |
+------------+   +----------+   +------------+
| Car        |   | Bike     |   | Truck      |
+------------+   +----------+   +------------+


+------------------+
| Reservation      |
+------------------+
| - spotId        |
| - vehicle       |
| - startTime     |
| - endTime       |
+------------------+
| + bookSpot()    |
| + cancel()      |
+------------------+

+------------------+
| Payment         |
+------------------+
| - amount        |
| - method        |
| - ticketId      |
+------------------+
| + process()     |
+------------------+

3. How the System Works
1️⃣ Vehicle Entry:

Car arrives → System checks for an available spot.
If a reservation exists, assigns a reserved spot.
Issues a ticket with timestamp.
2️⃣ Parking Spot Assignment:

System allocates an empty spot matching the vehicle’s size.
Updates isOccupied = true for that spot.
3️⃣ Exit & Payment:

User scans ticket → System calculates parking fee.
Payment is processed → Spot is marked as free.
4️⃣ Reservation Handling:

Users can book a specific spot before arrival.
If they don’t arrive, the spot is freed after a timeout.
