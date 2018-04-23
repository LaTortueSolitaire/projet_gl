#! /bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

compteur_g=0
compteur_lex=0

for i in ./src/test/deca/syntax/valid/unprovided/*.deca

do
  let compteur_lex++
  let compteur_g++
  echo "$i"


# Remplacer <executable> par test_synt ou test_lex
# ou test_context ou decac
  test_lex "$i" > "${i%.deca}".lis

done

for i in ./src/test/deca/syntax/invalid/unprovided/*.deca

do
  let compteur_lex++
  let compteur_g++
  echo "$i"

  if test_lex "$i" 2>&1 \
      | head -n 1 | grep -q "$i"
  then
      echo "Echec inattendu de test_lex"
      exit 1
  else
      echo "OK"
  fi

  rm -f "${i%.deca}".lis

done

echo "$compteur_lex tests lexicographiques ont été effectués"
echo " "
echo ""

synth.sh

echo ""
echo ""
