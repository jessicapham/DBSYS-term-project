#!/bin/bash

echo "Running benchmarks for SQL queries..."
docker run -it -v $PWD:/home db-project sh -c "./run_sql.sh lsqb && \\
                            ./run_sql.sh job && \\
                            echo 'DONE'"

echo "Running benchmarks for Cypher queries..."
docker run -it -v $PWD:/home db-project sh -c "./run_cypher.sh lsqb && \\
                            ./run_cypher.sh ldbc && \\
                            echo 'DONE'"