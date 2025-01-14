#! /bin/bash

dir=$(basename $(pwd))

echo $dir

echo "Pour livrer, fermez votre IDE puis taper « entrée »"
read x
echo "Nettoyage avant livraison (1/2) : suppression des répertoires 'target'"
find . -type d -name "target" -exec rm -rf {} \; -print
echo "Nettoyage avant livraison (2/2) : suppression des fichiers mips"
find . -type f -name "*.mips" -exec rm -rf {} \; -print
echo "En cas d'utilisation de Git, effacer à la main le répertoire .git"
echo "Quand c'est fait, taper « entrée »"
read x

cd ..
echo "Construction de l'archive (taper « entrée »)"
read x
tar cfz $dir.tgz $dir
cd $dir

echo "Archive ../$dir.tgz construite"
echo "Visualisation du contenu de l'archive (taper « entrée »)"
read x
tar tf ../$dir.tgz
echo
echo "Prêt pour livrer ../$dir.tgz"
echo

exit
