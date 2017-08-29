CWE-117: Improper Output Neutralization for Logs in VeraDemo
============================================================

VeraDemo has a lot of logging statements, unfortunately it uses untrusted data
in the logging statements. This may allow an attacker to inject their own
"Enters" (Carriage Return & Line Feed or just Line Feed) and thereby inject
their own log lines.

While this may seem innocuos consequences of this may be that auditors can not
trust log files in case of an incident or attackers may attempt to inject
HTML and trick an administrator into executing it and causing XSS on
an administrator part of the application.


Exploit
-------
1. Go to /verademo/login?target=%0D%0AINFO%20%20This%20has%20been%20injected
2. Observe that "INFO  This has been injected" has been added to the logs.

Mitigation
----------
* Validate that data does not contain a CR or LF character.
* Encode or remove CR and LF characters.

Remediation
-----------
* Do not use untrusted data in logging statements
* Validate using a whitelist of hardcoded values
* Use a logging format that differentiates between a log statement and 
  (untrusted) runtime data like JSON, Splunk or Greylog.

Resources
---------
* [CWE-113](https://cwe.mitre.org/data/definitions/113.html)