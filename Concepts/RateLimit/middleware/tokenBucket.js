// middleware/tokenBucket.js

const RATE_LIMIT = 10;  // The maximum capacity of the bucket

let tokenBucket = [];

// Function to refill the bucket
const refillBucket = () => {
  while (tokenBucket.length < RATE_LIMIT) {
    tokenBucket.push(Date.now());
  }
  console.log(`Bucket refilled: ${tokenBucket.length} tokens`);
};

// Middleware for rate limiting
const tokenBucketMiddleware = (req, res, next) => {
  if (tokenBucket.length > 0) {
    const token = tokenBucket.shift();
    console.log(`Token ${token} is consumed`);
    
    res.set('X-RateLimit-Remaining', tokenBucket.length);
    next();
  } else {
    res.status(429).set('X-RateLimit-Remaining', 0).set('Retry-After', 2).json({
      success: false,
      message: 'Too many requests'
    });
  }
};

module.exports = {
  tokenBucketMiddleware,
  refillBucket
};
