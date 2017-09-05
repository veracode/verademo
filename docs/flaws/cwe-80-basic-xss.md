CWE-80: Improper Neutralization of Script-Related HTML Tags in a Web Page (Basic XSS) in VeraDemo
=================================================================================================

VeraDemo mixes untrusted data with HTML, leading to the possibility of an attacker
injecting their own HTML that may do anything with the page.

Exploit
-------
1. Register an account with the following data:
   Username: hacker
   Password: hacker
   Confirm Password: hacker
   Real Name: Ms Hacker
   Blab Name: Hacker
2. Login with hacker/hacker
3. Add the following Blab: There's no scripting like cross site scripting.<script>alert("Hacked by Ms Hacker")</script>
4. Log out
5. Log in as admin/admin
6. Go to Blabbers
7. Follow Hacker.
8. Go to Feed.
9. Observe an alert box "Hacked by Ms Hacker".

Mitigate
--------
Validate input using a whitelist.

Remediate
---------
Encode data using [OWASP Java Encoder Project](https://www.owasp.org/index.php/OWASP_Java_Encoder_Project).

Resources
---------
* [CWE 80](https://cwe.mitre.org/data/definitions/80.html)
* [OWASP: OWASP Java Encoder Project](https://www.owasp.org/index.php/OWASP_Java_Encoder_Project)