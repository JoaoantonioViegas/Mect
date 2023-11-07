echo "Transfering data to the Concentration Site node."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'rm -rf Heist/*'
sshpass -f password scp concentrationSite.zip sd203@l040101-ws02.ua.pt:Heist

echo "Decompressing data sent to the Concentration Site node."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'cd Heist ; unzip -uq concentrationSite.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'cd Heist/concentrationSite ; java serverSide.main.ServerConcentrationSite 22222 l040101-ws06.ua.pt 22226'

echo "Concentration Site Server shutdown."