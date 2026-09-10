pipeline {
    agent any

    //tools {
       // maven 'Maven_3.9'   // Configure this name in Jenkins Global Tool Configuration
      //  jdk   'JDK_11'      // Configure this name in Jenkins Global Tool Configuration
  //  }

    parameters {
        choice(name: 'TEST_SUITE',
               choices: ['regression', 'smoke', 'parallel', 'api'],
               description: 'Select Maven profile / test suite to execute')
        choice(name: 'ENVIRONMENT',
               choices: ['qa', 'dev'],
               description: 'Select environment to run against')
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '15'))
        timeout(time: 60, unit: 'MINUTES')
    }

    stages {

        stage('Checkout') {
            steps {
                echo "Checking out source code from Git..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Compiling project..."
                bat 'mvn clean compile -DskipTests'
            }
        }

        stage('Execute Tests') {
            steps {
                echo "Running test suite: ${params.TEST_SUITE} on ${params.ENVIRONMENT}"
                bat """
                    mvn clean test -P${params.TEST_SUITE} \
                        -Denv=${params.ENVIRONMENT}
                """
            }
        }
    }

    post {
        always {
            echo "Publishing reports..."

            // Publish Extent Report
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ExtentReport.html',
                reportName: 'Extent Report'
            ])

            // Publish Cucumber HTML report (if BDD runner used)
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'cucumber-api-report.html',
                reportName: 'Cucumber API Report'
            ])

            // Archive TestNG / Surefire XML results
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'

            // Archive logs
            archiveArtifacts allowEmptyArchive: true,
                              artifacts: 'reports/**, logs/**',
                              fingerprint: true
        }
        success {
            echo 'Build and tests completed successfully.'
        }
        failure {
            echo 'Build failed — check the Extent report and console log for details.'
        }
    }
}
