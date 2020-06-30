FROM maven:3-jdk-8

EXPOSE 8080

COPY . /app

WORKDIR /app
RUN mvn compile

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
CMD ["mvn", "spring-boot:run"]