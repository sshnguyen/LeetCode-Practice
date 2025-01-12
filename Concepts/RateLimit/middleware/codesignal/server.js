const express = require('express');
const {checkRateLimit} = require("./services/rateLimiter");
const app = express();
app.use(express.json());

app.get('/', (req, res) => {
  res.send('hello world\n');
});

app.post('/take', (req, res) => {
  const {routeTemplate} = req.body;
  try {
    const checkLimitResult = checkRateLimit(routeTemplate);
    if (checkLimitResult.error){
      return res.status(404).json({message : checkLimitResult.error});
    }
    if (checkLimitResult.limitHit){
      return res.status(429).json({message : "Limit for the route has been met, request rejected", remainingTokens : checkLimitResult.remainingTokens});
    }
    return res.status(200).json({message : "The request should be accepted", remainingTokens : checkLimitResult.remainingTokens})
  } catch (error){
    return res.status(500).json({message : "Internal Server Error"});
  }
});

app.post('/', (req, res) => {
  res.status(200).send(`hello ${req.body.name}\n`);
});

module.exports = app;