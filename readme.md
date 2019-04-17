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


## Build

To build Verademo [Maven](https://maven.apache.org) is required.

`mvn package` will build the web application and output a war file to `target/verademo.war`. This war file can be uploaded to Veracode for static analysis.

## Configure

To run Verademo [MySQL](https://www.mysql.com/) and [Tomcat](https://tomcat.apache.org/) are required.

The simplest way to aquire these on MacOS is via [Homebrew](http://brew.sh/). Install Homebrew then:

    brew install mysql tomcat

### Database

Set up a database in MySQL called `blab` with a user of `blab` and password `z2^E6J4$;u;d`
 
## Run

Deploy the build output war file to Tomcat.

Open `/reset` in your browser and follow the instructions to prep the database

Login with a username/password as defined in `Utils.java`

## AWS Deployment

Verademo will also run out-of-the-box in AWS. Simply upload the `target/verademo.war` file into a Tomcat Elastic Beanstalk environment (with associated Amazon RDS). The database credentials listed above are not required when running in AWS.

On the first environment deployment, a script will automatically setup the database. Subsequent application re-deploys (without environment re-deploy), or application server restarts will not alter the database.

## Exploitation Demos

See the `docs` folder
