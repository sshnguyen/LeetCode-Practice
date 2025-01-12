const chai = require('chai');
const chaiHttp = require('chai-http');
const app = require('../server');

chai.use(chaiHttp);

const { request, expect } = chai;

describe('GET /', () => {
  it('responds with hello world', async () => {
    const response = await request(app).get('/');

    expect(response.status).to.equal(200);
    expect(response.text).to.contain('hello world');
  });
});
