# Conflict Resolution

Stories about handling disagreements, difficult conversations, and resolving team conflicts.

## Amazon Leadership Principles This Category Covers
- **Have Backbone; Disagree and Commit** - Challenging decisions respectfully, then committing fully
- **Earn Trust** - Speaking candidly, listening attentively, treating others respectfully
- **Are Right, A Lot** - Having strong judgment and seeking diverse perspectives
- **Bias for Action** - Making decisions and taking calculated risks in disagreements

## Most Common Amazon SDE Interview Questions

1. **Tell me about a time you disagreed with your manager about a technical decision** *(Have Backbone; Disagree and Commit)*

2. **Tell me about a time you had to work with a difficult teammate/engineer** *(Earn Trust + conflict resolution in technical context)*

3. **Tell me about a time you had to convince other engineers to adopt your solution** *(Have Backbone; Disagree and Commit)*

4. **Tell me about a time you had to implement something you disagreed with** *(Have Backbone; Disagree and Commit)*

5. **Tell me about a situation where you had a conflict with someone on your team. What was it about? What did you do? How did they react? What was the outcome?** *(Earn Trust + Have Backbone; Disagree and Commit)*

## Stories

### Example 8: Disagree and Commit (ELT Team Collaboration)
**Question:** Describe a situation where you had to move forward with a team decision you didn't agree with.

In our application, we had a batch job that ran daily to pull data from multiple sources into our tables. To meet audit compliance, our team needed to implement additional processes to ensure data accuracy, correctness, and failure alerts. The proposed solution was to collaborate with the ELT (extract, load, transform) team, which specialized in standardizing data transfers. My manager supported this approach because it meant shared ownership, dedicated monitoring, and support for any compliance changes affecting data transfer job. While I understood the benefits, I advocated for keeping ownership within our team, as relying on ELT would introduce a dependency, slowing us down when making changes. For example, we would need to go through them for modifications, testing, and log retrieval. After discussing trade-offs, my manager acknowledged my concerns but ultimately decided to proceed with ELT for standardization. I committed to the decision and worked closely with ELT team to ensure a smooth transition. I also collaborated with them to implement the necessary audit compliance processes. In the end, the collaboration proved beneficial—we leveraged ELT's resources and monitoring capabilities, and the business was satisfied as we achieved audit requirements well ahead of the deadline.

### Example 9: Convincing Team on Data Migration Approach
**Question:** Tell me about a time when you had to disagree with your team's approach and convince them.


Situation: "Last year when RBC acquired HSBC, our team was tasked with migrating all client and financial data from HSBC systems to RBC's platform. This was a high-stakes, one-time migration with a very tight window due to regulatory audits and the confidential nature of banking data. Any failure during the production window would have significant business and compliance implications."
Task: "I needed to design a data migration approach that would reliably handle the transfer of millions of records while meeting our aggressive timeline. The challenge was balancing speed of implementation with reliability, especially given we had limited testing time with production-like data."
Action: "The team initially favored a single, fully automated script to handle parsing, transformation, validation, and loading in one go. Their reasoning was sound - it would be faster to develop and deploy. However, I was concerned about the risk. I had worked on similar migrations before where seemingly minor data inconsistencies caused entire automated runs to fail hours into processing.
I pushed back and proposed a hybrid approach: breaking the automation into separate components - one for bulk transformation, separate validation jobs, and manual verification for critical checks. The team resisted because they felt this was over-engineering and would add 3-4 days to our already tight development schedule. One senior engineer argued that we should trust our testing to catch issues, and the project manager was concerned about timeline delays.
To convince them, I had to get specific. I analyzed sample HSBC data and found several edge cases - like inconsistent date formats and null values in supposedly required fields - that would cause silent failures in a monolithic script. I demonstrated how these issues would be nearly impossible to debug quickly in a single large process, but easy to isolate and fix with modular components. I also showed that while initial development would take longer, recovery time from production issues would be dramatically reduced."
Result: "The team agreed to my approach. During testing, we discovered multiple unexpected data quality issues that would have caused the automated script to fail completely. Because of the modular design, we could quickly identify and fix each issue without rerunning the entire process. The production migration completed successfully without any data integrity issues, and we actually finished ahead of our revised timeline because debugging was so much faster."