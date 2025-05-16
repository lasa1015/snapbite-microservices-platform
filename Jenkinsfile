pipeline {
  agent any


  options {
    disableConcurrentBuilds()  // ✅ 避免多个部署 Job 同时执行
    timeout(time: 10, unit: 'MINUTES') // 可选：设置超时，避免死锁
  }


  environment {
    DEPLOY_USER = "root"
    SSH_CREDENTIAL_ID = "snapbite-ssh" // Jenkins 中配置的 SSH 凭据 ID
  }

  stages {
    stage('Load Deployment Config') {
      steps {
        script {
          def config = readFile('.deploy.env').split('\n').collectEntries {
            if (it.trim() && !it.trim().startsWith('#')) {
              def parts = it.split('=')
              [(parts[0].trim()): parts[1].trim()]
            } else {
              [:]
            }
          }

          env.TARGET_HOST = config.TARGET_HOST
          env.DEPLOY_PATH = config.DEPLOY_PATH
        }
      }
    }

    stage('Deploy Services via SSH') {
      steps {
        sshagent(credentials: [env.SSH_CREDENTIAL_ID]) {
          sh """
            ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${env.TARGET_HOST} '
              cd ${env.DEPLOY_PATH} &&
              docker compose -f docker-compose.prod.yml pull &&
              docker compose -f docker-compose.prod.yml up -d
            '
          """
        }
      }
    }
  }
}
