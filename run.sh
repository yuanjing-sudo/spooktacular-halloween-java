#!/bin/sh
set -e
cd "$(dirname "$0")"
mkdir -p classes
javac -encoding UTF-8 -d classes $(find src -name '*.java')
java -cp classes spooktacular.game.TestEngine
java -cp classes spooktacular.app.AppTest
java -cp classes spooktacular.app.SpookyApp
