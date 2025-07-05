# elsa-coding-challenges

# How to deploy on local Kubernetes cluster

# Maven:
PS D:\...\quiz> mvn clean install

    -> start Docker Desktop
    -> delete old images from LOCAL & docker hub REPO
    -> start below containers on Docker Desktop
        - zipkin
        - prometheus
        - grafana

# Docker:
PS D:\...\quiz> docker build -t longlethanh/user-score:0.0.1-SNAPSHOT .
PS D:\...\quiz> docker push longlethanh/user-score:0.0.1-SNAPSHOT

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
