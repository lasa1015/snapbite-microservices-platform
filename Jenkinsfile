pipeline {
  agent any

  options {
    disableConcurrentBuilds()
    timeout(time: 10, unit: 'MINUTES')
  }

  stages {
    stage('Deploy Services (Local)') {
      steps {
        script {
          echo "[INFO] 本地部署镜像到当前机器..."

          sh '''
            cd /workspace
            docker compose -f docker-compose.prod.yml pull || true
            docker compose -f docker-compose.prod.yml up -d
          '''
        }
      }
    }
  }
}
