CWE-200: Information Exposure in VeraDemo
=========================================

VeraDemo has not been properly configured to 

Exploit
-------
* Go to /404 note the Tomcat version
* Go to /login and try to log in with a single quote: ', notice the SQL error and stack trace.
* Go to /tools and check an empty host, notice the ping command help.


Mitigate
--------



Remediate
---------
