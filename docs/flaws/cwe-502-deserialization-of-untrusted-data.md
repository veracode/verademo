CWE-502: Deserialization of Untrusted Data in VeraDemo
======================================================

VeraDemo does not make use of Spring Security for Authentication but instead implements it's own method.
You can find this in the ```com.veracode.verademo.controller.UserController``` class.
This class has a method "processLogin" which logs a user in and calls ```UserFactory.updateInResponse```.
This method serializes the User object and puts the result in a ```user``` cookie.

Every time the code then needs the current user it fetches it with ```UserFactory.createFromRequest``` as it does
in for example the ```showFeed``` method in the ```com.veracode.verademo.controller.BlabController``` class.

This entrusts the browser with supplying serialized Java code. As the deserialization process necessarily executed code
this is in effect a constrained form of code injection. An attacker can be then abuse this by supplying carefully crafted 
serialized data that, its deserialization executes shell commands or does other damage.

Exploit
-------
We can exploit this like so:

1. Log in to the application, note that you now have a "user" cookie.
2. Download the latest [frohoff/ysoserial](https://github.com/frohoff/ysoserial) JAR.
3. Run the following: ```java -jar ysoserial-master-v0.0.4-g35bce8f-67.jar CommonsCollections2 calc.exe | base64 -w 0 | clip```
4. Use the Chrome Developer Tools to alter the cookie value and paste the value from ysoserial
5. Refresh the page. This should give a Tomcat error but should also start the Windows Calculator.

Mitigate
--------
Sign the serialized data and verify the signature before using the serialized data.


Remediate
---------
Do not provide serialized Java code to untrusted parties, use an alternate serialization method that 
does not execute code like JSON.


Resources
---------
* [CWE-502](https://cwe.mitre.org/data/definitions/502.html)
* [frohoff/ysoserial](https://github.com/frohoff/ysoserial)