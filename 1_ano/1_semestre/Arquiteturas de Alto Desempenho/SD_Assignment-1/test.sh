#!/bin/bash
clear

javac main/Heist.java

java main.Heist > dump.txt

#read the 2nd line of the dump.txt file

line=$(head -n 2 dump.txt | tail -n 1)
#get the 1st word of the 2nd line
word=$(echo $line | cut -d ' ' -f 1)

#read the 13th line from the end of the log.txt file
line2=$(tail -n 13 log.txt | head -n 1)

#get the 6th word of the 13th line
word2=$(echo $line2 | cut -d ' ' -f 6)

#check if word is equal to word2
if [ "$word" = "$word2" ]; then
    echo "YES! Stole all the paintings ($word out of $word2)"
else
    echo "Uh oh, something went wrong ($word out of $word2)"
fi

#remove all .class files in the main, sharedRegions, entities and commInfra folders
find . -name "*.class" -type f -delete