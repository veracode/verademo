CWE-89: Improper Neutralization of Special Elements used in an SQL Command ('SQL Injection') in VeraDemo
========================================================================================================

Second Order SQL Injection is where an attacker first stores data that does not cause an SQL injection.
Then triggers the application to use this data in a subsequent SQL query.

When a user Ignores or Listens to another Blabber, VeraDemo will create an "event description" that contains
the username of the user that will be ignored or listened to. Unfortunately this is then used with concatenation
in an SQL query, so that if I create a user with username 'test"),(1,"admin was hacked' we will be able to inject that content.

Exploit
-------
1. Register an account with the following data:
   Username: test"),("admin","admin was hacked
   Password: test
   Confirm Password: test
   Real Name: Ms SQL Hacker
   Blab Name: SQL Hacker
2. Ignore the 500 error.
3. Login with admin/admin.
4. Go to Blabbers
5. Listen to Hacker
6. Go to Profile
7. Observe in your History that it says "admin was hacked"

Mitigate
--------
Use a whitelist to ensure data (like a username) only contains alphanumeric characters.

Remediate
---------
Query the data using prepared statements.

Resources
---------
* [CWE 89](https://cwe.mitre.org/data/definitions/89.html)
* [Wikipedia: SQL injection: Second order SQL injection](https://en.wikipedia.org/wiki/SQL_injection#Second_order_SQL_injection)
