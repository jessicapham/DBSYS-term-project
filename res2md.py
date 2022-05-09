#!/usr/bin/python3

import sys

def main():
    ts = """| Benchmark  | Query | tw(H) | tw(H')| hw(H) |\n| ----------- | ------- | ----- | ------|------ |\n"""
    for filename in sys.argv[1:]:
        count = 0
        with open(filename) as file:
            line = file.readline().rstrip()
            while(line):
                query = line.split("QUERY:")[1].rstrip().split("../")[1]
                ts += "|" + " " if count > 0 else query.split("/")[0]
                ts += "|" + query.split("/")[-1].split(".")[0]
                ts += "|" + file.readline().rstrip().split(" = ", 1)[1]
                ts += "|" + file.readline().rstrip().split(" = ", 1)[1]
                ts += "|" + file.readline().rstrip().split(" = ", 1)[1] + "|\n"
                line = file.readline().rstrip()
                count += 1
    
    print(ts)

main()