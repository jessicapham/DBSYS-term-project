#!/bin/bash

chmod +x ./project_cypher/run_project.sh

if [ -f "dimacs_pg.graph" ]; then
    rm dimacs_pg.graph
fi

if [ -f "dimacs_jg.graph" ]; then
    rm dimacs_jg.graph
fi

if [ $# -eq 0 ]; then
    echo "No query file or benchmark directory specified."
    exit 0
fi

BENCHMARK=""
SCHEMA=""

cd project_cypher/

if [[ "$1" == "lsqb" ]]; then
    BENCHMARK="../lsqb/*.cypher"
    RESULTS="results_lsqb.txt"
fi

if [[ "$1" == "ldbc" ]]; then
    BENCHMARK="../ldbc/*.cypher"
    RESULTS="results_ldbc.txt"
fi


if [[ "$BENCHMARK" != "" ]]; then
    if [ -f $RESULTS ]; then
        rm $RESULTS
    fi
    for FILE in $BENCHMARK; do
        echo "---------- Computing treewidth for query:" $FILE "----------"
        echo "QUERY: " $FILE >> $RESULTS
        ./run_project.sh "$1/$FILE" > res.log 2>&1

        echo "---------- Primal Graph: tw(H) ----------\n"
        ../Triangulator/main -treewidth < ../dimacs_pg.graph 2>&1 | \
        while IFS= read line; do
            if [[ "$line" == Treewidth* ]]; then 
                echo "tw(H) = ${line#'Treewidth: '}" >> $RESULTS
            fi
        done

        echo "---------- Join Graph: tw(H') ----------\n"
        ../Triangulator/main -treewidth < ../dimacs_jg.graph 2>&1 | \
        while IFS= read line; do
            if  [[ "$line" == Treewidth* ]]; then 
                echo "tw(H') = ${line#'Treewidth: '}" >> $RESULTS
            fi
        done

        echo "---------- Hypertree: hw(H) ----------\n"
        ../hwh/detkdecomp 4 ../hypergraph.txt | \
        while IFS= read line; do
            if [[ "$line" == *hypertree-width:* ]]; then
                hw=${line#*'hypertree-width: '}
                hw=${hw%').'}
                echo "hw(H) = $hw" >> $RESULTS
            fi
        done

        rm ../dimacs_pg.graph
    done
    exit 0
fi

./run_project.sh "$@"
echo "Computing tw(H)..."
../Triangulator/main -treewidth <../dimacs_pg.graph

echo "Computing tw(H')..."
../Triangulator/main -treewidth <../dimacs_jg.graph