#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

compteur_b=0


for fichier in ./src/test/deca/context/valid/unprovided/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    rm -f "${fichier%.deca}".lis

    if test_context "$fichier" 2>&1 \
                    | head -n 1 | grep -q "$fichier"
    then
        echo "Echec de la verification contextuelle"
       exit 1
    else
        echo "verification effectuée"
    fi

   test_context "$fichier" > "${fichier%.deca}".lis

   # if diff "${fichier%.deca}".lis "${fichier%.deca}".refb
   # then
   #   echo "La vérification est correcte "
   # else
   #   echo "La verification ne correspond pas a celle souhaitée "
   #  exit 1
   # fi
   echo ""
   rm -f "${fichier%.deca}".lis

done

for fichier in ./src/test/deca/context/valid/unprovided/testObjet/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    rm -f "${fichier%.deca}".lis

    if test_context "$fichier" 2>&1 \
                    | head -n 1 | grep -q "$fichier"
    then
        echo "Echec de la verification contextuelle"
       exit 1
    else
        echo "verification effectuée"
    fi

    test_context "$fichier" > "${fichier%.deca}".lis

    # if diff "${fichier%.deca}".lis "${fichier%.deca}".refb
    # then
    #   echo "La vérification est correcte "
    # else
    #   echo "La verification ne correspond pas a celle souhaitée "
    #  exit 1
    # fi
    echo ""
    rm -f "${fichier%.deca}".lis

done

for fichier in ./src/test/deca/context/valid/unprovided/testObjet/fields/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    rm -f "${fichier%.deca}".lis

    if test_context "$fichier" 2>&1 \
                    | head -n 1 | grep -q "$fichier"
    then
        echo "Echec de la verification contextuelle"
       exit 1
    else
        echo "verification effectuée"
    fi

    test_context "$fichier" > "${fichier%.deca}".lis

    # if diff "${fichier%.deca}".lis "${fichier%.deca}".refb
    # then
    #   echo "La vérification est correcte "
    # else
    #   echo "La verification ne correspond pas a celle souhaitée "
    #  exit 1
    # fi
    echo ""
    rm -f "${fichier%.deca}".lis

done
for fichier in ./src/test/deca/context/valid/unprovided/testObjet/methods/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    rm -f "${fichier%.deca}".lis

    if test_context "$fichier" 2>&1 \
                    | head -n 1 | grep -q "$fichier"
    then
        echo "Echec de la verification contextuelle"
       exit 1
    else
        echo "verification effectuée"
    fi

    test_context "$fichier" > "${fichier%.deca}".lis

    # if diff "${fichier%.deca}".lis "${fichier%.deca}".refb
    # then
    #   echo "La vérification est correcte "
    # else
    #   echo "La verification ne correspond pas a celle souhaitée "
    #   exit 1
    # fi
    echo ""
    rm -f "${fichier%.deca}".lis

done

for fichier in ./src/test/deca/context/invalid/unprovided/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    if test_context "$fichier" 2>&1 \
                    | head -n 1 | grep -q "$fichier"
    then
        echo "Echec attendu de la verification contextuelle"
    else
        echo "succès inattendu de la verification"
        exit 1
    fi

    echo ""

done

for fichier in ./src/test/deca/context/invalid/unprovided/testObjet/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    if test_context "$fichier" 2>&1 \
                    | grep -q -e "$fichier:[0-9][0-9]*:"
    then
        echo "Echec attendu de la verification contextuelle"
    else
        echo "succès inattendu de la verification"
        exit 1
    fi

    echo ""

done

for fichier in ./src/test/deca/context/invalid/unprovided/testObjet/inheritance/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    if test_context "$fichier" 2>&1 \
                    | grep -q -e "$fichier:[0-9][0-9]*:"
    then
        echo "Echec attendu de la verification contextuelle"
    else
        echo "succès inattendu de la verification"
        exit 1
    fi

    echo ""

done

for fichier in ./src/test/deca/context/invalid/unprovided/testObjet/fields/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    if test_context "$fichier" 2>&1 \
                    | grep -q -e "$fichier:[0-9][0-9]*:"
    then
        echo "Echec attendu de la verification contextuelle"
    else
        echo "succès inattendu de la verification"
        exit 1
    fi

    echo ""

done

for fichier in ./src/test/deca/context/invalid/unprovided/testObjet/methods/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    if test_context "$fichier" 2>&1 \
                    | grep -q -e "$fichier:[0-9][0-9]*:"
    then
        echo "Echec attendu de la verification contextuelle"
    else
        echo "succès inattendu de la verification"
        exit 1
    fi

    echo ""

done

echo ""
echo "$compteur_b tests de verification contextuelle effectués"
echo ""
echo ""
