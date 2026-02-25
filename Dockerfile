FROM eclipse-temurin:17-jdk
WORKDIR /code 
RUN apt-get update && apt-get install -y wget
RUN mkdir -p lib
RUN wget https://jdbc.postgresql.org/download/postgresql-42.7.10.jar -O ./lib/postgresql.jar
COPY src/ ./src/
RUN javac -d bin -cp lib/postgresql.jar src/db/*.java src/structs/*.java src/utils/*.java src/AppOperator.java
CMD ["java", "-cp", "bin:lib/postgresql.jar", "AppOperator"]

