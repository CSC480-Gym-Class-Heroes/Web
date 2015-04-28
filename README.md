Introduction
============

In the interest of keeping this software maintainable and easily reusable,
here is some fun documentation to read.  OMG it's so fun.  I can't believe
all the fun I'm having.

Setting Up a Development Environment
------------------------------------

There are some interesting quirks when setting up a database that I will 
try to walk you through.  This guide is written assuming that you're using
linux as your development environment.  Some of the command line usage will 
differ from windows.

###TL;DR###
####For Development####

    git clone https://github.com/CSC480-Gym-Class-Heroes/Web.git

    cd CSC480-Gym-Class-Heroes

    sudo apt-get install mysql-server

    mysql -u root -p < database.sql

If you don't already have it, pull down the 
[apache-tomcat binaries](https://tomcat.apache.org/download-80.cgi) and unzip
them.  For security purposes, we don't want to run tomcat as root.

This section changes depending on your login shell
If your login shell is bash, replace ~/.profile with ~/.bash_profile

    echo "export DEPLOYMENT=dev" >> ~/.profile
    echo "export TOMCAT_HOME='/Path/To/Tomcat/Directory/'" >> ~/.profile
    source ~/.profile

    make

    ${TOMCAT_HOME}/bin/catalina.sh run

Open a web browser and goto http://localhost:9999/Gym_Rats/
If you see anything on this page, you had a successful install

###JDBC requires a username and password to login to the database.###

This means that in order to actually send and retrieve information,
you will have to supply the JDBC DriverManager's getConnection method
with Strings representing the username and password for your database.

For obvious security reasons, it is a bad idea to keep the password for your
database in plaintext in the source code of your application.  This becomes
even more of a concern if your application is in a public github repository.

For [less than obvious reasons]('http://wiki.apache.org/tomcat/FAQ/Password'),
it is no more secure to keep your password in an encrypted file on disk and 
read it into your program on startup.

The established best practice is to simply keep a config file with the db
password in plain text.  Limit the config file's access rights to only be
accessible by the user that is meant to run your application server, which
should be strongly protected by that user's login password.

MAKE SURE TO .gitignore THIS FILE.  The whole point of this security measure is
to keep anyone from learning your database password.  Publishing it to a public
repository is counterproductive.

Since the application needs the password to run, make sure to create the
password.db file at path/to/applicationDirectory/web/WEB-INF/password.db

###JDBC requires a database url to connect to it###

When you're setting up your database for local deployment, your JDBC url will 
probably be localhost.  When you move to production, your JDBC url will probably
be moxie.cs.oswego.edu. 
To make deployment faster and easier, there are two deployment environments: 
development and production.  Make sure to change your DEPLOYMENT environment
variable to dev if you're running this application locally, and change it to 
prod if you're running the application in production.
