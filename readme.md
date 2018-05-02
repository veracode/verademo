# VeraDemo - Blab-a-Gag

## About

Blab-a-Gag is a fairly simple forum type application which allows:
 - users to post a one-liner joke
 - users to follow the jokes of other users or not (listen or ignore)
 - users to comment on other users messages (heckle)
 
### URLs

`/reset` will reset the data in the database with a load of:
 - users
 - jokes
 - heckles
  
`/feed` shows the jokes/heckles that are relevant to the current user.

`/blabbers` shows a list of all other users and allows the current user to listen or ignore.

`/profile` allows the current user to modify their profile.

`/login` allows you to log in to your account

`/register` allows you to create a new user account

`/tools` shows a tools page that shows a fortune or lets you ping a host.
   
## Configure

Build and installation requires [Maven](https://maven.apache.org), [MySQL](https://www.mysql.com/) and [Tomcat](https://tomcat.apache.org/).

The simplest way to aquire these on MacOS is via [Homebrew](http://brew.sh/). Install Homebrew then:

    brew install maven mysql tomcat

### Database

Set up a database in MySQL called `blab` with a user of `blab` and password `z2^E6J4$;u;d`
 
## Run

`mvn package` will build the web application and output a war file to `target/verademo.war`

Deploy the resulting war file to Tomcat (or run a SAST scan on it).

Open `/reset` in your browser and follow the instructions to prep the database

Login with your username/password as defined in `Utils.java`
