
# UT CS386D Database Systems Term Project
## Measuring the Width of a Join Graph for (Graph) Queries 
This repo contains our research project for the Mobile Computing course at UT Austin.

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