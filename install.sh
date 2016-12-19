#!/bin/bash


#Compile
javac src/container/*.java src/data_Package/*.java src/menu/*.java src/other/*.java
#Packing
jar cvfm vc.jar manifest ./src/*

