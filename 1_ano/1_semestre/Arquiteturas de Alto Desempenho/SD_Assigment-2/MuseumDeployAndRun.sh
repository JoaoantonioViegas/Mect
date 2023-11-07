echo "Transfering data to the Museum node."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'rm -rf Heist/*'
sshpass -f password scp museum.zip sd203@l040101-ws01.ua.pt:Heist

echo "Decompressing data sent to the Museum node."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'cd Heist ; unzip -uq museum.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'cd Heist/museum ; java serverSide.main.ServerMuseum 22221 l040101-ws06.ua.pt 22226'

echo "Museum Server shutdown."