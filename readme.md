## About

This is the VeraDemo application - Blab-a-Gag

Blab-a-Gag is a fairly simple forum type application which allows:
 - users to post a one-liner joke
 - users to follow the jokes of other users or not (heckle or ignore)
 - users to comment on other users messages (heckle)
 
### URLs

`/reset` will reset the data in the database with a load of:
 - users
 - jokes
 - heckles
  
`/feed` shows the jokes/heckles that are relevant to the current user.

`/blabbers` shows a list of all other users and allows the current user to heckle or ignore.

`/profile` allows the current user to modify their profile.

`/login` allows you to log in to your account

`/register` allows you to create a new user account
 
## Configure

### Dependencies

Download the Veracode custom cleanser library from `https://tools.veracode.com/customcleanser/VeracodeAnnotations.jar` then run:

    mvn install:install-file -Dfile=VeracodeAnnotations.jar -DgroupId=com.veracode -DartifactId=annotations -Dversion=1.0 -Dpackaging=jar

### Database

Set up a database in MySQL called `blab` with a user of `blab` and password `z2^E6J4$;u;d`
 
### Switching between good/bad code

    # Bad to good
    sed -i -e 's/\(START BAD CODE\) \*\/$/\1/g' UserController.java
    sed -i -e 's/\(START GOOD CODE\)$/\1 *\//g' UserController.java
    
    # Good to bad
    sed -i -e 's/\(START GOOD CODE\) \*\/$/\1/g' UserController.java
    sed -i -e 's/\(START BAD CODE\)$/\1 *\//g' UserController.java

## Run

Deploy to Tomcat

Open `/reset` in your browser and follow the instructions to prep the database

Login with your username/password as defined in `Utils.java`

