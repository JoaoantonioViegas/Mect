#loop for 100 times
for i in $(seq 1 300); do
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "General Repository"   -e "./localGeneralRepoDeployAndRun.sh" &
    sleep .1
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "Concentration Site"  -e "./localConcentrationSiteDeployAndRun.sh" &
    sleep .1
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "Assault Party 0"  -e "./localAssaultParty0DeployAndRun.sh" &
    sleep .1
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "Assault Party 1"  -e "./localAssaultParty1DeployAndRun.sh" &
    sleep .1
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "Museum"  -e "./localMuseumDeployAndRun.sh" &
    sleep .1
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "Control Site"  -e "./localControlSiteDeployAndRun.sh" &
    sleep .1
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "Thieves"  -e "./localThievesDeployAndRun.sh" &
    sleep .1
    xterm -bg '#2E3436' -fg '#D3D7CF' -T "Master Thief"  -e "./localMasterDeployAndRun.sh" 

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

    sleep 2

    echo "End of iteration $i"
done
