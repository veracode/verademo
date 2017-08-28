CWE-113: Improper Neutralization of CRLF Sequences in HTTP Headers ('HTTP Response Splitting') in VeraDemo
==========================================================================================================

VeraDemo remembers the username in a cookie. However because this is then set in a cookie without any validation this could be used
to inject one or multiple Carriage Return (\r) and Line Feed (\n) characters in the headers.

(Un)Fortunately the current VeraDemo setup makes use of Tomcat 8.5 which automatically removes CR and LF characters when they are
detected as part of header content (unlike a vulnerable Application Server like Tomcat 6 or Red Hat Wildfly). So it is not possible
to exploit this.

Also to split responses you need a vulnerable caching reverse proxy like Squid which has not been configured for VeraDemo.

Still relying on such external controls is dangerous as "must be hosted on an application server that protects against header injection"
is not typically a requirement that's taken into consideration as a project changes and hosting requirements change.

Exploit
-------
Not possible.

Mitigation
----------
* Using a Application Server that prevents CR and LF to be set for a header value.
* Not using a reverse proxy.

Remediation
-----------
Using whitelist validation and preferably only using hardcoded values.

Resources
---------
* [CWE-113](https://cwe.mitre.org/data/definitions/113.html)
* [Wikipedia: HTTP response splitting](https://en.wikipedia.org/wiki/HTTP_response_splitting)
* [OWASP: HTTP Response Splitting](https://www.owasp.org/index.php/HTTP_Response_Splitting)
* [IBM: On "HTTP Response Splitting" vulnerability](https://www-304.ibm.com/support/docview.wss?uid=swg27019020)