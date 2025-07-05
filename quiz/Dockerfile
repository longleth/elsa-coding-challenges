#############################
#    User Score Service     #
#############################

FROM        amazoncorretto:17.0.15-alpine
LABEL       authors="longle"

ADD         target/quiz-0.0.1-SNAPSHOT.jar /user-score-0.0.1-SNAPSHOT.jar

ENTRYPOINT  ["java", "-jar", "user-score-0.0.1-SNAPSHOT.jar"]