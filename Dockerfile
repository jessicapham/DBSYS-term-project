FROM ubuntu:18.04
RUN apt-get -y update
RUN apt-get -y install git make g++ wget \
    coinor-cbc coinor-libclp-dev coinor-libcbc-dev \
    openjdk-11-jdk ca-certificates-java maven vim
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64

RUN export JAVA_HOME

WORKDIR /home
