
# Database Systems Term Project
## Measuring the Width of a Join Graph for (Graph) Queries 

This repo contains our term project for the Database Systems course at UT Austin.

---
# Getting Started

## Requirements
- Docker

## Prerequisites
Fetch all the data from submodules
```
git submodule update --init --recursive
```

# Computing Tree Widths

### **Build Docker image**
```
./build.sh 
```
### **Run**
```
./treewidths.sh
```

The benchmark results are saved in `.txt` files in the `project_sql` and `project_cypher` directories
and outputted in the format of a markdown table to the console. The results can be properly
saved to an `.md` file by running: `./treewidths.sh > results.md`

To obtain the benchmark results again without re-computing the treewidths: 
```
./print-results.sh
```

# Miscellaneous
## Git Submodules
This project uses code from other Git repositories. Git submodules is used to keep a git repository as a subdirectory of another git repository. 

### Current submodules
- Triangulator
- newdetkdecomp

### To add a submodule:
```
git submodule add <repository URL>
```

## Graph File Formatting
http://prolland.free.fr/works/research/dsat/dimacs.html