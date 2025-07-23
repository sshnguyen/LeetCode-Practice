# Part 1: Basic Key-Value Store
# Implement a basic key-value store with set and get operations.

# Example:
# KV kv = new KV();
# kv.set("foo", "bar");
# kv.get("foo");  // Returns "bar"

# Part 2: Versioned Key-Value Store
# Extend the basic store to support versioning. The set operation returns a timestamp, and get can optionally take a timestamp to retrieve historical values.

import time


class KeyValueStore:
  def __init__(self):
    self.store = {}
  def set(self, key, value):
    self.store[key] = value
  def get(self, key):
    if not key in self.store:
      return ""
    return self.store[key]
  
class KeyValueStore2:
    def __init__(self):
        # key -> list of (timestamp, value)
        self.store = {}

    def set(self, key, value):
        timestamp = int(time.time())
        if key not in self.store:
            self.store[key] = []
        self.store[key].append((timestamp, value))
        print(timestamp)
        return timestamp

    def get(self, key, timestamp=None):
      if key not in self.store:
          return None

      if timestamp is None:
          # return latest value
          return self.store[key][-1][1]

      res = ""
      if key not in self.store:
          return res
      values = self.store[key]
      l = 0
      r = len(values) - 1
      while l <= r:
          mid = l + ((r - l) // 2)
          if values[mid][0] <= timestamp:
              res = values[mid][1]
              l = mid + 1
          else:
              r = mid - 1
      return res
  
if __name__ == "__main__":
  # Create an instance of Solution
  store = KeyValueStore()
  store.set("foo", "bar")
  print(store.get("foo")) #bar

  store = KeyValueStore2()
  timestamp = store.set("foo", "bar")
  time.sleep(2)
  print(timestamp)
  print(store.get("foo", timestamp)) #bar
  store.set("foo", "bar2")

  # Get latest value
  print(store.get("foo"))  # Returns "bar2"