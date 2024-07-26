FROM eclipse-temurin:17-jdk
RUN  useradd -m -u 1000 -s /bin/bash/jenkin
RUN  yum install openssh-clients -y

FROM openjdk:17-jdk-alpine
COPY target/employee-management-system-maven-0.0.1-SNAPSHOT.jar employee-management-system-maven-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/employee-management-system-maven-0.0.1-SNAPSHOT.jar"]