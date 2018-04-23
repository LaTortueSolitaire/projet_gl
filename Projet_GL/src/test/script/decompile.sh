#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

compteur_dec=0;

for i in ./src/test/deca/syntax/valid/*.deca
do
    let compteur_dec++
    let compteur_g++

    u=${i%.deca}.decp
    v=${u%.decp}.decp2

    rm -f "$u" "$v"

    echo "$i"

    decac -p "$i" > "$u"

    decac -p "$u" > "$v"

    if diff "$u" "$v"
    then
        echo "Succes attendu de decac -p "
    else
        echo "L'idempotence n'est pas vérifier "
        exit 1
    fi

    rm -f "$u" "$v"
done

for i in ./src/test/deca/codegen/valid/unprovided/ok/*.deca
do
    let compteur_dec++
    let compteur_g++

    u=${i%.deca}.decp
    v=${u%.decp}.decp2

    rm -f "$u" "$v"

    echo "$i"

    decac -p "$i" > "$u"

    decac -p "$u" > "$v"

    if diff "$u" "$v"
    then
        echo "Succes attendu de decac -p "
    else
        echo "L'idempotence n'est pas vérifier "
        exit 1
    fi

    rm -f "$u" "$v"
done

for i in ./src/test/deca/syntax/valid/testObjet/*.deca
do
    let compteur_dec++
    let compteur_g++

    u=${i%.deca}.decp
    v=${u%.decp}.decp2

    rm -f "$u" "$v"

    echo "$i"

    decac -p "$i" > "$u"

    decac -p "$u" > "$v"

    if diff "$u" "$v"
    then
        echo "Succes attendu de decac -p "
    else
        echo "L'idempotence n'est pas vérifier "
        exit 1
    fi

    rm -f "$u" "$v"
done

for i in ./src/test/deca/context/valid/unprovided/*.deca
do
    let compteur_dec++
    let compteur_g++

    u=${i%.deca}.decp
    v=${u%.decp}.decp2

    rm -f "$u" "$v"

    echo "$i"

    decac -p "$i" > "$u"

    decac -p "$u" > "$v"

    if diff "$u" "$v"
    then
        echo "Succes attendu de decac -p "
    else
        echo "L'idempotence n'est pas vérifier "
        exit 1
    fi

    rm -f "$u" "$v"

done

for i in ./src/test/deca/context/valid/unprovided/testObjet/*.deca
do
    let compteur_dec++
    let compteur_g++

    u=${i%.deca}.decp
    v=${u%.decp}.decp2

    rm -f "$u" "$v"

    echo "$i"

    decac -p "$i" > "$u"

    decac -p "$u" > "$v"

    if diff "$u" "$v"
    then
        echo "Succes attendu de decac -p "
    else
        echo "L'idempotence n'est pas vérifier "
        exit 1
    fi

    rm -f "$u" "$v"
done

for i in ./src/test/deca/context/valid/unprovided/testObjet/methods/*.deca
do
    let compteur_dec++
    let compteur_g++

    u=${i%.deca}.decp
    v=${u%.decp}.decp2

    rm -f "$u" "$v"

    echo "$i"

    decac -p "$i" > "$u"

    decac -p "$u" > "$v"

    if diff "$u" "$v"
    then
        echo "Succes attendu de decac -p "
    else
        echo "L'idempotence n'est pas vérifier "
        exit 1
    fi

    rm -f "$u" "$v"
done

for i in ./src/test/deca/context/valid/unprovided/testObjet/fields/*.deca
do
    let compteur_dec++
    let compteur_g++

    u=${i%.deca}.decp
    v=${u%.decp}.decp2

    rm -f "$u" "$v"

    echo "$i"

    decac -p "$i" > "$u"

    decac -p "$u" > "$v"

    if diff "$u" "$v"
    then
        echo "Succes attendu de decac -p "
    else
        echo "L'idempotence n'est pas vérifier "
        exit 1
    fi

    rm -f "$u" "$v"
done

echo ""
echo " $compteur_dec  tests d'idempotence effectués "

echo ""
echo ""
echo ""
