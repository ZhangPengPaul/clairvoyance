#!/bin/bash
time  rm -rf lib/ target/
mkdir lib
cp  $PLAY_HOME/framework/play-1.4.1.jar lib/
time  mvn clean dependency:copy-dependencies -DoutputDirectory=lib
