pipeline {
  agent any

  stages {
    stage('Deploy Services') {
      steps {
        sh """
          cd /root/docker
          docker compose -f docker-compose.prod.yml pull
          docker compose -f docker-compose.prod.yml up -d
        """
      }
    }
  }
}
