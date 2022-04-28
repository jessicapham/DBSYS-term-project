#!/bin/bash

# Compile code
javac -Xlint:deprecation -cp ../JSqlParser/target/jsqlparser-4.5-SNAPSHOT.jar $(find . -name '*.java')

# Run code
java -cp ../JSqlParser/target/jsqlparser-4.5-SNAPSHOT.jar:. Main "$@"
