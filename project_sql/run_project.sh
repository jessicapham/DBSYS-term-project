#!/bin/bash

# Compile code
javac -Xlint:deprecation -cp test.jar:../jars/* $(find . -name '*.java')

# Run code
java -cp ../jars/*:. Main "$@"
