
QUESTIONS for interviewers:

What initially drew you to working for Circle? and how has that evolved? I know crypto space changes fast so I'm curious about your experience at circle

What are some of the most exciting projects you've had the chance to work on during your time at Circle?

What's Circle working on next that you're most excited about?



What do you know about Circle:

Company behind stable coin usdc and eurc, usdc is the second biggest stablecoin greatest advantage in the stable coin field is probably its regulatory compliance, transparency and trust.
Circle is building the financial system primarily though its stablecoins so
services like redemption service, connecting with instutions for cross border
payments. In crypto space, it has a protocol to transfer usdc accross different chains

Why Circle
So I've known of circle for a while due to its stablecoin. I've an interest in crypto for a while now. Even back when I was in uni, I particiapted in and created trading bots for crypto.com api competition. I've so built small program to be use interacting with exchangesr and blockchains. So what What really excites me about circle is how much interesting work there is here. They're dealing with everything from technical challenges like moving money across different blockchains to navigating all these new regulations and the scale of the business. For my next role, I want something that's faster-paced and greater impact from the products that I work on, and I think Circle can provide exactly that.



Questions include what interests you in Circle, your projects, and how you behave under pressure.


If you are given an offer from Circle, which team would be your "dream team" that you'd want to be a part of?


How do you manage your work load at your current company?
I manage my workload by prioritizing tasks based on business impact and deadlines, and by breaking larger projects into smaller, trackable milestones. For planning and estimations I make sure to  factor in potential dependencies or risks up front. Which allow us to identify potential issues that could affect the timeline. Also, delivering consistently and earlier than expected so I have buffer time if there are multiple tasks with similar deadlines.


What is your greatest strength?
Love for new challenges & continuous learning

I think my greatest strength is that I love working on new and challenging tasks, especially when they require me to quickly learn new skills or technologies. For example, when my team was tasked with building a modern web application in React and Node.js — technologies we had little experience with as a mostly Java backend team — I volunteered to lead the project. I taught myself the stack, gathered requirements from stakeholders, implemented all core features, and collaborated across teams to integrate necessary tools. We launched on time, and the application is now used by all credit officers in the wholesale credit risk group. I find these challenges energizing because they stretch my abilities and lead to meaningful impact.


What is your biggest weakness that you'd bring to Circle - 
Overcommitting without fully scoping

Earlier in my career, I sometimes committed to tasks before fully considering all the dependencies, scalability, or edge cases. For example, while working on a credit portfolio dashboard, I tried to reduce costs by building a function that used Excel stock data instead of paying for an API provider. It worked in small tests, which made me commit to implementing the function. but I hadn’t fully considered performance and scalability — once I tried it with real data volumes, it was too slow and unreliable. We ended up scrapping it, which meant wasted development time. That experience taught me to validate solutions at scale before committing.





## ADDING SECURITY/ CODE SCANNING ANALYSIS TO PIPELINE ** 

Task: When I first joined my team four years ago, one of my first tasks was to upgrade our logging library from Log4j to Log4j2 due to a critical security vulnerability.

Situation: While working on this, I spoke with coworkers and discovered that we had no automated vulnerability checks in our CI/CD pipeline. As a result, developers were often blocked when updating packages, needing to manually search through our internal repository for secure versions. I also realized we lacked any code analysis tool.

Action: This huge gap was surprsing to me and I proposed integrating both a package vulnerability scanner and a static code analysis tool. After getting approval, I research and found the tools that was available for internal use within RBC, and integrated both into our CI/CD pipeline. I also work on the initial effort to address the code vulnerabilities, and dependencies vulnerabilities.

Result: After the scans were integrated to all the pipeline and the initial fixes were put in, our team agreed to use the scans and ensure that there would be no high or critial vulnerability were to be introduced before promoting to production. This year, RBC rolled out a company-wide policy requiring teams to move to GitHub Actions and include vulnerability and code analysis scans, and any build with a critical issue would be block from deploying to prod. Thanks to the groundwork we've put in 3 years ago, our team’s transition was very smooth, we had already remediated most issues ahead of the policy change.


## DELIVERING GRM0 ** 
Task: Two years ago, my team was tasked with building a new web application to help credit officers track their client portfolios, monitor events that might impact credit quality, and move from paper-based quarterly affirmations to a fully digital process.

Situation: The project required using React and Node.js—technologies that our team has little experience with, as we primarily worked on Java-based backend services. I did have some experience through a previous volunteer project, and I saw this as an opportunity to challenge myself and asked my I asked my manager to let me lead the project and committed to quickly learning everything needed to deliver a full-stack solution.

Action: From being the sole developer of this new project, I worked closely with business stakeholders to understand their needs and gather detailed requirements. I also researched external APIs and collaborated with internal RBC teams to integrate tools that could meet our needs. On the development side, I implemented all features and ensured we adhered to deadlines.

