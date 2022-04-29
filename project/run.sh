#!/bin/bash

# Compile code
javac -Xlint:deprecation -cp ../JSqlParser/target/jsqlparser-4.5-SNAPSHOT.jar:../jgrapht-1.5.1/lib/* $(find . -name '*.java')

# Run code
java -cp ../JSqlParser/target/jsqlparser-4.5-SNAPSHOT.jar:../jgrapht-1.5.1/lib/*:. Main "$@"
