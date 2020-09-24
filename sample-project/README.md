# Camunda Sample Project

## Introduction

This project was designed to show a simple but powerfull usage of the _camunda-mock-services_, recreating a CI environment by **docker-compose** with a Camunda BPMN Server, a PostgresSQL and a Jenkins instance.

## Components

- [Simple Process BPMN](src/main/bpmn/simple_process.bpmn) - Business Flow to be tested
- [topicSimple-10.json](src/test/resources/scenarios/topicSimple-10.json) - External Service behaviour
- [SimpleOrderSimulation.scala](src/test/scala/order/request/SimpleOrderSimulation.scala) - Test to ensure the flow have the expected lifecicle.
    - Deploy the bpmn file
    - Submit a message to the just created process 
    - Check if the processing result is _COMPLETED_
    - Check if a variable _messageApproved_ is on that process variables map, with __true__ value.
    - Delete the deployment 

## Setting up the environment

At first, you need to run `docker-compose up -d` to download the docker images and start the services.
Camunda BPMN will be available on http://localhost/camunda. The installation is very intuitive, you can keep the default settings.
I suggest use two terminals (or Terminator in Linux)
- On first terminal, please compile the parent project before, and start the mock instance by running `java -jar ../target/mock-service-0.0.1-SNAPSHOT.jar --scenariosFolder=src/test/resources/scenarios`. You should see a **Started MockServiceApplication** and a few **TASK/CLIENT** lines.
- On second terminal run `mvn gatling:test` to start the test suite.
    - If you see on first terminal a message **Execution completed with variables messageApproved=true** That's good news, it's working as expected :pray:
- At the end a text report will me generated, and should have OK=5 KO=0 indicating all test scenarios were sucessful. It also generates a HTML report you can check in your browser.

Now you can make your changes to the BMPN process and run tests again to check the results, and of course create new mock scenarios and keep playing!!


## CI Setup
Jenkins is a little tricky, at the time I made this documentation I had to do a few anoying steps to set it up... If you know a better way to do the setup please let me know :grin:
- First, access the landing page at http://localhost:8080, and you can retrieve the secret password running `docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword`
- After first setup, you need to identify the container id by `docker ps` and use it with `docker exec –it 8f59969cde4d /bin/bash` to gain access inside the image
- Inside it, download the openjdk-11 and unzip into `/usr/local/jdk11/`
- Back to browser, install Maven and Gatling plugins. On plugin configurations, add a Maven 3 installation named _M3_.
- Create a new Pipeline Job and use the script below, changing your git repository. And just run it :sunglasses:
```groovy
    pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages {
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/ricardo-comar/camunda-mock-service.git'
                
                sh "mvn clean package"
                
                // Run Maven on a Unix agent.
                dir("sample-project") {
                    sh "java -jar ../target/mock-service-0.0.1-SNAPSHOT.jar --scenariosFolder=src/test/resources/scenarios &"
                    sh "mvn clean gatling:test -Dgatling.useOldJenkinsJUnitSupport=true"
                }

            }

            post {
                // If Maven was able to run the tests, even if some of the test
                // failed, record the test results and archive the jar file.
                always {
                    gatlingArchive()
                }
            }
        }
    }
}

 ```
---
# References
- https://medium.com/@tushar0618/docker-compose-to-run-and-connect-camunda-and-mysql-containers-2ac396a07d99
- https://camunda.com/download/modeler/
- https://www.infoq.com/articles/events-workflow-automation/
- https://camunda.com/best-practices/_/
- https://camunda.com/best-practices/handling-data-in-processes/
- https://camunda.com/blog/2015/08/start-and-complete-process-with-rest-api/
- https://forum.camunda.org/t/starting-new-process-instance-from-another-process-messages/3657/2
- https://docs.camunda.org/javadoc/camunda-bpm-platform/7.6/org/camunda/bpm/engine/RuntimeService.html
- https://github.com/camunda-consulting/messaging-example
- https://github.com/camunda/camunda-external-task-client-spring-boot
- https://camunda.com/best-practices/invoking-services-from-the-process/#_understanding_and_using_strong_external_tasks_strong
