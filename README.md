# veraPDF-library-GUI

To build and run veraPDF PDF/A Conformance Checker GUI please follow next steps :

1) Clone repository into local system

```
git clone https://github.com/veraPDF/veraPDF-library-GUI
```

2) Build project using maven (from veraPDF-library-GUI directory)

```
mvn clean install appassembler:assemble
```

3) Launch the GUI

Unix / MacOS:
```
./target/appassembler/bin/VeraPDF\ validation\ GUI 
```

Windows:
```
"./target/appassembler/bin/VeraPDF validation GUI.bat"
```

