CWE-601: URL Redirection to Untrusted Site ('Open Redirect') in VeraDemo
========================================================================================================

VeraDemo uses untrusted data while building URL redirects.
This leaves the application vulnerable to malicious users controlling
the destination website served when navigating through VeraDemo.

Exploit
-------
1. Go to /verademo/login?target=http://google.com
2. Login, if the user is not already
3. Observe the Google homepage is loaded

Mitigate
--------
Verify the redirect destination stays within the current application space.
Use a whitelist to validate the target parameter before returning it.

Remediate
---------
Don't use untrusted data when constructing URLs for redirection. Instead,
maintain a whitelist of known URLs/targets and only use external data
as a selector rather than the redirection value.

Resources
---------
* [CWE 601](https://cwe.mitre.org/data/definitions/601.html)
* [OWASP Cheat Sheet](https://www.owasp.org/index.php/Unvalidated_Redirects_and_Forwards_Cheat_Sheet)