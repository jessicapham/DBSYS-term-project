#!/bin/bash

# Compile code
javac -Xlint:deprecation -cp /Users/umairshahzad/Desktop/DBSYS-term-project/front-end/expressions/target/expressions-9.0_2.13-9.0-SNAPSHOT.jar:/usr/local/opt/scala@2.13/idea/lib/scala-library.jar:../front-end/ast-factory/target/cypher-ast-factory-9.0-9.0-SNAPSHOT.jar:../front-end/javacc-parser/target/cypher-javacc-parser-9.0-9.0-SNAPSHOT.jar:../front-end/ast/target/ast-9.0_2.13-9.0-SNAPSHOT.jar:../front-end/util/target/util-9.0_2.13-9.0-SNAPSHOT.jar:../front-end/neo4j-ast-factory/target/opencypher-cypher-ast-factory-9.0_2.13-9.0-SNAPSHOT.jar:../jgrapht-1.5.1/lib/* $(find . -name '*.java')

# Run code
java -cp /Users/umairshahzad/Desktop/DBSYS-term-project/front-end/expressions/target/expressions-9.0_2.13-9.0-SNAPSHOT.jar:/usr/local/opt/scala@2.13/idea/lib/scala-library.jar:../front-end/ast-factory/target/cypher-ast-factory-9.0-9.0-SNAPSHOT.jar:../front-end/javacc-parser/target/cypher-javacc-parser-9.0-9.0-SNAPSHOT.jar:../front-end/ast/target/ast-9.0_2.13-9.0-SNAPSHOT.jar:../front-end/util/target/util-9.0_2.13-9.0-SNAPSHOT.jar:../front-end/neo4j-ast-factory/target/opencypher-cypher-ast-factory-9.0_2.13-9.0-SNAPSHOT.jar:../jgrapht-1.5.1/lib/*:. Main "$@"
