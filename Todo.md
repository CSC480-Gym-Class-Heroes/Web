Middleware Todo List
====================
- Create a fake DB to mock 2-3 weeks of gym data.
- Perform more rigorous test cases for existing middleware components.
- Create cronjob to restart application on server reboot.
- Write rest endpoint documentation in swagger or similar utility.
Untested Components
-------------------

###DatabaseUtility###

- getCountData(Gym, Day) - returns a list of countDatapoints. i.e. a list of <count,timestamp> pairs for the given Gym, Day combination.

- updateAverageInCount(Gym, Day, int) - Updates the average number of people who have come into the gym on day with inCount.

- getAverageInCount(Gym, Day) - the average number of people who entered the given Gym for the given DayOfWeek.

- validateArgument(Gym)

- validateArgument(Day)
