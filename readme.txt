This is the VeraDemo application - Blab-a-Gag
=============================================

Blab-a-Gag is a fairly simple forum type application which allows:
 - users to post a one-liner joke
 - users to follow the jokes of other users or not (heckle or ignore)
 - users to comment on other users messages (heckle)
 
 There is a url (verademo/reset) which will reset the data in the database with a load of
  - users
  - jokes
  - heckles
  
 The main url (verademo/feed) shows the jokes/heckles that are relevant to the current user
 The blabbers url (verademo/blabbers) shows a list of all other users and allows the current user to heckle or ignore
 The profile url (veracode/profile) allows the current user to modify their profile
 There is a login/registration system for users on verademo/login and verademo/register
 
 
Shell Commands that will switch between Bad and Good versions of the code in UserController.java
================================================================================================
sed -i -e 's/\(START BAD CODE\) \*\/$/\1/g' UserController.java
sed -i -e 's/\(START GOOD CODE\)$/\1 *\//g' UserController.java

sed -i -e 's/\(START GOOD CODE\) \*\/$/\1/g' UserController.java
sed -i -e 's/\(START BAD CODE\)$/\1 *\//g' UserController.java
 
 To be able to deploy the app using Maven...
 ===========================================
 1. The VeraDemo VM needs to be reachable as verademo.veracode.local (use hosts file)
 2. Add the following to  {user.home}/.m2/settings.xml
  <servers>
    <server>
      <id>TomcatServer</id>
      <username>maven</username>
      <password>maven</password>
    </server>
    <server>
      <id>VeraDemoTomcat</id>
      <username>maven</username>
      <password>maven</password>
    </server>
  </servers>
 3. Use the redeploy goal in Maven
 
 