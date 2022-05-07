FROM ubuntu:18.04
RUN apt-get -y update
RUN apt-get -y install git make g++ wget \
    coinor-cbc coinor-libclp-dev coinor-libcbc-dev \
    openjdk-11-jdk ca-certificates-java maven vim
ENV JAVA_HOME /usr/lib/jvm/java-11-openjdk-amd64

RUN export JAVA_HOME

ADD . /home
WORKDIR /home

RUN wget https://sourceforge.net/projects/jgrapht/files/JGraphT/Version%201.5.1/jgrapht-1.5.1.tar.gz \
    && tar -xvzf jgrapht-1.5.1.tar.gz

RUN wget https://downloads.lightbend.com/scala/2.13.8/scala-2.13.8.tgz \
    && tar -xvzf scala-2.13.8.tgz 

RUN cd ./Triangulator/ && make clean && make && cd ../
RUN cd ./newdetkdecomp/ && make clean && make && cd ../
