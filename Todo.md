Web Team Todo List
==================
All components are due from their owners by end of day on their assigned due
date.
- ~~Chaskin - Sunday 5/3/2015 - Update README.md with updates on "Setting Up a
Development Environment for future developers."~~

Frontend Todo List
=============
Overall
-------
~~- Glimmerglass is cammelCased in some placed but not in others.  Let's try to be
consistent with the gym names.~~


Homepage
--------
~~- The homepage needs real information on the page.
The current page was good for an initial mock up, but since we are moving into
user testing, we need a page that a real user would look at.~~

Real Time Display Pages
-----------------------
~~- The tables displaying classes have a number associated with each class which
 is indexed from zero.  Do we even need a number associated with each class?~~

~~- The table displaying the classes is not labled with the current day, so it
may not be clear to everyone what classes that table is describing.  Consider
including a link to the school's gym class information?~~

History - Kelly - Wednesday 4/29/2015
-------
- The history page currently shows an error when there is no data for the
associated week.  We should update this to default render data for each day of the week as zero unless given a new value.

~~- It is very unclear what the history page is displaying.  Are we displaying a
days worth of data, or a week, or a month?~~

~~- The current x-axis on the history graph represents time, but it is currently
indexed in milliseconds.  This is very unhelpful to a user who wanted to know
what time of day is busiest.~~

Navbar
------
~~- History is accessed through a drop down menu, but current count pages are not.
Like I said, let's try to keep it consistent but easy to use.  Any ideas here?~~

~~- Clicking "Contact" does nothing.~~

~~- Clicking the history dropdown, you see three entries:~~
  ~~- Cooper History~~
  ~~- Glimmerglass History~~
  ~~- One more seperated link~~

~~What is "One more seperated link" for?~~

Middleware Todo List
====================
- ~~Chaskin - Sunday 5/3/2015 - Clean up javadoc comments and generate javadoc
for middleware components.~~
- Chaskin - Sunday 5/3/2015 - Write rest endpoint documentation in swagger or similar utility.
- Chaskin - Sunday 5/10/2015 - Bug Fixes
- Chaskin - Tuesday 5/12/2015 - Todo List for Future Developers
- Chaskin - Make the server "remember" what the state of the gym was in case of reboot?

- Mark - Wednesday 4/29/2015 - Create a fake DB to mock 2-3 weeks of gym data.?
- Chaskin - Perform more rigorous test cases for existing middleware components.?
- James - Wednesday 5/13/2015 - Create cronjob to restart application on server reboot.?

Untested Components - Chaskin - Wednesday 5/13/2015
-------------------

###DatabaseUtility###

- ~~getCountData(Gym, Day) - returns a list of countDatapoints. i.e. a list of <count,timestamp> pairs for the given Gym, Day combination.~~

- updateAverageInCount(Gym, Day, int) - Updates the average number of people who have come into the gym on day with inCount.

- getAverageInCount(Gym, Day) - the average number of people who entered the given Gym for the given DayOfWeek.

- validateArgument(Gym)

- validateArgument(Day)
