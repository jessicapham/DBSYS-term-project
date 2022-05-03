#!/bin/bash

docker run -it db-project sh -c "./run.sh lsqb && \\
                            ./run.sh job && \\
                            echo 'BENCHMARK RESULTS FOR LSQB' && \\
                            cat project/results_lsqb.txt && \\
                            echo 'BENCHMARK RESULTS FOR JOB' && \\
                            cat project/results_job.txt"