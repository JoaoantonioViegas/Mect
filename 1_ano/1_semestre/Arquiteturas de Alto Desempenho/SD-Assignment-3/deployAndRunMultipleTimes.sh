#loop for 100 times
for i in $(seq 1 100); do
    xterm -T "General Repository" -e "./GeneralRepoDeployAndRun.sh" &
    sleep 8
    xterm -T "Concentration Site" -e "./ConcentrationSiteDeployAndRun.sh" &
    sleep 1
    xterm -T "Assault Party 0" -e "./AssaultParty0DeployAndRun.sh" &
    sleep 1
    xterm -T "Assault Party 1" -e "./AssaultParty1DeployAndRun.sh" &
    sleep 1
    xterm -T "Museum" -e "./MuseumDeployAndRun.sh" &
    sleep 1
    xterm -T "Control Site" -e "./ControlSiteDeployAndRun.sh" &
    sleep 3
    xterm -T "Thieves" -e "./ThievesDeployAndRun.sh" &
    sleep 3
    xterm -T "Master Thief" -e "./MasterDeployAndRun.sh" 

    echo "Validating ..."

    echo "Downloading log.txt from the General Repository node."
    sshpass -f password scp sd203@l040101-ws06.ua.pt:Heist/dirGeneralRepository/log.txt /home/amaral/

    echo "Downloading total.txt from the Client node."
    sshpass -f password scp sd203@l040101-ws10.ua.pt:Heist/dirThieves/total.txt /home/amaral/

    #veryfing if the log.txt is equal to the total.txt
    line=$(tail -n 13 /home/amaral/log.txt | head -n 1)

    #read the 6th word from the line
    word=$(echo $line | awk '{print $6}')

    #read the content of the file /home/amaral/Heist/generalRepository/total.txt
    total=$(cat /home/amaral/total.txt)

    #compare the two values
    if [ $word -eq $total ]
    then
        echo "Heist successful!"
        echo $word "/" $total
    else
        echo "Test failed!"
        echo $word "/" $total
        exit 1
    fi

    sleep 5

    echo "End of iteration $i"
done
