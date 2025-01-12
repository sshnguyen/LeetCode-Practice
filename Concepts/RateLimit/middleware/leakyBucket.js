// middleware/leakyBucket.js

const BUCKET_SIZE = 10;  // The maximum capacity of the bucket
const REFILL_RATE = 1;   // Tokens added per second

// In-memory store for buckets
const buckets = {};

// Leaky bucket rate limiting middleware
const leakyBucketMiddleware = (req, res, next) => {
  const ip = req.ip;

  // Initialize the bucket for the IP if it doesn't exist
  if (!buckets[ip]) {
    buckets[ip] = {
      tokens: BUCKET_SIZE,
      lastRefill: Date.now()
    };
  }

  const currentTime = Date.now();
  const elapsedTime = (currentTime - buckets[ip].lastRefill) / 1000;  // Convert to seconds
  buckets[ip].lastRefill = currentTime;

  // Refill the bucket based on elapsed time
  buckets[ip].tokens += elapsedTime * REFILL_RATE;
  if (buckets[ip].tokens > BUCKET_SIZE) {
    buckets[ip].tokens = BUCKET_SIZE;
  }

  // Check if there are enough tokens to proceed
  if (buckets[ip].tokens >= 1) {
    buckets[ip].tokens -= 1;
    next();  // Allow the request to proceed
  } else {
    res.status(429).send('Too Many Requests');  // Rate limit exceeded
  }
};

module.exports = leakyBucketMiddleware;
