#!/bin/bash

docker run -it -v $PWD:/home db-project sh -c "cd ./Triangulator/ && make clean && make && cd ../ \
                                                && cd ./newdetkdecomp/ && make clean && make && cd ../ \
                                                && echo 'Running benchmarks for SQL queries...' \
                                                && ./run_sql.sh lsqb \
                                                && ./run_sql.sh job \
                                                && ./run_sql.sh tpc-h \
                                                && echo 'DONE' \
                                                && echo 'Running benchmarks for Cypher queries...' \
                                                && ./run_cypher.sh lsqb \
                                                && ./run_cypher.sh ldbc \
                                                && echo 'DONE'"

                                                