# veraPDF-library
veraPDF PDF/A validation library

Steps to run validation : 

1) Clone repository in your local environment
```
git clone https://github.com/veraPDF/veraPDF-library
```
2) Build the project
```
cd {project directory}
mvn clean install
```
3) Run validation
```
cd {project directory}/cli/target
java -jar vera-cli-1.0-SNAPSHOT-jar-with-dependencies.jar verapdf --validate --input "{path to input pdf file}" --profile "{path to validation profile}" --output "{path to saved report}"
```
