const chai = require('chai');
const chaiHttp = require('chai-http');
const app = require('../server');
const {loadConfig} = require("../services/rateLimiter");

chai.use(chaiHttp);

const { request, expect } = chai;

before(() => {
  loadConfig({
  "rateLimitsPerEndpoint": [
    { "endpoint": "GET /user/:id", "burst": 10, "sustained": 120 }
  ]
  });
});

//Utility function to wait for some miliseconds
function delay(time){
  return new Promise(resolve => setTimeout(resolve, time));
}

describe('Testing /take end point', () => {
  it('should accept "GET /user/:id" route', async () => {
    const requestBody = {
      routeTemplate : "GET /user/:id"
    };
    const response = await request(app).post('/take').send(requestBody);
    expect(response.status).to.equal(200);
    expect(response.body.remainingTokens).to.equal(9);
  });
  
  it('should reject "GET /user/:id" route when limit is hit', async () => {
    const requestBody = {
      routeTemplate : "GET /user/:id"
    };
    // make repeated request to hit the limit of "GET /user/:id" route
    for (let i = 0; i < 10; i++){
      await request(app).post('/take').send(requestBody);
    }
    
    const response = await request(app).post('/take').send(requestBody);
    
    expect(response.status).to.equal(429);
    expect(response.body.remainingTokens).to.equal(0);
  });
  
  it('should accept "GET /user/:id" again after limit is hit, and some token is refilled', async () => {
    const requestBody = {
      routeTemplate : "GET /user/:id"
    };
    // make repeated request to hit the limit of "GET /user/:id" route
    for (let i = 0; i < 10; i++){
      await request(app).post('/take').send(requestBody);
    }
    
    //wait enough time for 2 tokens to be refilled
    //60000 ms / 120 * 2
    await delay(1000)
    
    const response = await request(app).post('/take').send(requestBody);
    
    expect(response.status).to.equal(200);
    expect(response.body.remainingTokens).to.equal(1);
  });
  
  it('should return 404 status for routes not in config', async () => {
    const requestBody = {
      routeTemplate : "GET /random"
    };

    const response = await request(app).post('/take').send(requestBody);
    expect(response.status).to.equal(404);
  });
  
  
  
});