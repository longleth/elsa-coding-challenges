# elsa-coding-challenges

# HOW TO START SERVICE ON LOCAL DEV ENVIRONMENT

# Prerequisites

- JDK: 17.x.x
- Maven
- Redis Cache Server
    Host: localhost
    Port: 6379 (Default)

- Database import - NOT REQUIRED due to using H2 on local dev env (Ideally will be replaced by MySQL in future)
- Due to real-time data requirement, the current solution temporarily uses Redis Cache
with DEFAULT Redis server configuration.

# Local startup

Navigate to your project directory on local (pulled from GitHub repository)

> mvn clean install
> java -jar target/quiz-0.0.1-SNAPSHOT.jar
>

# ENDPOINT TEST USING POSTMAN

Test data located at: /quiz/src/main/resources/elsa.quiz.json

Authentication: NO

POST: http://localhost:8080/api/quiz/join
JSON Request Body:
    {
        "quizId": "686923eb6af25b779321f0f6",
        "userId": "user_1"
    }
Response: OK

POST: http://localhost:8080/api/quiz/submit
JSON Body:
    {
        "quizId": "686923eb6af25b779321f0f6",
        "userId": "user_1",
        "questionId": "question_1",
        "answer": "A"
    }
Response: OK

GET: http://localhost:8080/api/quiz/leaderboard/686923eb6af25b779321f0f6
Response: OK
Response JSON Body:
    [
        {
            "value": "user_7",
            "score": 3.0
        },
        {
            "value": "user_2",
            "score": 3.0
        },
        {
            "value": "user_1",
            "score": 3.0
        },
        {
            "value": "user_4",
            "score": 2.0
        },
        {
            "value": "user_8",
            "score": 1.0
        },
        {
            "value": "user_6",
            "score": 1.0
        },
        {
            "value": "user_5",
            "score": 1.0
        },
        {
            "value": "user_3",
            "score": 1.0
        }
    ]


# ----------------------------------------- #
# How to deploy on local Kubernetes cluster #
# ----------------------------------------- #

# Maven:
PS D:\...\quiz> mvn clean install

    -> start Docker Desktop
    -> delete old images from LOCAL & docker hub REPO
    -> start below containers on Docker Desktop
        - zipkin
        - prometheus
        - grafana
    > docker pull bitnami/zipkin
    > docker pull bitnami/prometheus
    > docker run -d -p 9411:9411 --name elsa-zipkin --network elsa-vpn bitnami/zipkin:latest
    > docker run -d -p 9090:9090 --name elsa-prometheus --network elsa-vpn bitnami/prometheus:latest

# Docker:
PS D:\...\quiz> docker build -t longlethanh/user-score:0.0.1-SNAPSHOT .
PS D:\...\quiz> docker push longlethanh/user-score:0.0.1-SNAPSHOT

> docker network create elsa-vpn
> docker run -d -p 8080:8080 --name elsa-user-score --network elsa-vpn longlethanh/user-score:0.0.1-SNAPSHOT
> docker run -d --name elsa-redis --network elsa-vpn -p 6379:6379 redis/redis-stack:latest
> docker network inspect elsa-vpn

![img.png](containers.png)

# Kubernetes:
PS D:\...\quiz> cd k8s
PS D:\...\quiz\k8s> minikube start

PS D:\...\quiz\k8s> kubectl get services
PS D:\...\quiz\k8s> kubectl delete services srv1 srv2

PS D:\...\quiz\k8s> kubectl get deployments
PS D:\...\quiz\k8s> kubectl delete deployments dep1 dep2

    -> replica sets are implicitly removed then the child-pods will be automatically deleted

PS D:\...\quiz\k8s> dir
PS D:\...\quiz\k8s> kubectl create -f pod-deployment.yml
PS D:\...\quiz\k8s> kubectl create -f user-score-service.yml
PS D:\...\quiz\k8s> minikube dashboard

PS D:\...\quiz\k8s> kubectl port-forward service/user-score-service 7088:8888

# Test:
POST:
http://localhost:7088/api/quiz/join

POST:
http://localhost:7088/api/quiz/submit

GET:
http://localhost:7088/api/quiz/leaderboard/{QUIZ-ID}

# Tech notes:
<service-name>.<namespace>.svc.cluster.local:<service-port>
user-score-service.default.svc.cluster.local:8888
