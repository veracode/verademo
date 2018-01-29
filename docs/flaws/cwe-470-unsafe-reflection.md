CWE-470: Use of Externally-Controlled Input to Select Classes or Code ('Unsafe Reflection') in VeraDemo
=======================================================================================================

When you chose to **Listen** or **Ignore** **Blabbers** the process has been abstracted to a generic BlabberCommand.
The UI is then allowed to specify the command but it is not filtered for the allowed commands.


Exploit
-------
** Warning, use FireFox, do not use Chrome on Windows Server for this demo! **
We can exploit this like so:
1. Log in to VeraDemo.
2. Click **Blabbers** in the menu.
3. Open the FireFox Developer Tools (note do not use Chrome!<sup>1</sup>) to the Network tab and select HTML.
4. Click **Listen** for a blabber.
5. Note the POST requests, note the command in the post data for both requests.
6. Inspect a Listen or Ignore button.
7. Rename it to "Remove Account"
8. Change the hidden ```command``` field to ```removeAccount```.
9. Click the Remove Account button.
10. Notice the account has now been removed.


Mitigate
--------
Use a blacklist of banned command values.


Remediate
---------
Use a whitelist with allowed command values for that controller action and use the static values from the whitelist.


Resources
---------
* [CWE 470](https://cwe.mitre.org/data/definitions/470.html)

Footnotes
---------
1. [Chrome Developer Tools do not show form data on Windows Server 2012](https://stackoverflow.com/questions/46237449/chrome-developer-tools-do-not-show-form-data-on-windows-server-2012)