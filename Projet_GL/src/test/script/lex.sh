#!/bin/sh

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

#Tests pour voir qu'il reperent bien tous les jetons souhaités

for i in ./src/test/deca/syntax/invalid/test_lexico/verifie/*.deca

do

echo "$i"

if test_lex "$i" 2>&1 \
    | head -n 1 | grep -q "$i"
then
    echo "Echec inattendu de test_lex"
    exit 1
else
    echo "OK"
fi
# Remplacer <executable> par test_synt ou test_lex
# ou test_context ou decac
test_lex "$i" > "${i%.deca}".lis

done




#Tests pour reperer des erreurs lexicographiques

# for i in ./src/test/deca/syntax/invalid/test_lexico/erroner/*.deca
#
# do
#
# echo "$i"
#
# if test_lex "$i" 2>&1 \
#     | grep -q -e "$i"
# then
#     echo "Echec attendu pour test_lex"
# else
#     echo "Erreur non detectee par test_lex pour chaine_incomplete.deca"
#     exit 1
# fi
#
# test_lex "$i" > "${i%.deca}".lis
#
# done
