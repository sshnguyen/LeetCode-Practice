from collections import defaultdict
import heapq
import time

# ----- ROUND 1: Product Sales and Profit Calculation -----
def calculate_product_profits(transactions, product_costs):
    product_summary = {}  # product -> {units_sold, revenue, cost, profit}

    for tx in transactions:
        product = tx['product']
        quantity = tx['quantity']
        price = tx['price']
        cost = product_costs.get(product, 0)

        if product not in product_summary:
            product_summary[product] = {
                'units_sold': 0,
                'revenue': 0.0,
                'cost': 0.0,
                'profit': 0.0
            }

        product_summary[product]['units_sold'] += quantity
        product_summary[product]['revenue'] += quantity * price
        product_summary[product]['cost'] += quantity * cost
        product_summary[product]['profit'] += quantity * (price - cost)

    # Sort by profit in descending order
    sorted_products = sorted(product_summary.items(), key=lambda x: x[1]['profit'], reverse=True)
    return sorted_products



# ----- ROUND 2: Delivery Wait Time & Shopper Count Optimization -----
def minimum_shoppers(orders, delivery_duration, max_avg_wait):
    if not orders:
        return 0

    # Sort orders by request time
    orders.sort(key=lambda x: x["time"])
    
    # Try increasing number of shoppers until condition met
    for shoppers in range(1, len(orders) + 1):
        # Priority queue for next available shopper time
        pq = [0] * shoppers
        total_wait = 0

        for order in orders:
            request_time = order["time"]
            # Pop the soonest available shopper
            available_at = heapq.heappop(pq)
            start_time = max(request_time, available_at)
            wait_time = start_time - request_time
            total_wait += wait_time
            # Schedule shopper again after delivery
            heapq.heappush(pq, start_time + delivery_duration)

        avg_wait = total_wait / len(orders)
        if avg_wait <= max_avg_wait:
            return shoppers

    return len(orders)  # worst case: one shopper per order


# ----- MAIN FUNCTION TO TEST BOTH -----
def main():
    # Test Round 1
    sales = [
        {"product": "apple", "units": 10, "price_per_unit": 2.0},
        {"product": "banana", "units": 5, "price_per_unit": 1.0},
        {"product": "apple", "units": 3, "price_per_unit": 2.0},
    ]
    costs = {"apple": 1.0, "banana": 0.5}

    print("--- Product Profits ---")
    profits = calculate_product_profits(sales, costs)
    for p in profits:
        print(p)

    # Test Round 2
    orders = [
        {"order_id": 1, "time": 0},
        {"order_id": 2, "time": 1},
        {"order_id": 3, "time": 3},
        {"order_id": 4, "time": 10},
    ]
    delivery_duration = 5
    max_avg_wait = 5

    print("\n--- Minimum Shoppers Needed ---")
    print(minimum_shoppers(orders, delivery_duration, max_avg_wait))


if __name__ == "__main__":
    main()