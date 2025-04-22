pipeline {
    agent any
    environment {
        MAVEN_HOME = '/usr/local/Cellar/maven/3.9.9/libexec'   // Path to your Maven installation
        PATH = "${MAVEN_HOME}/bin:${env.PATH}"
    }
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
