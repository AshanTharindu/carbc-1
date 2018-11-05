#!/usr/bin/env bash

##directory where jar file is located
dir=./target

##jar file name
jar_name=carbc-0.0.1-jar-with-dependencies.jar

## Perform some rmvValidation on input arguments
if [ -z "$1" ]; then
        echo "Missing arguments, exiting.."
        echo "Usage : $0 arg1"
        exit 1
fi

java -jar $dir/$jar_name $1 $2