#!/bin/bash

echo "=== Testing search flights with seat requirement ==="
curl "http://localhost:8080/search-flights?origin=LAX&destination=JFK&date=2024-12-15&seats=2"

echo -e "\n\n=== Testing book flight using flight ID ==="
# Note: Replace flight_id with actual ID from search results above
# For this example, assuming the cheapest flight has ID 3 (DL789 at $279.99)
curl -X POST http://localhost:8080/book-cheapest-flight \
  -H "Content-Type: application/json" \
  -d '{"flight_id": 3, "seats": 2}'

echo -e "\n\n=== Testing search again to see updated seats ==="
curl "http://localhost:8080/search-flights?origin=LAX&destination=JFK&date=2024-12-15&seats=2"

echo -e "\n\n=== Testing with insufficient seats (should return fewer flights) ==="
curl "http://localhost:8080/search-flights?origin=LAX&destination=JFK&date=2024-12-15&seats=50"

echo -e "\n\n=== Testing book with invalid flight ID ==="
curl -X POST http://localhost:8080/book-cheapest-flight \
  -H "Content-Type: application/json" \
  -d '{"flight_id": 999, "seats": 1}'

echo -e "\n\n=== Testing book without flight_id (should return error) ==="
curl -X POST http://localhost:8080/book-cheapest-flight \
  -H "Content-Type: application/json" \
  -d '{"seats": 1}'