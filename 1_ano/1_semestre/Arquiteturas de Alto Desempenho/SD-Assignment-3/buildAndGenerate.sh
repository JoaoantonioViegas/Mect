echo "Compiling source code."
javac -source 8 -target 8 -cp ./genclass.jar */*.java */*/*.java


echo "Distributing intermidiate code to the different execution environments."

echo "RMI Registry"
rm -rf dirRMIRegistry/interfaces
mkdir -p dirRMIRegistry/interfaces
cp interfaces/*.class dirRMIRegistry/interfaces


echo "Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces
mkdir -p dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects dirRegistry/interfaces
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces


echo "General Repository of Information."
rm -rf dirGeneralRepository/interfaces dirGeneralRepository/serverSide dirGeneralRepository/clientSide
mkdir -p    dirGeneralRepository/serverSide \
            dirGeneralRepository/serverSide/main \
            dirGeneralRepository/serverSide/objects \
            dirGeneralRepository/interfaces \
            dirGeneralRepository/clientSide \
            dirGeneralRepository/clientSide/entities
cp serverSide/main/SimulPar.class serverSide/main/ServerGeneralRepo.class dirGeneralRepository/serverSide/main
cp serverSide/objects/GeneralRepo.class dirGeneralRepository/serverSide/objects
cp interfaces/Register.class interfaces/GeneralRepoInterface.class dirGeneralRepository/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirGeneralRepository/clientSide/entities


echo "Assault Party"
rm -rf dirAssaultParty/interfaces dirAssaultParty/serverSide dirAssaultParty/clientSide dirAssaultParty/commInfra
mkdir -p    dirAssaultParty/serverSide \
            dirAssaultParty/serverSide/main \
            dirAssaultParty/serverSide/objects \
            dirAssaultParty/interfaces \
            dirAssaultParty/clientSide \
            dirAssaultParty/clientSide/entities \
            dirAssaultParty/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerAssaultParty.class dirAssaultParty/serverSide/main
cp serverSide/objects/AssaultParty.class dirAssaultParty/serverSide/objects
cp interfaces/* dirAssaultParty/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirAssaultParty/clientSide/entities
cp commInfra/*.class dirAssaultParty/commInfra


echo "Concentration Site"
rm -rf dirConcentrationSite/interfaces dirConcentrationSite/serverSide dirConcentrationSite/clientSide dirConcentrationSite/commInfra
mkdir -p    dirConcentrationSite/serverSide \
            dirConcentrationSite/serverSide/main \
            dirConcentrationSite/serverSide/objects \
            dirConcentrationSite/interfaces \
            dirConcentrationSite/clientSide \
            dirConcentrationSite/clientSide/entities \
            dirConcentrationSite/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerConcentrationSite.class dirConcentrationSite/serverSide/main
cp serverSide/objects/ConcentrationSite.class dirConcentrationSite/serverSide/objects
cp interfaces/* dirConcentrationSite/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirConcentrationSite/clientSide/entities
cp commInfra/*.class dirConcentrationSite/commInfra


echo "Control Site"
rm -rf dirControlSite/interfaces dirControlSite/serverSide dirControlSite/clientSide dirControlSite/commInfra
mkdir -p    dirControlSite/serverSide \
            dirControlSite/serverSide/main \
            dirControlSite/serverSide/objects \
            dirControlSite/interfaces \
            dirControlSite/clientSide \
            dirControlSite/clientSide/entities \
            dirControlSite/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerControlSite.class dirControlSite/serverSide/main
cp serverSide/objects/ControlSite.class dirControlSite/serverSide/objects
cp interfaces/* dirControlSite/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirControlSite/clientSide/entities
cp commInfra/*.class dirControlSite/commInfra


echo "Museum"
rm -rf dirMuseum/interfaces dirMuseum/serverSide dirMuseum/clientSide dirMuseum/commInfra
mkdir -p    dirMuseum/serverSide \
            dirMuseum/serverSide/main \
            dirMuseum/serverSide/objects \
            dirMuseum/interfaces \
            dirMuseum/clientSide \
            dirMuseum/clientSide/entities \
            dirMuseum/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerMuseum.class dirMuseum/serverSide/main
cp serverSide/objects/Museum.class dirMuseum/serverSide/objects
cp interfaces/* dirMuseum/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirMuseum/clientSide/entities
cp commInfra/*.class dirMuseum/commInfra


echo "Thieves"
rm -rf dirThieves/serverSide dirThieves/clientSide dirThieves/interfaces
mkdir -p    dirThieves/serverSide \
            dirThieves/serverSide/main \
            dirThieves/interfaces \
            dirThieves/clientSide \
            dirThieves/clientSide/entities \
            dirThieves/clientSide/main
cp serverSide/main/SimulPar.class dirThieves/serverSide/main
cp clientSide/main/ClientOrdinaryThief.class dirThieves/clientSide/main
cp clientSide/entities/OrdinaryThief.class clientSide/entities/OrdinaryThiefStates.class dirThieves/clientSide/entities
cp interfaces/* dirThieves/interfaces

echo "Master Thief"
rm -rf dirMaster/serverSide dirMaster/clientSide dirMaster/interfaces
mkdir -p    dirMaster/serverSide \
            dirMaster/serverSide/main \
            dirMaster/interfaces \
            dirMaster/clientSide \
            dirMaster/clientSide/entities \
            dirMaster/clientSide/main
cp serverSide/main/SimulPar.class dirMaster/serverSide/main
cp clientSide/main/ClientMasterThief.class dirMaster/clientSide/main
cp clientSide/entities/MasterThief.class clientSide/entities/MasterThiefStates.class dirMaster/clientSide/entities
cp interfaces/* dirMaster/interfaces


echo "Compressing execution environments."

echo "RMI registry"
rm -f dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry

echo "Register Remote Objects"
rm -f dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry

echo "General Repository"
rm -f dirGeneralRepository.zip
zip -rq dirGeneralRepository.zip dirGeneralRepository

echo "Assault Party"
rm -f dirAssaultParty.zip
zip -rq dirAssaultParty.zip dirAssaultParty

echo "Concentration Site"
rm -f dirConcentrationSite.zip
zip -rq dirConcentrationSite.zip dirConcentrationSite

echo "Control Site"
rm -f dirControlSite.zip
zip -rq dirControlSite.zip dirControlSite

echo "Museum"
rm -f dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum

echo "Thieves"
rm -f dirThieves.zip
zip -rq dirThieves.zip dirThieves

echo "Master Thief"
rm -f dirMaster.zip
zip -rq dirMaster.zip dirMaster
