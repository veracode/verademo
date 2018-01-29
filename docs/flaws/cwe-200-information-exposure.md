CWE-200: Information Exposure in VeraDemo
=========================================

VeraDemo has not been properly configured for production use, as such it has
features that are helpful for debugging but also very helpful for attackers.

Exploit
-------
1. Go to /login
2. Fill in for Username: test'
3. Press Login
4. Observe stack trace with a lot of information.

Others:
* Go to /404 note the Tomcat version
* Go to /login and try to log in with a single quote: ', notice the SQL error and stack trace.
* Go to /tools and check an empty host, notice the ping command help.


Mitigate
--------
Only show stack trace for authorized users.

Remediate
---------
Remove stack trace generation.