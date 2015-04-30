CLASSPATH = -cp .:/Applications/Tomcat/lib/servlet-api.jar:lib/*:src/servlet/
DEST = -d classes
SRC = \
    src/Model/DatabaseUtility.java \
    src/Model/DayOfWeek.java \
    src/Model/Gym.java \
    src/Model/GymClass.java \
	src/servlet/GetCurrentCount.java \
	src/servlet/GetGymClassesFor.java \
	src/servlet/GetMaxCount.java \
	src/servlet/Healthcheck.java \
	src/servlet/StartupShutdownListener.java \
	src/servlet/UpdateCount.java \
	src/servlet/UpdateHealth.java \


compile:
	javac $(CLASSPATH) $(DEST) $(SRC)

clean:
	$(RM) *.class
