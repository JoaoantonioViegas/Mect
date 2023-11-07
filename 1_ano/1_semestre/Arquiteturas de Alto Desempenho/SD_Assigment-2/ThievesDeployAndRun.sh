echo "Transfering data to the Ordinary Thieves node."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'rm -rf Heist/*'
sshpass -f password scp ordinaryThieves.zip sd203@l040101-ws10.ua.pt:Heist

echo "Decompressing data sent to the Ordinary Thieves node."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'cd Heist ; unzip -uq ordinaryThieves.zip'

echo "Executing program at the Ordinary Thieves node."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'cd Heist/ordinaryThieves ; java clientSide.main.ClientOrdinaryThief l040101-ws02.ua.pt 22222 l040101-ws03.ua.pt 22223 l040101-ws01.ua.pt 22221 l040101-ws04.ua.pt 22224 l040101-ws05.ua.pt 22225 l040101-ws06.ua.pt 22226'

echo "Ordinary Thieves Client shutdown"