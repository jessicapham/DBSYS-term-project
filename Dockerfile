FROM ubuntu:18.04
RUN apt-get -y update
RUN apt-get -y install git make g++ \
    coinor-cbc coinor-libclp-dev coinor-libcbc-dev \
    openjdk-11-jdk ca-certificates-java maven vim
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-arm64
RUN export JAVA_HOME

ADD . /home

WORKDIR /home

RUN wget https://sourceforge.net/projects/jgrapht/files/JGraphT/Version%201.5.1/jgrapht-1.5.1.tar.gz \
    && tar -xvzf jgrapht-1.5.1.tar.gz

RUN cd ./JSqlParser/ && mvn package && cd ../
RUN cd ./Triangulator && make && cd ../
RUN cd ./cd newdetkdecomp/ && make && ../
