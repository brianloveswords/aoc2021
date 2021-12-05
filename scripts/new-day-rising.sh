#!/usr/bin/env bash

set -euo pipefail

function main {
    day="day$1"

    echo "New day: $day"
    mkdir -p "$day"/resources
    mkdir -p "$day"/src/{main,test}/scala

    mainfile="$day"/src/main/scala/Main.scala
    if [[ -f $mainfile ]]; then
        echo "File already exists: $mainfile"
    else
        echo "Creating file: $mainfile"
        cat >"$mainfile" <<<'@main def main() = println("cool")'
    fi

    testfile="$day"/src/test/scala/TestMain.scala
    if [[ -f $testfile ]]; then
        echo "File already exists: $testfile"
    else
        echo "Creating file: $testfile"
        cat >"$testfile" <<EOF
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestMain extends ScalaCheckEffectSuite:
  test("ok") {
      assert(true)
  }
EOF
    fi
}

main "$@"
