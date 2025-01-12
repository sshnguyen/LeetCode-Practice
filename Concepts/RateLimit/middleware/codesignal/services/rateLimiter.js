// In-memory storage for rate limits
let rateLimits = {};

/**
 * Loads rate limit configurations into memory.
 * @param {Object} rateLimitConfig - Configuration object from config.json 
 */
const loadConfig = (rateLimitConfig) => {
    rateLimitConfig.rateLimitsPerEndpoint.forEach(config => {
        // Initialize rate limit data for each endpoint
        rateLimits[config.endpoint] = {
            burst: config.burst,
            sustained : config.sustained,
            tokens: config.burst,
            lastRefilled: Date.now()
        }
    })
};

/**
 * Refills tokens for a specific route template based on elapsed time since last refill.
 * @param {string} routeTemplate - The route template to refill tokens for
 */
const refillTokens = (routeTemplate) => {
    const currentTime = Date.now();
    const timeElapsedSinceLastRefilled = currentTime - rateLimits[routeTemplate].lastRefilled;
    const tokenRefillRate = rateLimits[routeTemplate].sustained / 60000;
    const refillTokens = Math.floor(timeElapsedSinceLastRefilled * tokenRefillRate);
    
    if (refillTokens > 0){
        //refill tokens only up to the burst limit
        rateLimits[routeTemplate].tokens = Math.min (rateLimits [routeTemplate].burst, rateLimits[routeTemplate].tokens + refillTokens);
        // update lastRefilled date if token were refilled and not at max limit
        if (rateLimits[routeTemplate].tokens != rateLimits[routeTemplate].burst){
            //calculate exact time elapse equivalent of tokens Refilled
            rateLimits[routeTemplate].lastRefilled += tokenRefillRate * refillTokens;
        } else {
            //reset last refilled time to now if tokens are to max limit.
            rateLimits[routeTemplate].lastRefilled = currentTime;
        }
    }
    
};

/**
 * Checks if a request can be accepted based on the rate limit for a specific route template.
 * @param {string} routeTemplate - The route template to check rate limit for
 * @returns {Object} Object indicating if limit is hit and remaining tokens
 */
const checkRateLimit = (routeTemplate) => {
    if (!rateLimits[routeTemplate]){
        return {
            error: "Route template not found"
        };
    }
    //refill token as soon as possible, before checking limit
    refillTokens(routeTemplate);

    if (rateLimits[routeTemplate].tokens > 0){
        // tokens available, allow request and decrease token count
        rateLimits[routeTemplate].tokens -=1;
        return {
            limitHit : false,
            remainingTokens : rateLimits[routeTemplate].tokens
        };
    } else {
        // no token, reject request
        return {
            limitHit : true,
            remainingTokens : 0
        };
    }
};

module.exports = {loadConfig, checkRateLimit};