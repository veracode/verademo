CWE-77: Improper Neutralization of Special Elements used in a Command ('Command Injection') in VeraDemo
=======================================================================================================

VeraDemo has a hidden page called **Tools**, available at /verademo/tools.

This has functionality to let you check the uptime of a host or show a fortune.
Unfortunately they do so by directly executing shell commands without any validation.

Exploit
-------
We can exploit this like so:
1. Go to /verademo/tools
2. For Host enter: google.com && calc
3. Observe the calculator being opened as evidence that command injection has occurred.
4. Right click on the dropdown with "funny" and click Inspect.
5. Change the first option value from "funny.txt" to "funny.txt && calc"
6. Press the Change button in the browser
7. Observe the calculator being opened as evidence that command injection has occurred.

Mitigate
--------
Validate host and fortunes definition.

Remediate
---------
Validate the host and use a whitelist for allowed fortunes files.

Resources
---------
* [CWE 77](https://cwe.mitre.org/data/definitions/77.html)