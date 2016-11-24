#!/bin/bash
time  rm -rf lib/ target/
mkdir lib
cp  $PLAY_HOME/framework/play*.jar lib/
time  mvn clean dependency:copy-dependencies -DoutputDirectory=lib
