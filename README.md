# VeraDemo - Blab-a-Gag

### :information_source: Notice

This project is intentionally vulnerable! It contains known vulnerabilities and security errors in its code and is meant as an example project for software security scanning tools such as Veracode. Please do not report vulnerabilities in this project; the odds are they’re there on purpose :) .

## About

Blab-a-Gag is a fairly simple forum type application which allows:
* Users can post a one-liner joke.
* Users can follow the jokes of other users or not (listen or ignore).
* Users can comment on other users messages (heckle).

### URLs

* `/feed` shows the jokes/heckles that are relevant to the current user.
* `/blabbers` shows a list of all other users and allows the current user to listen or ignore.
* `/profile` allows the current user to modify their profile.
* `/login` allows you to log in to your account
* `/register` allows you to create a new user account
* `/tools` shows a tools page that shows a fortune or lets you ping a host.


## Run

If you don't already have Docker this is a prerequisite.

```
docker run --rm -it -p 127.0.0.1:8080:8080 antfie/verademo
```

Navigate to: http://127.0.0.1:8080.

## Exploitation Demos

See the `/docs` folder. [Pipeline Scan](https://docs.veracode.com/r/Pipeline_Scan) results of this application are in `/docs/scan_results/results.json`. 

## Technologies Used

* Spring boot
* MariaDB

## Development

To build the container run this:
```
docker pull mariadb:10.6.2
docker build --no-cache -t verademo .
```

To run the container for local development run this:

```
docker run --rm -it -p 127.0.0.1:8080:8080 --entrypoint bash -v "$(pwd)/app:/app" verademo
```

You will then need to manually run the two commands within `/entrypoint.sh`. The first starts the DB in the background whereas the second compiles and runs the application. Typically a container shouldn't have multiple services but this was done for convenience.