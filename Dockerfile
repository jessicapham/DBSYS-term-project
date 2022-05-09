FROM ubuntu:18.04
RUN apt-get -y update && apt-get -y upgrade && \
    apt-get -y install git make g++ wget \
    coinor-cbc coinor-libclp-dev coinor-libcbc-dev \
    openjdk-11-jdk ca-certificates-java maven vim && \
    apt install -y software-properties-common && add-apt-repository ppa:deadsnakes/ppa -y && \
    apt -y update && apt install -y python3.8

RUN export JAVA_HOME="$(dirname $(dirname $(readlink -f $(which java))))" && apt install -y python3-pip

WORKDIR /home


