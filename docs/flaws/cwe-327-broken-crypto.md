CWE-327: Use of a Broken or Risky Cryptographic Algorithm in VeraDemo
========================================================================================================

VeraDemo hashes user password with a known-weak hashing function, MD5.
Using a SQL injection vulnerability, we can extract the hashes from the database.

Exploit
-------
1. Login as a non-admin user (e.g. john)
2. Go to /verademo/profile
3. For Username fill in: john' UNION ALL (SELECT username, password, 1 FROM users WHERE username = 'admin') -- 
   (Note the trailing space character!)
4. Ignore the error message in the Eclipse console
5. Refresh the page
6. Copy the hash from the 'Real Name' field
7. Search Google for the hash and find a site that displays the original value

Mitigate
--------
Use salting when hashing passwords with a longer hash function like SHA-256.

Remediate
---------
Use a strong function for hashing passwords, like bcrypt or PBKDF2.

Resources
---------
* [CWE 327](https://cwe.mitre.org/data/definitions/327.html)
* [Wikipedia: Cryptographic hash function](https://en.wikipedia.org/wiki/Cryptographic_hash_function)
* [Wikipedia: Encryption](https://en.wikipedia.org/wiki/Encryption)
