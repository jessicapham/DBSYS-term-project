FROM ubuntu:18.04
RUN apt-get -y update
RUN apt-get -y install git make g++ wget \
    coinor-cbc coinor-libclp-dev coinor-libcbc-dev \
    openjdk-11-jdk ca-certificates-java maven vim

RUN export JAVA_HOME="$(dirname $(dirname $(readlink -f $(which java))))"

WORKDIR /home