Result:
The application launched successfully and on schedule. It took 8 months for beta release and a year to production. It received strong positive feedback from both directors and credit officers, significantly streamlining their workflow and reducing the quarterly affirmation process from 2-3 weeks each quarter to just a few days. It is now being used by all directors and credit officers in the wholesale credit risk within RBC.

## COLLABORATION TO SAVE MONEY FOR GRM0**

Task: This is the full-stack application that I led, while I'm building the appication to help credit officers manage client portfolios. It's like a dashboard that track each client credit events like stock increase decrease, rating increase and decrease. We had a very limited budget. Majoriy of it was committed to an external APIs services that provided real-time stock data and external ratings — a feature that was highly requested by our users.


Situation: I needed to find a way to deliver the application with the required features without exceeding our budget.

Action:
During an a company hackathon, I met an innovative internal team that specialized in hosting applications and experimental projects on cloud platform. I reached out and proposed a collaboration: they could use our application as a real-world use case to demonstrate their platform's capabilities, and in return, they would help us host the application. I worked closely with their engineers to ensure our requirements were met, and coordinated deployment, and access controls on their infrastructure.

Result:
This collaboration eliminated the need for physical server hosting, which was part of our original plan and would have consumed a large portion of the project budget and a lot of time getting approvals and setting up. By partnering with the internal cloud team, we avoided those costs entirely, saving this project tens of thousands of dollars annually. This allowed us to redirect the budget toward user-requested features — such as integrating real-time stock data via an external API.The  partnership simplified our infrastructure challenges and turned into a win-win for both teams. 


## MISSING DEADLINE**

Situation:  While I was volunteering as a web developer for a nonprofit a few years ago, we followed two-week sprints where each volunteer committed to around 8 hours of work. During one sprint, I picked up a feature that I thought I could finish within that time.

Task: The feature seemed straightforward at first, so I estimated I'd be able to get it done during the sprint. But once I started working on it, I realized it was more complicated than I expected — there were some technical details I hadn’t considered and there's a dependency on another task that I would have to pick up to complete.


Action: Normally, I’d just work extra hours to finish what I committed to, but around that time, I had a big deadline coming up at my full-time job and I didn't have the extra time. So I knew I wouldn't be able to complete everything on time.
As soon as I realized I couldn’t finish everything on time, I let the team know what was going on. I didn’t want to hold things up and let it affect our timeline and the next sprint, so I broke the feature into smaller pieces and asked if anyone had time to help with the parts that didn't have a depencency. After I wrapped up my work deadline, I came back and finished the rest.

Result: The feature ended up getting delivered a bit after the original sprint. This experience taught me to be more careful when estimating tasks — and to take time up front to really think about how each feature interact with the entire system before commiting. It also showed me how important it is to speak up early when things don’t go as planned. On the bright side, the breakdown of tasks we did during that sprint actually helped us move faster in later sprints. So even though this was a bit of a hiccup, it didn’t affect our overall progress, and we were able to deliver the MVP ahead of schedule.

## DISAGREE AND CONVINCE TEAM OF DATA MIGRATION APPROACH ##

Situation: Last year, when RBC acquired HSBC, our team was responsible for migrating relevant client and financial data from HSBC systems into RBC's platform. This was a high-stakes, one-time migration, and we had a very tight window due to regulatory audits and the confidential nature of the data. Any issues during the production window could’ve had major compliance and business consequences.

Task: I was responsible for designing and developing a migration approach that could handle millions of records reliably, while also meeting our aggressive deadlines. The challenge was finding the right balance between speed and reliability, especially since we didn’t have much time to test with production-like data due to the confidential data.

Action: The team initially wanted to build a single automated script that would handle parsing, transforming, validating, and loading all at once. It was appealing because it seemed quicker to develop and deploy. But I had concerns —specifically about data quality, since we didn’t have direct access to the source system or database. All we had was a flat file export, which made it hard to fully understand what kind of edge cases or inconsistencies might be in the data. Without that visibility, I felt it was risky to rely on a fully automated process, especially for something this critical.
I pushed back and suggested a hybrid approach: break the migration into modular components — bulk transformation in one job, validation in another, and manual review for records that fails validation checks. At first, there was a lot of resistance. Some engineers felt this added unnecessary complexity, and would slow us down and to put more trust in the data files provided by HSBC.
To make my case, once we received the data files from HSBC, I started digging into them and like I thought there are many edge cases to handle — things like inconsistent date formats, missing values in required fields, and even duplicate client records...ect. Some of these issues we could account for when building the script during testing, but my concern was that if new data issues showed up in production, a single monolithic script would likely fail, and it would be much harder to isolate and debug the root cause quickly.

Result: After that the team agreed. And I start the implementation, during testing, I uncovered a lot more unexpected data quality issues that would’ve caused the all-in-one script to fail or produce incorrect results. Thanks to the modular design, we were able to debug and fix issues quickly without disrupting the whole pipeline. The production migration ended up completing smoothly, with no data integrity problems.






