echo "Compiling source code."

javac -source 8 -target 8 -cp ./genclass.jar */*.java */*/*.java

echo "Distributing intermidiate code to the different execution environments."

echo "General Repository of Information."

rm -rf generalRepository
mkdir -p    generalRepository \
            generalRepository/serverSide generalRepository/serverSide/main \
            generalRepository/serverSide/entities generalRepository/serverSide/sharedRegions \
            generalRepository/clientSide generalRepository/clientSide/entities \
            generalRepository/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerGeneralRepo.class generalRepository/serverSide/main
cp serverSide/entities/GeneralRepoClientProxy.class generalRepository/serverSide/entities
cp serverSide/sharedRegions/GeneralRepo.class serverSide/sharedRegions/GeneralRepoInterface.class generalRepository/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class generalRepository/clientSide/entities
cp commInfra/Message.class commInfra/MessageException.class commInfra/MessageType.class commInfra/ServerCom.class generalRepository/commInfra

echo "Assault Party"
rm -rf assaultParty
mkdir -p    assaultParty \
            assaultParty/serverSide assaultParty/serverSide/main \
            assaultParty/serverSide/entities assaultParty/serverSide/sharedRegions \
            assaultParty/clientSide assaultParty/clientSide/entities assaultParty/clientSide/stubs \
            assaultParty/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerAssaultParty.class assaultParty/serverSide/main
cp serverSide/entities/AssaultPartyClientProxy.class assaultParty/serverSide/entities
cp serverSide/sharedRegions/AssaultParty.class serverSide/sharedRegions/AssaultPartyInterface.class serverSide/sharedRegions/GeneralRepoInterface.class assaultParty/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class assaultParty/clientSide/entities
cp clientSide/stubs/GeneralRepoStub.class assaultParty/clientSide/stubs
cp commInfra/*.class assaultParty/commInfra

echo "Concentration Site"
rm -rf concentrationSite
mkdir -p    concentrationSite \
            concentrationSite/serverSide concentrationSite/serverSide/main \
            concentrationSite/serverSide/entities concentrationSite/serverSide/sharedRegions \
            concentrationSite/clientSide concentrationSite/clientSide/entities concentrationSite/clientSide/stubs \
            concentrationSite/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerConcentrationSite.class concentrationSite/serverSide/main
cp serverSide/entities/ConcentrationSiteClientProxy.class concentrationSite/serverSide/entities
cp serverSide/sharedRegions/ConcentrationSite.class serverSide/sharedRegions/ConcentrationSiteInterface.class serverSide/sharedRegions/GeneralRepoInterface.class concentrationSite/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class concentrationSite/clientSide/entities
cp clientSide/stubs/GeneralRepoStub.class concentrationSite/clientSide/stubs
cp commInfra/*.class concentrationSite/commInfra

echo "Control Site"
rm -rf controlSite
mkdir -p    controlSite \
            controlSite/serverSide controlSite/serverSide/main \
            controlSite/serverSide/entities controlSite/serverSide/sharedRegions \
            controlSite/clientSide controlSite/clientSide/entities controlSite/clientSide/stubs \
            controlSite/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerControlSite.class controlSite/serverSide/main
cp serverSide/entities/ControlSiteClientProxy.class controlSite/serverSide/entities
cp serverSide/sharedRegions/ControlSite.class serverSide/sharedRegions/ControlSiteInterface.class serverSide/sharedRegions/GeneralRepoInterface.class controlSite/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class controlSite/clientSide/entities
cp clientSide/stubs/GeneralRepoStub.class controlSite/clientSide/stubs
cp commInfra/*.class controlSite/commInfra

echo "Museum"
rm -rf museum
mkdir -p    museum \
            museum/serverSide museum/serverSide/main \
            museum/serverSide/entities museum/serverSide/sharedRegions \
            museum/clientSide museum/clientSide/entities museum/clientSide/stubs \
            museum/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerMuseum.class museum/serverSide/main
cp serverSide/entities/MuseumClientProxy.class museum/serverSide/entities
cp serverSide/sharedRegions/Museum.class serverSide/sharedRegions/MuseumInterface.class serverSide/sharedRegions/GeneralRepoInterface.class museum/serverSide/sharedRegions
cp clientSide/entities/OrdinaryThiefStates.class clientSide/entities/OrdinaryThiefCloning.class museum/clientSide/entities
cp clientSide/stubs/GeneralRepoStub.class museum/clientSide/stubs
cp commInfra/*.class museum/commInfra

echo "Thieves"
rm -rf ordinaryThieves
mkdir -p    ordinaryThieves \
            ordinaryThieves/serverSide ordinaryThieves/serverSide/main \
            ordinaryThieves/clientSide ordinaryThieves/clientSide/entities ordinaryThieves/clientSide/main \
            ordinaryThieves/commInfra ordinaryThieves/clientSide/stubs

cp serverSide/main/SimulPar.class ordinaryThieves/serverSide/main
cp clientSide/main/ClientOrdinaryThief.class ordinaryThieves/clientSide/main
cp clientSide/entities/OrdinaryThief.class clientSide/entities/OrdinaryThiefStates.class ordinaryThieves/clientSide/entities
cp clientSide/stubs/GeneralRepoStub.class clientSide/stubs/AssaultPartyStub.class clientSide/stubs/ControlSiteStub.class clientSide/stubs/ConcentrationSiteStub.class clientSide/stubs/MuseumStub.class ordinaryThieves/clientSide/stubs
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class ordinaryThieves/commInfra

echo "Master Thief"
rm -rf masterThief
mkdir -p    masterThief \
            masterThief/serverSide masterThief/serverSide/main \
            masterThief/clientSide masterThief/clientSide/entities masterThief/clientSide/main \
            masterThief/commInfra masterThief/clientSide/stubs

cp serverSide/main/SimulPar.class masterThief/serverSide/main
cp clientSide/main/ClientMasterThief.class masterThief/clientSide/main
cp clientSide/entities/MasterThief.class clientSide/entities/MasterThiefStates.class masterThief/clientSide/entities
cp clientSide/stubs/GeneralRepoStub.class clientSide/stubs/AssaultPartyStub.class clientSide/stubs/ControlSiteStub.class clientSide/stubs/ConcentrationSiteStub.class masterThief/clientSide/stubs
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class masterThief/commInfra



echo "Compressing execution environments."
echo "General Repository"
rm -f generalRepository.zip
zip -rq generalRepository.zip generalRepository

echo "Assault Party"
rm -f assaultParty.zip
zip -rq assaultParty.zip assaultParty

echo "Concentration Site"
rm -f concentrationSite.zip
zip -rq concentrationSite.zip concentrationSite

echo "Control Site"
rm -f controlSite.zip
zip -rq controlSite.zip controlSite

echo "Museum"
rm -f museum.zip
zip -rq museum.zip museum

echo "Thieves"
rm -f ordinaryThieves.zip
zip -rq ordinaryThieves.zip ordinaryThieves

echo "Master Thief"
rm -f masterThief.zip
zip -rq masterThief.zip masterThief
