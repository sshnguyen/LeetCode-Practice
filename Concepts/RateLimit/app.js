const express = require('express');
const { refillBucket, tokenBucketMiddleware } = require('./middleware/tokenBucket');
const leakyBucketMiddleware = require('./middleware/leakyBucket');
const axios = require('axios');
const app = express();

// // Apply the leaky bucket middleware to /api endpoints
// app.use('/api', leakyBucketMiddleware);

// // Example /api endpoint
// app.get('/api/data', (req, res) => {
//   res.send('This is protected API data.');
// });

// // Start the server
// const PORT = 3000;
// app.listen(PORT, () => {
//   console.log(`Server is running on port ${PORT}`);
// });


// // Create a cron job to periodically refill the bucket every 2 seconds
// const job = new CronJob('*/2 * * * * *', refillBucket);


// const axios = require('axios');

async function getFiscalYearStatements(companyId) {
  try {
    // Replace 'YOUR_API_URL' with the actual URL of the API endpoint
    const apiUrl = `https://api-v2.intrinio.com/companies/${companyId}/fundamentals?api_key=OjkzMmJmY2ViMDFhODU0ODA5YmMzZjBkNjRjOTczMzEw`;

    const response = await axios.get(apiUrl);
    const fundamentals = response.data.fundamentals;

    // Arrays to store the statement IDs for the last 4 fiscal years and 5 quarters
    let incomeStatementsAnnual = Array(4).fill('');
    let cashFlowStatementsAnnual = Array(4).fill('');
    let balanceSheetStatementsAnnual = Array(4).fill('');

    let incomeStatementsQuarters = Array(5).fill('');
    let cashFlowStatementsQuarters = Array(5).fill('');
    let balanceSheetStatementsQuarters = Array(5).fill('');

    // Get the current year to determine the last four fiscal years
    const currentYear = new Date().getFullYear();

    // Filter and collect the statement IDs for the last four fiscal years
    const filteredFundamentalsAnnual = fundamentals
      .filter(statement => statement.fiscal_period === "FY" && statement.fiscal_year >= currentYear - 4)
      .sort((a, b) => b.fiscal_year - a.fiscal_year);

    filteredFundamentalsAnnual.forEach(statement => {
      const yearDiff = currentYear - statement.fiscal_year;
      if (yearDiff < 4) {
        if (statement.statement_code === "income_statement") {
          incomeStatementsAnnual[yearDiff] = statement.id;
        } else if (statement.statement_code === "cash_flow_statement") {
          cashFlowStatementsAnnual[yearDiff] = statement.id;
        } else if (statement.statement_code === "balance_sheet_statement") {
          balanceSheetStatementsAnnual[yearDiff] = statement.id;
        }
      }
    });

    // Sort the fundamentals by fiscal year then fiscal period in descending order
    const filteredFundamentalsQuarters = fundamentals
      .filter(statement => ["Q1", "Q2", "Q3", "Q4"].includes(statement.fiscal_period))
      .sort((a, b) => {
        if (b.fiscal_year !== a.fiscal_year) {
          return b.fiscal_year - a.fiscal_year;
        }
        const periodOrder = ["Q4", "Q3", "Q2", "Q1"];
        return periodOrder.indexOf(b.fiscal_period) - periodOrder.indexOf(a.fiscal_period);
      });

    // Filter and collect the statement IDs for the last five quarters
    let quarterCount = { income_statement: 0, cash_flow_statement: 0, balance_sheet_statement: 0 };
    filteredFundamentalsQuarters.forEach(statement => {
      if (quarterCount[statement.statement_code] < 5) {
        if (statement.statement_code === "income_statement") {
          incomeStatementsQuarters[quarterCount.income_statement] = statement.id;
          quarterCount.income_statement++;
        } else if (statement.statement_code === "cash_flow_statement") {
          cashFlowStatementsQuarters[quarterCount.cash_flow_statement] = statement.id;
          quarterCount.cash_flow_statement++;
        } else if (statement.statement_code === "balance_sheet_statement") {
          balanceSheetStatementsQuarters[quarterCount.balance_sheet_statement] = statement.id;
          quarterCount.balance_sheet_statement++;
        }
      }
    });

    return {
      annual: {
        incomeStatements: incomeStatementsAnnual,
        cashFlowStatements: cashFlowStatementsAnnual,
        balanceSheetStatements: balanceSheetStatementsAnnual,
      },
      quarters: {
        incomeStatements: incomeStatementsQuarters,
        cashFlowStatements: cashFlowStatementsQuarters,
        balanceSheetStatements: balanceSheetStatementsQuarters,
      }
    };
  } catch (error) {
    console.error('Error fetching fiscal year and quarter statements:', error);
    throw error;
  }
}

async function fetchFinancialData(id) {
    console.log(id);
    const apiUrl = `https://api-v2.intrinio.com/fundamentals/${id}/standardized_financials?api_key=OjkzMmJmY2ViMDFhODU0ODA5YmMzZjBkNjRjOTczMzEw`;
    try {
      const response = await axios.get(apiUrl);
      return response.data;
    } catch (error) {
      console.error(`Error fetching financial data for ID ${id}:`, error);
      return null;
    }
  }

  async function mergeFinancialData(statements) {
    const mergedData = [];
  
    for (const id of statements) {
      if (id) {
        const data = await fetchFinancialData(id);
        if (data) {
          mergedData.push(data);
        }
      }
    }
  
    return mergedData;
  }

  async function getAndMergeStatements(companyId) {
    try {
      const { incomeStatements, cashFlowStatements, balanceSheetStatements } = (await getFiscalYearStatements(companyId)).annual;
  
      const incomeStatementsData = await mergeFinancialData(incomeStatements);
      const cashFlowStatementsData = await mergeFinancialData(cashFlowStatements);
      const balanceSheetStatementsData = await mergeFinancialData(balanceSheetStatements);
  
      const result = {
        incomeStatementsData,
        cashFlowStatementsData,
        balanceSheetStatementsData
      };
  
      console.log(JSON.stringify(result, null, 2));
      
      return result;
    } catch (error) {
      console.error('Error getting and merging statements:', error);
      throw error;
    }
  }
  
  // Usage example
  const companyId = 'AAPL'; // Replace with the actual company identifier
  getAndMergeStatements(companyId)
    .then(result => console.log('Done'))
    .catch(error => console.error(error));