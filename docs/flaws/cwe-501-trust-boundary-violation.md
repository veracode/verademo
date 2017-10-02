CWE-501: Trust Boundary Violation in VeraDemo
=============================================

VeraDemo identifies a logged in user by the username in the session.
It also puts the username in the session during registration.
I wonder what would happen if registration is never completed...

Exploit
-------
1. Go to /register
2. Try to register for 'admin'
3. Note the error saying the user already exists.
4. Now go to 'Log in'
5. Note that you are logged in as admin.

Mitigate
--------
Rename "username" to "username_register".

Remediate
---------
Create a new session upon login.