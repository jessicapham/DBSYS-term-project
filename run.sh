#!/bin/bash

chmod +x ./project/run_project.sh

if [ -f "dimacs.graph" ]; then
    rm dimacs.graph
fi

if [ $# -eq 0 ]
  then
    echo "No query file or benchmark directory specified."
    exit 0
fi

BENCHMARK=""
SCHEMA=""

cd project/

if [[ "$1" == "lsqb" ]]; then
    BENCHMARK="../lsqb/*.sql"
    SCHEMA="lsqb/schema.txt"
fi

if [[ "$1" == "tpc-h" ]]; then
    BENCHMARK="../tpc-h/*.sql"
    SCHEMA="tpc-h/schema.txt"

fi

if [[ "$1" == "job" ]]; then
    BENCHMARK="../job/*.sql"
    SCHEMA="job/schema.txt"

fi


if [[ "$BENCHMARK" != "" ]]; then
    for FILE in $BENCHMARK; 
        do echo "---------- Computing treewidth for query:" $FILE "----------"; 
        ./run_project.sh "$SCHEMA" "lsqb/$FILE"
        ../Triangulator/main -treewidth < ../dimacs.graph
        rm ../dimacs.graph
    done
    exit 0
fi

./run_project.sh "$@"
../Triangulator/main -treewidth < ../dimacs.graph