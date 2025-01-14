#  Bibliothèque MARS
    
## mars.jar
- Obtenu sur le
  [site](https://courses.missouristate.edu/KenVollmar/MARS/index.htm)
- __ MARS __ : MIPS Assembler and Runtime Simulator
- Version 4_5 ( exclu sources .java)
- Usage :
    - ` java -jar mars.jar              `  => Interface graphique
    - ` java -jar mars.jar fichier.mips `  => Exécution console

## Installation dans un dépôt local

Fait pour apparaître dans un dépôt Maven local avec la commande :

```
$ cd lib
$ ls
mars.jar 
$ mvn org.apache.maven.plugins:maven-install-plugin:3.1.2:install-file \
    -Dfile=./mars.jar \
    -DgroupId=courses.missouristate.edu \
    -DartifactId=KenVollmar.MARS \
    -Dversion=4.5 \
    -Dpackaging=jar \
    -DlocalRepositoryPath=./
```
