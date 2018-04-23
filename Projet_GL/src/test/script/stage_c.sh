#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

compteur_c=0


# for fichier in ./src/test/deca/context/valid/unprovided/*.deca
# do
#     let compteur_c++
#     let compteur_g++
#
#     echo "$fichier"
#
#     rm -f "${fichier%.deca}".ass
#
#     if decac "$fichier" 2>&1 \
#                     | head -n 1 | grep -q -e "$fichier" -e "Exception"
#     then
#         echo "Echec de compilation"
#         # exit 1
#     else
#         echo "compilation effectuée"
#     fi
#
#     if [ ! -f "${fichier%.deca}".ass ]
#     then
#         echo "${fichier%.deca}.ass n'as pas été généré "
#     fi
#
#     if ima "${fichier%.deca}".ass 2>&1 \
#                     | head -n 1 | grep -q "IMA"
#     then
#         echo "Echec de l'interpretation par ima "
#         # exit 1
#     else
#         echo "Pas d'erreur a l'interpretation "
#     fi
#
#     if diff "${fichier%.deca}".ass "${fichier%.deca}".refc
#     then
#       echo "Le fichier .ass généré est correct "
#     else
#       echo "Le fichier .ass ne correspond pas  a la reference"
#       # exit 1
#     fi
#     echo ""
#     rm -f "${fichier%.deca}".ass
#
# done

# for fichier in ./src/test/deca/codegen/valid/unprovided/*.deca
# do
#     let compteur_c++
#     let compteur_g++
#
#     echo "$fichier"
#
#     rm -f "${fichier%.deca}".ass
#
#     if decac "$fichier" 2>&1 \
#                     | head -n 1 | grep -q -e "$fichier" -e "Exception"
#     then
#         echo "Echec de compilation"
#         # exit 1
#     else
#         echo "compilation effectuée"
#     fi
#
#     if [ ! -f "${fichier%.deca}".ass ]
#     then
#         echo "${fichier%.deca}.ass n'as pas été généré "
#     fi
#
#     if ima "${fichier%.deca}".ass 2>&1 \
#                     | head -n 1 | grep -q "IMA"
#     then
#         echo "Echec de l'interpretation par ima "
#         # exit 1
#     else
#         echo "Pas d'erreur a l'interpretation "
#     fi
#
#     if diff "${fichier%.deca}".ass "${fichier%.deca}".refc
#     then
#       echo "Le fichier .ass généré est correct "
#     else
#       echo "Le fichier .ass ne correspond pas  a la reference"
#       # exit 1
#     fi
#     echo ""
#     rm -f "${fichier%.deca}".ass
#
# done
#
# for fichier in ./src/test/deca/codegen/valid/unprovided/testObjets/*.deca
# do
#     let compteur_c++
#     let compteur_g++
#
#     echo "$fichier"
#
#     rm -f "${fichier%.deca}".ass
#
#     if decac "$fichier" 2>&1 \
#                     | head -n 1 | grep -q -e "$fichier" -e "Exception"
#     then
#         echo "Echec de compilation"
#         # exit 1
#     else
#         echo "compilation effectuée"
#     fi
#
#     if [ ! -f "${fichier%.deca}".ass ]
#     then
#         echo "${fichier%.deca}.ass n'as pas été généré "
#     fi
#
#     if ima "${fichier%.deca}".ass 2>&1 \
#                     | head -n 1 | grep -q "IMA"
#     then
#         echo "Echec de l'interpretation par ima "
#         # exit 1
#     else
#         echo "Pas d'erreur a l'interpretation "
#     fi
#
#     if diff "${fichier%.deca}".ass "${fichier%.deca}".refc
#     then
#       echo "Le fichier .ass généré est correct "
#     else
#       echo "Le fichier .ass ne correspond pas  a la reference"
#       # exit 1
#     fi
#     echo ""
#     rm -f "${fichier%.deca}".ass
#
# done

for fichier in ./src/test/deca/codegen/valid/unprovided/ok/*.deca
do
    let compteur_c++

    echo "$fichier"

    rm -f "${fichier%.deca}".ass

    if decac "$fichier" 2>&1 \
                    | head -n 1 | grep -q -e "$fichier" -e "Exception"
    then
        echo "Echec de compilation"
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
    else
        echo "Pas d'erreur a l'interpretation "
    fi

    ima "${fichier%.deca}".ass

    # if diff "${fichier%.deca}".ass "${fichier%.deca}".refc
    # then
    #   echo "Le fichier .ass généré est correct "
    # else
    #   echo "Le fichier .ass ne correspond pas  a la reference"
    #   # exit 1
    # fi
    echo ""
    echo ""
    rm -f "${fichier%.deca}".ass

done

for fichier in ./src/test/deca/codegen/invalid/*.deca
do
    let compteur_b++
    let compteur_g++

    echo "$fichier"

    rm -f "${fichier%.deca}".ass


    if decac "$fichier" 2>&1 \
                    | grep -q -e "$fichier:[0-9][0-9]*:"
    then
        echo "Echec attendu de la compilation"
    fi

    if [ ! -f "${fichier%.deca}".ass ]
    then
        echo "${fichier%.deca}.ass n'as pas été généré "
    fi

    if ima "${fichier%.deca}".ass 2>&1 \
                    | head -n 1 | grep -q -e "IMA" -e "Error"
    then
        echo "Echec attendu de l'interpretation par ima "
    else
        echo "Pas d'erreur a l'interpretation, c'est anormale "
    fi

    rm -f "${fichier%.deca}".ass


    echo ""

done

echo ""
echo "$compteur_c tests decac effectués"
echo ""
echo ""
