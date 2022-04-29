#!/bin/bash

chmod +x ./project/run_project.sh

if [ -f "dimacs.graph" ]; then
    rm dimacs.graph
fi

if [ $# -eq 0 ]
  then
    echo "No query input file provided."
    exit 0
fi

cd project/
./run_project.sh "$@"

cd ../Triangulator
./main -treewidth < ../dimacs.graph