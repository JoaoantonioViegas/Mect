xterm -T "General Repository" -hold -e "./GeneralRepoDeployAndRun.sh" &
sleep 1
xterm -T "Concentration Site" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
sleep 1
xterm -T "Assault Party 0" -hold -e "./AssaultParty0DeployAndRun.sh" &
sleep 1
xterm -T "Assault Party 1" -hold -e "./AssaultParty1DeployAndRun.sh" &
sleep 1
xterm -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
sleep 1
xterm -T "Control Site" -hold -e "./ControlSiteDeployAndRun.sh" &
sleep 1
xterm -T "Thieves" -hold -e "./ThievesDeployAndRun.sh" &
sleep 1
xterm -T "Master Thief" -hold -e "./MasterDeployAndRun.sh" 
