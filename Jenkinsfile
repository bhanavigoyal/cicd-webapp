pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }

        stage('Test') { 
            steps {
                sh 'mvn test' 
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml' 
                }
            }
        }

        stage('Sonar-Report') {
            steps {
                sh '''
                    mvn clean install sonar:sonar \
                    -Dsonar.host.url=http://localhost:9000 \
                    -Dsonar.analysis.mode=publish
                '''
            }
        }
    }
}
