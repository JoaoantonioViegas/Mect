#!/bin/bash
clear

#check if an argument was passed and set the limit of the loop to that, otherwise set it to 1000
if [ -z "$1" ]
  then
    limit=1000
  else
    limit=$1
fi


rm -f output.txt

touch output.txt

#loop 

for i in $(seq 1 $limit); do

    echo "round $i"

    javac main/Heist.java

    java main.Heist > dump.txt

    #read the 1st line of the dump.txt file
    line=$(sed -n 1p dump.txt)
    #get the 3rd word of the 6th line
    word=$(echo $line | cut -d ' ' -f 3)

    #read the 13th counting from the end of the log.txt file
    line2=$(tail -n 13 log.txt | head -n 1)

    #get the 6th word of the 13th line
    word2=$(echo $line2 | cut -d ' ' -f 6)

    #check if word is equal to word2
    if [ "$word" = "$word2" ]; then
        echo "round $i : $word $word2" >> output.txt
    else
        echo "mismatch in round $i"
        echo "round $i : $word - $word2" 
        exit 1
    fi

    #remove the dump.txt file
    rm dump.txt
done

echo "Completed with no errors!"

