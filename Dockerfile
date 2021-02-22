

# Pull base image.
FROM openjdk:11-jre-slim
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
# Install Chrome

RUN apt-get --assume-yes update && apt-get --assume-yes upgrade
RUN apt update
RUN apt-get --assume-yes install wget
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
# Google Chrome binary, version 83

RUN dpkg -i google-chrome-stable_current_amd64.deb; apt-get -fy install
RUN google-chrome --no-sandbox --version
RUN apt-get --assume-yes install file
#RUN apt-get --assume-yes install lib32ncurses5

RUN apt-get update --yes
RUN apt-get install curl --assume-yes
RUN apt-get install libglib2.0-0 --assume-yes
RUN apt-get install libnss3 --assume-yes
RUN apt-get install libx11-6 --assume-yes
RUN apt-get install -yqq unzip
RUN wget -O /tmp/chromedriver.zip http://chromedriver.storage.googleapis.com/`curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip
RUN unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/

# Deploy java app

ENTRYPOINT ["java", "-jar", "/app.jar"]
