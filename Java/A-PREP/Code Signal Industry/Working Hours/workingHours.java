Your task is to implement a simplified version of a program registering the working hours of contract workers at a facility. All operations that should be supported by this program are described below.

Solving this task consists of several levels. Subsequent levels are opened when the current level is correctly solved. You always have access to the data for the current and all previous levels.

Requirements
Your task is to implement a simplified version of a program registering the working hours of contract workers at a facility. Plan your design according to the level specifications below:

Level 1: The working hours register program should support adding workers to the system, registering the time when workers enter or leave the office and retrieving information about the time spent in the office.
To move to the next level, you need to pass all the tests at this level.

Note
You will receive a list of queries to the system, and the final output should be an array of strings representing the returned values of all queries. Each query will only call one operation.

Level 1
Introduce operations for adding workers, registering their entering or leaving the office and retrieving information about the amount of time that they have worked.

ADD_WORKER <workerId> <position> <compensation> — should add the workerId to the system and save additional information about them: their position and compensation. If the workerId already exists, nothing happens and this operation should return "false". If the workerId was successfully added, return "true". workerId and position are guaranteed to contain only English letters and spaces.
REGISTER <workerId> <timestamp> — should register the time when the workerId entered or left the office. The time is represented by the timestamp. Note that REGISTER operation calls are given in the increasing order of the timestamp parameter. If the workerId doesn't exist within the system, nothing happens and this operation should return "invalid_request". If the workerId is not in the office, this operation registers the time when the workerId entered the office. If the workerId is already in the office, this operation registers the time when the workerId left the office. If the workerId's entering or leaving time was successfully registered, return "registered".
GET <workerId> — should return a string representing the total calculated amount of time that the workerId spent in the office. The amount of time is calculated using finished working sessions only. It means that if the worker has entered the office but hasn't left yet, this visit is not considered in the calculation. If the workerId doesn't exist within the system, return an empty string.
