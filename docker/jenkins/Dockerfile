FROM jenkins/jenkins:lts

USER root

# 安装 docker CLI
RUN apt-get update && \
    apt-get install -y docker.io && \
    usermod -aG docker jenkins

USER jenkins
