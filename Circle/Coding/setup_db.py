import psycopg2
from datetime import datetime

def create_database():
    # Connect to default postgres database first
    conn = psycopg2.connect(
        host="localhost",
        database="postgres",
        user="postgres",
        password="password"
    )
    conn.autocommit = True
    cursor = conn.cursor()
    
    # Create flights_db database
    try:
        cursor.execute("CREATE DATABASE flights_db")
        print("Database created successfully")
    except psycopg2.errors.DuplicateDatabase:
        print("Database already exists")
    
    cursor.close()
    conn.close()

def create_tables():
    conn = psycopg2.connect(
        host="localhost",
        database="flights_db",
        user="postgres",
        password="password"
    )
    cursor = conn.cursor()
    
    # Create flights table
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS flights (
            id SERIAL PRIMARY KEY,
            airline_code VARCHAR(3),
            flight_number VARCHAR(10),
            origin VARCHAR(3),
            destination VARCHAR(3),
            departure_date DATE,
            departure_time TIMESTAMP,
            available_seats INTEGER,
            price DECIMAL(10,2)
        )
    """)
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Tables created successfully")

def insert_mock_data():
    conn = psycopg2.connect(
        host="localhost",
        database="flights_db",
        user="postgres",
        password="password"
    )
    cursor = conn.cursor()
    
    # Clear existing data
    cursor.execute("DELETE FROM flights")
    
    # Insert mock flights
    mock_flights = [
        ('UA', 'UA123', 'LAX', 'JFK', '2024-12-15', '2024-12-15 08:00:00', 50, 299.99),
        ('AA', 'AA456', 'LAX', 'JFK', '2024-12-15', '2024-12-15 10:30:00', 30, 289.99),
        ('DL', 'DL789', 'LAX', 'JFK', '2024-12-15', '2024-12-15 14:00:00', 25, 279.99),
        ('UA', 'UA234', 'JFK', 'LAX', '2024-12-15', '2024-12-15 16:00:00', 40, 319.99),
        ('AA', 'AA567', 'JFK', 'LAX', '2024-12-15', '2024-12-15 18:30:00', 35, 309.99),
    ]
    
    for flight in mock_flights:
        cursor.execute("""
            INSERT INTO flights (airline_code, flight_number, origin, destination, 
                               departure_date, departure_time, available_seats, price)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
        """, flight)
    
    conn.commit()
    cursor.close()
    conn.close()
    print("Mock data inserted successfully")

if __name__ == '__main__':
    create_database()
    create_tables()
    insert_mock_data()