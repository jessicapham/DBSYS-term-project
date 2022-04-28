
# Database Systems Term Project
## Measuring the Width of a Join Graph for (Graph) Queries 

This repo contains our term project for the Database Systems course at UT Austin.

---

# Getting Started

## Requirements
- Maven
- JDK 8 or later

## Prerequisites
### **Build JSQLParser**
```
cd JSQLParser/
mvn package
````

This will produce a `jsqlparser-VERSION.jar` file in the `target/` directory which is used by the project code to parse queries into an AST.

# Running the Project
### **Parser**
From the root directory:
```
cd project/
./run.sh
```

If first time running, give execution permission to the script:
`chmod +x run.sh`

# Miscellaneous
## Git Submodules
This project uses code from other Git repositories. Git submodules is used to keep a git repository as a subdirectory of another git repository. 

### Current submodules
- JSQLParser

### To add a submodule:
```
git submodule add <repository URL>
```

## Graph File Formatting
http://prolland.free.fr/works/research/dsat/dimacs.html