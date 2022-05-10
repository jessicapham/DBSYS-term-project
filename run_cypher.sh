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

BENCHMARK="../$1/*.cypher"
RESULTS="results_$1.txt"

if [[ "$BENCHMARK" != "" ]]; then
    if [ -f $RESULTS ]; then
        rm $RESULTS
    fi
    COUNTER=1
    TOTAL=$(ls -l $BENCHMARK | wc -l)
    echo "Benchmark: $1"
    for FILE in $BENCHMARK; do
        echo "QUERY: " $FILE >> $RESULTS
        echo -ne "$COUNTER / $TOTAL \r"
        ./run_project.sh "$1/$FILE" > res.log 2>&1

        ../Triangulator/main -treewidth < ../dimacs_pg.graph 2>&1 | \
        while IFS= read line; do
            if [[ "$line" == Treewidth* ]]; then 
                echo "tw(H) = ${line#'Treewidth: '}" >> $RESULTS
            fi
        done

         ../Triangulator/main -treewidth < ../dimacs_jg.graph 2>&1 | \
        while IFS= read line; do
            if  [[ "$line" == Treewidth* ]]; then 
                echo "tw(H') = ${line#'Treewidth: '}" >> $RESULTS
            fi
        done

        K=1
        FOUND=false
        while [ $FOUND == "false" ]; do
            while IFS= read line; do
                if [[ "$line" == 'Hypertree of width'*'not found'* ]]; then
                    break
                elif [[ "$line" == *hypertree-width:* ]]; then
                    hw=${line#*'hypertree-width: '}
                    hw=${hw%').'}
                    echo "hw(H) = $hw for k = $K" >> $RESULTS
                    FOUND=true
                fi
            done <<<$( ../newdetkdecomp/bin/detkdecomp $K ../hypergraph.txt  2>&1)
            (( K++ ))
        done
        COUNTER=$((COUNTER+1))
        rm ../dimacs_pg.graph
    done
    echo $'\n'
    exit 0
fi

./run_project.sh "$@"
echo "Computing tw(H)..."
../Triangulator/main -treewidth <../dimacs_pg.graph

echo "Computing tw(H')..."
../Triangulator/main -treewidth <../dimacs_jg.graph