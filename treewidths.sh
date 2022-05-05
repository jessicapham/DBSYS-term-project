#!/bin/bash

echo "Running benchmarks for SQL queries..."
docker run -it db-project sh -c "./run_sql.sh lsqb && \\
                            ./run_sql.sh job && \\
                            echo 'BENCHMARK RESULTS FOR LSQB' && \\
                            cat project_sql/results_lsqb.txt && \\
                            echo 'BENCHMARK RESULTS FOR JOB' && \\
                            cat project_sql/results_job.txt"

echo "Running benchmarks for Cypher queries..."
docker run -it db-project sh -c "./run_cypher.sh lsqb && \\
                            ./run_cypher.sh ldbc && \\
                            echo 'BENCHMARK RESULTS FOR LSQB' && \\
                            cat project_cypher/results_lsqb.txt && \\
                            echo 'BENCHMARK RESULTS FOR LDBC' && \\
                            cat project_cypher/results_ldbc.txt"