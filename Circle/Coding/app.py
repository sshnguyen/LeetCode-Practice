import psycopg2
from flask import Flask, request, jsonify
from datetime import datetime
import json

app = Flask(__name__)

# Database connection
def get_db_connection():
    conn = psycopg2.connect(
        host="localhost",
        database="flights_db",
        user="postgres",
        password="password"
    )
    return conn

@app.route('/book-cheapest-flight', methods=['POST'])
def book_cheapest_flight():
    try:
        # Parse request data
        data = request.get_json()
        flight_id = data.get('flight_id')
        requested_seats = data.get('seats', 1)
        
        # Validate required fields
        if not flight_id:
            return jsonify({"error": "flight_id is required"}), 400
        
        # Connect to database
        conn = get_db_connection()
        # Create object to query the database
        cursor = conn.cursor()
        
        # Query for specific flight by ID
        query = """
            SELECT id, airline_code, flight_number, origin, destination, 
                   departure_time, available_seats, price
            FROM flights 
            WHERE id = %s
              AND available_seats >= %s
        """
        
        cursor.execute(query, (flight_id, requested_seats))
        flight = cursor.fetchone()
        
        if not flight:
            return jsonify({"error": "Flight not found or insufficient seats available"}), 404
        
        # Extract flight data
        flight_id, airline, flight_num, orig, dest, dep_time, avail_seats, price = flight
        
        # Update available seats
        update_query = """
            UPDATE flights 
            SET available_seats = available_seats - %s
            WHERE id = %s
        """
        cursor.execute(update_query, (requested_seats, flight_id))
        conn.commit()
        
        # Prepare response
        response = {
            "booking_success": True,
            "flight": {
                "id": flight_id,
                "airline": airline,
                "flight_number": flight_num,
                "route": f"{orig} → {dest}",
                "departure": dep_time.isoformat(),
                "seats_booked": requested_seats,
                "price_per_seat": float(price),
                "total_cost": float(price) * requested_seats,
                "remaining_seats": avail_seats - requested_seats
            }
        }
        
        cursor.close()
        conn.close()
        
        return jsonify(response), 200
        
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/search-flights', methods=['GET'])
def search_flights():
    origin = request.args.get('origin')
    destination = request.args.get('destination')
    date = request.args.get('date')
    seats = request.args.get('seats', 1)  # Default to 1 seat if not provided
    
    conn = get_db_connection()
    cursor = conn.cursor()
    
    # Updated query to include seat availability filter
    query = """
        SELECT airline_code, flight_number, departure_time, available_seats, price
        FROM flights 
        WHERE origin = %s AND destination = %s AND DATE(departure_time) = %s 
        AND available_seats >= %s
        ORDER BY price ASC
    """
    
    query_to_fetch_cheapest = """
        SELECT airline_code, flight_number, departure_time, available_seats, price
        FROM flights 
        WHERE origin = %s AND destination = %s AND DATE(departure_time) = %s 
        AND available_seats >= %s
        ORDER BY price ASC
        LIMIT 1
    """
    
    cursor.execute(query, (origin, destination, date, seats))
    flights = cursor.fetchall()
    
    result = []
    for flight in flights:
        result.append({
            "airline": flight[0],
            "flight_number": flight[1],
            "departure": flight[2].isoformat(),
            "available_seats": flight[3],
            "price": float(flight[4])
        })
    
    cursor.close()
    conn.close()
    
    return jsonify({"flights": result})

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=8080)