#!/bin/bash

docker build -t db-project .
docker run -it --name db-project-container db-project 

