docker run -it -v $PWD:/home db-project sh -c "echo '------ BENCHMARK RESULTS FOR CYPHER QUERIES -----' \
                                                && python3 res2md.py project_cypher/results_ldbc.txt \
                                                && python3 res2md.py project_cypher/results_lsqb.txt \
                                                && echo '------ BENCHMARK RESULTS FOR SQL QUERIES -----' \
                                                && python3 res2md.py project_sql/results_lsqb.txt \
                                                && python3 res2md.py project_sql/results_job.txt \
                                                && python3 res2md.py project_sql/results_tpc-h.txt"