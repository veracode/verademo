CWE-384: Session Fixation in VeraDemo
=====================================

A webserver has no means of identifying one request from another.
In order to do so it has the concept of a 'session' identified by
as session cookie. Any request with that cookie is identified as
belonging to that session.
Typically sessions hold authentication information making them
very vulnerable for attackers.
Attackers may try to steal session identifiers.
Or they may attempt to simply provide their own session identifier
and let the victim log into that.
This demo does just that with an XSS flaw and a weakened session cookie. 

Exploit
-------
** Warning, use FireFox. Do not use Chrome or another browser (for victim) with an XSS Auditor in this demo! **
1. Open a normal window (attacker) and a 'Private Window' (victim).
2. As an attacker go to /login, open up the Dev tools. In the Console tab, enter "document.cookie" (no quotes).
   Copy the value after "JSESSIONID="
3. Craft the following link, where <REPLACE_ME> (including angle brackets) is replaced by the JSESSIONID value copied in the previous step: /login?username=%22%3E%3Cscript%3Edocument.cookie%20%3d%20%22JSESSIONID%3d<REPLACE_ME>%3bpath%3d%2fverademo%22%3blocation.href%3d%22%2fverademo%2flogin%22%3b%3C%2fscript%3E%3Cinput%20type%3dhidden%20value%3d%22
4. Paste the link in the victim window.
5. Let the victim log in.
6. Reload the attacker window.
7. Note you are logged in as the victim.

Mitigate
--------
Remove useHttpOnly="false" from Tomcat context.xml.

Remediate
---------
Change the session id after login.