# Failure/Learning Examples

Stories about mistakes, failures, and what you learned from challenging situations.

## Amazon Leadership Principles This Category Covers
- **Learn and Be Curious** - Always seeking to improve and learning from mistakes
- **Ownership** - Taking responsibility for failures and their resolution
- **Earn Trust** - Being vocally self-critical and honest about shortcomings
- **Deliver Results** - Rising to the occasion despite setbacks
- **Bias for Action** - Taking calculated risks and learning from outcomes

## Most Common Amazon SDE Interview Questions

1. **Tell me about a time you failed** *(Learn and Be Curious - technical failure preferred for SDE)*

2. **Tell me about a time you received critical feedback on your code/design** *(Learn and Be Curious + Earn Trust)*

3. **Tell me about a time you committed a mistake?** *(Learn and Be Curious + Ownership)*

4. **Tell me about a time when you couldn't meet your deadline?** *(Deliver Results + Learn and Be Curious)*

5. **Tell me about a time where you did not know something and how you tackled it.** *(Learn and Be Curious + Dive Deep)*

## Stories

### Example 2: Roadblock with Financial API
**Question:** Tell me about a time when you faced a roadblock. How did you approach it?

Recently, a third-party financial API that our financial dashboard application heavily depended on announced it would be decommissioned in two months. This was a significant challenge because the dashboard relied on this API for most of its data. and I was tasked to find a replacement that maintained all previous data points and functionalities while staying within the same price point. First thing I did was research alternative APIs. No single API provider gives all the datasets that we are looking for, and the price would increase if we had to subscribe to multiple providers. I had to approach the solution in multiple different ways. First to try and find the API that best fit our need, I had to research into external API as well as RBC in-house APIs comparing data points covered and cost. For data points that wasn't in the API, I proposed and implemented calculating the data from the raw datasets. To stay in budget, I also had to negotiate with API providers for better pricing bundles. In the end, I successfully migrated to new data source without disrupting the dashboard functionality, and kept the price within budget. This experience taught me adaptability, and the importance of thorough research and the value of negotiating for what you need.

### Example 3: Missing Deadline (Volunteer Project)
**Question:** Tell me about a time when you failed to meet a deadline. What did you learn?

During my volunteer work as a web developer, I once took tasks that involved implementing some feature. During sprint planning, I estimated that I could complete these tasks within our 2 weeks sprint time. However, I underestimated the complexity of the task, and while working on it, I realized it takes significantly more time to complete than I initially thought. Usually if I promised to do something, I will try to deliver it, even if it meant working more to make it up, however, at this time, I had an important deliverable coming up for my full-time job. I had to prioritize my full time job, and I immediately communicated this to the volunteer team. I offered to mitigate this delay by breaking the feature up to smaller tasks and ask other volunteers if they can help with some of the smaller tasks without dependencies. Once I had more time after finishing up my full time job responsibilities, I worked on the rest of the tasks to ensure the feature is completed. This experience taught me the importance of realistic time estimation, this means deeply understanding all the tasks that make up a feature, and proactive communication, because without it, the team would not be able to adjust and help and it would've caused even more delay.
