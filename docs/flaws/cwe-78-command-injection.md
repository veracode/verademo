CWE-78: Improper Neutralization of Special Elements used in an OS Command ('OS Command Injection') in VeraDemo
==============================================================================================================

VeraDemo has a page called **Tools**, available at `/verademo/tools`.

This has functionality to let you check the uptime of a host or show a fortune.
Unfortunately they do so by directly executing shell commands without any validation.

Exploit
-------
We can exploit this like so:
1. Go to `/verademo/tools`.
2. For Host enter: `127.0.0.1 ; cat /etc/passwd`.
3. Click `Check`.
4. Observe the file listing as evidence that OS command injection has occurred.
5. Right click on the dropdown with "literature" and click Inspect to open the browser's developer tools.
6. Change the first option value from `literature` to `; ls -al /`.
7. Press the Change button in the browser.
8. Observe the directory listing as evidence that OS command injection has occurred.

Mitigate
--------
Validate host and fortunes definition.

Remediate
---------
Validate the host and use a whitelist for allowed fortunes files.

Resources
---------
* [CWE 78](https://cwe.mitre.org/data/definitions/78.html)
* https://downloads.veracode.com/securityscan/cwe/v4/java/78.html#example