#loop for 100 times
for i in $(seq 1 100); do
    xterm -T "General Repository"  -e "./localGeneralRepoDeployAndRun.sh" &
    sleep 1
    xterm -T "Concentration Site"  -e "./localConcentrationSiteDeployAndRun.sh" &
    sleep 1
    xterm -T "Assault Party 0"  -e "./localAssaultParty0DeployAndRun.sh" &
    sleep 1
    xterm -T "Assault Party 1"  -e "./localAssaultParty1DeployAndRun.sh" &
    sleep 1
    xterm -T "Museum"  -e "./localMuseumDeployAndRun.sh" &
    sleep 1
    xterm -T "Control Site"  -e "./localControlSiteDeployAndRun.sh" &
    sleep 1
    xterm -T "Thieves"  -e "./localThievesDeployAndRun.sh" &
    sleep 1
    xterm -T "Master Thief"  -e "./localMasterDeployAndRun.sh" 

    echo "Validating ..."

    #read the 13th line from the end of the file /home/amaral/Heist/generalRepository/log.txt

    line=$(tail -n 13 /home/amaral/Heist/generalRepository/log.txt | head -n 1)

    #read the 6th word from the line
    word=$(echo $line | awk '{print $6}')

    #read the content of the file /home/amaral/Heist/generalRepository/total.txt
    total=$(cat /home/amaral/Heist/generalRepository/total.txt)

    #compare the two values
    if [ $word -eq $total ]
    then
        echo "Heist successful!"
        echo $word "==" $total
    else
        echo "Test failed!"
        echo $word "!=" $total
        exit 1
    fi

    sleep 5

    echo "End of iteration $i"
done
