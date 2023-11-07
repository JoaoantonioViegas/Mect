xterm  -T "RMI registry" -hold -e "./RMIRegistryDeployAndRun.sh" &
sleep 10
xterm  -T "Registry" -hold -e "./RegistryDeployAndRun.sh" &
sleep 10
xterm -T "General Repository" -hold -e "./GeneralRepoDeployAndRun.sh" &
sleep 10
xterm -T "Concentration Site" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
sleep 3
xterm -T "Assault Party 0" -hold -e "./AssaultParty0DeployAndRun.sh" &
sleep 3
xterm -T "Assault Party 1" -hold -e "./AssaultParty1DeployAndRun.sh" &
sleep 3
xterm -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
sleep 3
xterm -T "Control Site" -hold -e "./ControlSiteDeployAndRun.sh" &
sleep 3
xterm -T "Thieves" -hold -e "./ThievesDeployAndRun.sh" &
sleep 3
xterm -T "Master Thief" -hold -e "./MasterDeployAndRun.sh" 
