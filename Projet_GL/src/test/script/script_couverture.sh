#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


mvn cobertura:clean
mvn cobertura:instrument

echo "script stage_a.sh"
stage_A.sh

echo "script stage_c.sh"
stage_c.sh

echo "script stage_b.sh"
stage_b.sh

echo "script decompile.sh"
decompile.sh

echo "option verif"
option_v.sh

echo "option -d"
option_d.sh

echo "option -r"
option_r.sh

cobertura-report.sh
firefox target/site/cobertura/index.html
