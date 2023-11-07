echo "Transfering data to the Assault Party node 0."
sshpass -f password ssh sd203@l040101-ws04.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws04.ua.pt 'rm -rf Heist/*'
sshpass -f password scp assaultParty.zip sd203@l040101-ws04.ua.pt:Heist

echo "Decompressing data sent to the Assault Party node 0."
sshpass -f password ssh sd203@l040101-ws04.ua.pt 'cd Heist ; unzip -uq assaultParty.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws04.ua.pt 'cd Heist/assaultParty ; java serverSide.main.ServerAssaultParty 22224 l040101-ws06.ua.pt 22226'

echo "Assault Party Server 0 shutdown."
