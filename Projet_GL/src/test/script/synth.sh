#! /bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


compteur=0

# exemple de définition d'une fonction
test_synt_invalide () {
    # $1 = premier argument.
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*" -e "Exception"
    then
        echo "Echec attendu pour test_synt."
    else
        echo "Succes inattendu de test_synt."
        exit 1
    fi
}

for cas_de_test in src/test/deca/syntax/invalid/unprovided/*.deca
do
    let compteur++
    let compteur_g++
    echo "$cas_de_test"
    test_synt_invalide "$cas_de_test"
done

for i in ./src/test/deca/syntax/valid/*.deca

do
  let compteur++
  let compteur_g++
  test_synt "$i" > "${i%.deca}".lis
  echo "$i"

  if test_synt "$i" 2>&1 | \
      grep -q -e ':[0-9][0-9]*:'
  then
      echo "Echec inattendu pour test_synt sur $i"
      exit 1
  else
      if diff "${i%.deca}".lis "${i%.deca}".ref
      then
        echo "Succes attendu de test_synt sur $i"
      fi
  fi

  rm -f "${i%.deca}".lis


done

for i in ./src/test/deca/syntax/valid/testObjet/*.deca

do
  let compteur++
  let compteur_g++
  test_synt "$i" > "${i%.deca}".lis
  echo "$i"

  if test_synt "$i" 2>&1 | \
      grep -q -e ':[0-9][0-9]*:'
  then
      echo "Echec inattendu pour test_synt sur $i"
      exit 1
  else
      if diff "${i%.deca}".lis "${i%.deca}".ref
      then
        echo "Succes attendu de test_synt sur $i"
      fi
  fi

  rm -f "${i%.deca}".lis


done

for i in ./src/test/deca/syntax/valid/unprovided/*.deca

do
  let compteur++
  let compteur_g++
  test_synt "$i" > "${i%.deca}".lis
  echo "$i"

  if test_synt "$i" 2>&1 | \
      grep -q -e ':[0-9][0-9]*:'
  then
      echo "Echec inattendu pour test_synt sur $i"
      exit 1
  else
      if diff "${i%.deca}".lis "${i%.deca}".ref
      then
        echo "Succes attendu de test_synt sur $i"
      fi
  fi

  rm -f "${i%.deca}".lis


done

for i in ./src/test/deca/codegen/valid/unprovided/ok/*.deca

do
  let compteur++
  let compteur_g++
  test_synt "$i" > "${i%.deca}".lis
  echo "$i"

  if test_synt "$i" 2>&1 | \
      grep -q -e ':[0-9][0-9]*:'
  then
      echo "Echec inattendu pour test_synt sur $i"
      exit 1
  else
      if diff "${i%.deca}".lis "${i%.deca}".ref
      then
        echo "Succes attendu de test_synt sur $i"
      fi
  fi

  rm -f "${i%.deca}".lis


done

for i in ./src/test/deca/context/valid/unprovided/*.deca

do
  let compteur++
  let compteur_g++
  test_synt "$i" > "${i%.deca}".lis
  echo "$i"

  if test_synt "$i" 2>&1 | \
      grep -q -e ':[0-9][0-9]*:'
  then
      echo "Echec inattendu pour test_synt sur $i"
      exit 1
  else
      if diff "${i%.deca}".lis "${i%.deca}".ref
      then
        echo "Succes attendu de test_synt sur $i"
      fi
  fi

  rm -f "${i%.deca}".lis


done

echo " $compteur tests syntaxiques ont été effectués"
