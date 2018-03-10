**Spring Boot 2 REST - AWS Example Project for LSD**

### The REST Service
####building the Docker image

just `mvn clean install`

or

`mvn docker:build`

`mvn docker:push`

####Running the service locally
`mvn docker:run`

http://localhost:8080/students


###configuring AWS
`aws configure`

`aws iam create-group --group-name kops`

`aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonEC2FullAccess --group-name kops`

`aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonS3FullAccess --group-name kops`

`aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonRoute53FullAccess --group-name kops`

`aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/IAMFullAccess --group-name kops`

`aws iam attach-group-policy --policy-arn arn:aws:iam::aws:policy/AmazonVPCFullAccess --group-name kops`

`aws iam create-user --user-name kops`

`aws iam add-user-to-group --user-name kops --group-name kops`

`aws iam list-users`

`aws iam create-access-key --user-name kops`

`aws configure`

`aws ec2 describe-availability-zones --region eu-central-1`

`export AWS_ACCESS_KEY_ID=<access key>`
`export AWS_SECRET_ACCESS_KEY=<secret key>`
   
###creating cluster storage on S3
`aws s3api create-bucket --bucket my-cluster-store --region eu-central-1 --create-bucket-configuration LocationConstraint=eu-central-1`

`export KOPS_STATE_STORE=s3://my-cluster-store`

###creating the cluster
`export NAME=my-rest-cluster.k8s.local`

`kops create cluster --v=0 --cloud=aws --node-count 2 --master-size=t2.micro --master-zones=eu-central-1a --zones eu-central-1a,eu-central-1b --name=${NAME} --node-size=t2.micro`

`kops get cluster`
`kops edit cluster my-rest-cluster.k8s.local`

###starting the cluster
`kops update cluster ${NAME}` for trial

`kops update cluster ${NAME} --yes` for real run

`kops validate cluster`

`kubectl get nodes --show-labels`

`kops rolling-update cluster --yes`

###deploying the dashboard
`kubectl create -f src/main/yaml/1.admin-user.yaml`

`kubectl create -f src/main/yaml/2.cluster-role.yaml`

`kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/master/src/deploy/recommended/kubernetes-dashboard.yaml`

###accessing the dashboard
#### getting a token for admin user
`kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep admin-user | awk '{print $1}')`

#### starting proxy to AWS cluster
`kubectl proxy`

#### dashboard URL
http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/#!/login

###deploying the application to the cluster

`kubectl run rest-example --replicas=1 --labels="app=rest-example" --image=jotka/rest-example:latest --port=8080`

`kubectl expose deployment rest-example --type=LoadBalancer --name=rest-example-service`

`kubectl get services rest-example-service`

`kubectl describe services rest-example-service`

###accessing the service on the cluster
http://a125dd595245811e8a7ad02c5a2fc852-2069798364.eu-central-1.elb.amazonaws.com:8080/students

###removing the cluster
`kops delete cluster --name my-rest-cluster.k8s.local --yes` 