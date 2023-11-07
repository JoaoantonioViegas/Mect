echo "Transfering data to the General Repository node."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'rm -rf Heist/*'
sshpass -f password scp generalRepository.zip sd203@l040101-ws06.ua.pt:Heist

echo "Decompressing data sent to the General Repository node."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'cd Heist ; unzip -uq generalRepository.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'cd Heist/generalRepository ; java serverSide.main.ServerGeneralRepo 22226'

echo "General Repository Server shutdown."

sshpass -f password ssh sd203@l040101-ws06.ua.pt 'cd Heist/generalRepository ; less log.txt'