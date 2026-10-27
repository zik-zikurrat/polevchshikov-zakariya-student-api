// CI pipeline for the Student API of Polevchshikov Zakariya (IT2-2312, ID 37052)
pipeline {
    agent any

    tools {
        // JDK installation configured in Manage Jenkins -> Tools
        jdk 'JDK21'
    }

    triggers {
        // Poll GitHub every minute and build when a new commit appears
        pollSCM('* * * * *')
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '30'))
    }

    environment {
        STUDENT_NAME  = 'Polevchshikov Zakariya'
        STUDENT_GROUP = 'IT2-2312'
        STUDENT_ID    = '37052'
        IMAGE_NAME    = 'polevchshikov-zakariya-student-api'
        // Database credentials for the smoke test, stored in Jenkins Credentials
        // (exposes DB_CREDS_USR and DB_CREDS_PSW; the password is masked in the log)
        DB_CREDS      = credentials('polevchshikov-zakariya-db')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Student: ${STUDENT_NAME} (group ${STUDENT_GROUP}, ID ${STUDENT_ID})"
                sh 'git log -1 --pretty=format:"Commit: %h %s (%an)%n"'
                script {
                    // Image tag is derived from the version in build.gradle, e.g. 1.0.0 -> v1.0-polevchshikov-zakariya
                    env.IMAGE_TAG = sh(returnStdout: true, script: '''
                        awk -F"'" '/^version/ {split($2, v, "."); print "v" v[1] "." v[2] "-polevchshikov-zakariya"}' build.gradle
                    ''').trim()
                }
                echo "Docker image for this build: ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Build') {
            steps {
                sh '''
                    java -version
                    chmod +x gradlew
                    ./gradlew --no-daemon clean bootJar
                    ls -lh build/libs/
                '''
            }
        }

        stage('Test') {
            steps {
                // A failing test makes this command return a non-zero exit code,
                // so the stage fails and the next stages are skipped
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit 'build/test-results/test/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }

        stage('Verify') {
            steps {
                sh '''
                    echo "Checking that the image exists..."
                    docker image inspect ${IMAGE_NAME}:${IMAGE_TAG} \
                        --format 'Image: {{index .RepoTags 0}} | ID: {{.Id}} | Created: {{.Created}} | Size: {{.Size}} bytes'
                    docker images ${IMAGE_NAME}

                    echo "Smoke test: start the image with a temporary PostgreSQL..."
                    NET=pz-ci-${BUILD_NUMBER}
                    docker network create $NET
                    docker run -d --name pz-ci-db-${BUILD_NUMBER} --network $NET --network-alias postgres \
                        -e POSTGRES_DB=ci_db -e POSTGRES_USER="$DB_CREDS_USR" -e POSTGRES_PASSWORD="$DB_CREDS_PSW" \
                        postgres:17-alpine
                    until docker exec pz-ci-db-${BUILD_NUMBER} pg_isready -h 127.0.0.1 -U "$DB_CREDS_USR" -d ci_db; do sleep 2; done
                    docker run -d --name pz-ci-app-${BUILD_NUMBER} --network $NET \
                        -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ci_db \
                        -e SPRING_DATASOURCE_USERNAME="$DB_CREDS_USR" -e SPRING_DATASOURCE_PASSWORD="$DB_CREDS_PSW" \
                        ${IMAGE_NAME}:${IMAGE_TAG}
                    for i in $(seq 1 30); do
                        if docker run --rm --network $NET curlimages/curl:8.11.1 -sf \
                            http://pz-ci-app-${BUILD_NUMBER}:8080/polevchshikov-zakariya/; then
                            echo; echo "Smoke test passed"; exit 0
                        fi
                        sleep 2
                    done
                    docker logs pz-ci-app-${BUILD_NUMBER}
                    echo "Smoke test failed"; exit 1
                '''
            }
            post {
                always {
                    sh '''
                        docker rm -f pz-ci-app-${BUILD_NUMBER} pz-ci-db-${BUILD_NUMBER} 2>/dev/null || true
                        docker network rm pz-ci-${BUILD_NUMBER} 2>/dev/null || true
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "SUCCESS: ${IMAGE_NAME}:${IMAGE_TAG} built and verified for ${STUDENT_NAME}"
        }
        failure {
            echo "FAILURE: pipeline of ${STUDENT_NAME} stopped. Check the red stage and its console output."
        }
    }
}
