CWE-73: External Control of File Name or Path in VeraDemo
========================================================================================================

VeraDemo uses untrusted data while constructing file paths.
This leaves the application vulnerable to malicious users uploading
their own files or exfiltrating files from the system. This may allow
for code execution or be used as a step to further system compromise.

Exploit 1 - Stealing files
-------
1. Go to /verademo/profile as a logged in user
2. Copy the URL of the "Download Profile Image" link
3. Paste the URL into the browser URL bar
4. Change the value of the `image` parameter to: ../../WEB-INF/web.xml
5. Observe that the web.xml configuration file is downloaded

Exploit 2 - Upload Files
-------
1. Go to /verademo/profile as a logged in user
2. Choose a payload as the profile image (example provided in doc/artifacts)
3. Change the username to: ../../resources/bin/exploit
5. Navigate to /verademo/resources/bin/exploit.html
6. See the script renders successfully

Mitigate
--------
Canonicalize the file path and check the directory matches the image directory.
Check the file type to ensure the file served/uploaded is a png file.

Remediate
---------
Don't use external data as part of the file path. Ideally map a randomly
generated file name to the user so user-controlled values are never mixed
with file path information.

Resources
---------
* [CWE 73](https://cwe.mitre.org/data/definitions/73.html)
* [Wikipedia: Directory traversal attack](https://en.wikipedia.org/wiki/Directory_traversal_attack)
* [OWASP: Path travsersal](https://www.owasp.org/index.php/Path_Traversal)