# For Security Labs we need both the application and DB running within the same container.
# It's far easier to use the MariaDB base image and install Maven and Tomcat on top than
# the other way around. We are using Maven to enable re-compilation within the lab.
#
#https://hub.docker.com/_/mariadb/
# This is Ubuntu 20.04.1 LTS
FROM mariadb

# Configure MariaDB
ENV MYSQL_RANDOM_ROOT_PASSWORD=true
ENV MYSQL_DATABASE=blab

# Copy DB schema for DB initialisation
COPY db /docker-entrypoint-initdb.d

# Install OpenJDK 8 and Maven
# Also install the fortune-mod fortune game
# https://docs.microsoft.com/en-us/dotnet/core/install/linux-ubuntu
RUN apt-get update \
    && apt-get -y dist-upgrade \
    && apt-get -y install openjdk-8-jdk-headless openjdk-8-jre-headless maven fortune-mod iputils-ping \
    && ln -s /usr/games/fortune /bin/ \
    && rm -rf /var/lib/apt/lists/*

COPY entrypoint.sh /

WORKDIR /app
COPY app /app

# Compile
RUN mvn package && rm -rf target

ENTRYPOINT ["/entrypoint.sh"]
CMD ["-c"]