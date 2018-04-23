#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


for i in ./src/test/deca/syntax/valid/*.deca
do


      v=${i%.deca}.decp

      echo "$i"
      echo "$v"
done
