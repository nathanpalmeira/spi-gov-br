#!/bin/bash
echo Commiting JAR
cp target/govbr-selos-mapper-provider-1.0.0.jar jar/govbr-selos-mapper-provider-1.0.0.jar
git add jar/govbr-selos-mapper-provider-1.0.0.jar
git commit -m "update jar"
git push
	