echo "Transfering data to the Master Thief node."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'rm -rf Heist/*'
sshpass -f password scp masterThief.zip sd203@l040101-ws09.ua.pt:Heist

echo "Decompressing data sent to the Master Thief node."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'cd Heist ; unzip -uq masterThief.zip'

echo "Executing program at the Master Thief node."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'cd Heist/masterThief ; java clientSide.main.ClientMasterThief l040101-ws02.ua.pt 22222 l040101-ws03.ua.pt 22223 l040101-ws04.ua.pt 22224 l040101-ws05.ua.pt 22225 l040101-ws06.ua.pt 22226'

echo "Master Thief Client shutdown"