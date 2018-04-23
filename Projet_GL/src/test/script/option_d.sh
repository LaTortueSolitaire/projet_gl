#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

compteur_r=0


for fichier in ./src/test/deca/codegen/valid/unprovided/ok/*.deca
do
    let compteur_r++

    echo "$fichier"

    rm -f "${fichier%.deca}".ass

    if decac -d -d 4 "$fichier" 2>&1 \
                    | head -n 1 | grep -q -e "$fichier" -e "Exception"
    then
        echo "Echec de compilation"
        # exit 1
    else
        echo "compilation effectuée"
    fi

    if [ ! -f "${fichier%.deca}".ass ]
    then
        echo "${fichier%.deca}.ass n'as pas été généré "
    fi

    if ima "${fichier%.deca}".ass 2>&1 \
                    | head -n 1 | grep -q "IMA"
    then
        echo "Echec de l'interpretation par ima "
        # exit 1
    else
        echo "Pas d'erreur a l'interpretation "
    fi

    ima "${fichier%.deca}".ass


    echo ""
    echo ""
    rm -f "${fichier%.deca}".ass

done

echo ""
echo "$compteur_r tests decac avec l'option -r 4"
echo ""
echo ""
