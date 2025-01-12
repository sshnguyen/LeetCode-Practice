const app = require("./server");
const rateLimitConfig = require('./config.json');
const {loadConfig} = require("./services/rateLimiter");
const PORT = process.env.NODE_PORT || 3000;

app.listen(PORT, () => {
  loadConfig(rateLimitConfig);
  console.log(`api listening on port ${PORT}...`);
});
