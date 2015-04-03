CLASSPATH = -classpath /home/csaroff/apps/apache-tomcat-8.0.21/lib/servlet-api.jar:lib/*
DEST = build/web/WEB-INF/classes
SRC = \
	src/java/Model/*.java \
	src/java/Endpoints/*.java
    #src/League/*.java \
    #src/WoW/*.java 
WARFILE_NAME=Gym.war
all:
	make buildDir
	cd build/web/ && jar -cvf $(WARFILE_NAME) *
	mv build/web/$(WARFILE_NAME) ${TOMCAT_HOME}/webapps
buildDir:
	rm -r build
	mkdir build
	cp -r web/ build/web/
	cp -r lib/ build/web/WEB-INF/lib/
	make compile
compile:
	mkdir $(DEST)
	javac $(CLASSPATH) -d $(DEST) $(SRC)
clean:
	$(RM) *.class

