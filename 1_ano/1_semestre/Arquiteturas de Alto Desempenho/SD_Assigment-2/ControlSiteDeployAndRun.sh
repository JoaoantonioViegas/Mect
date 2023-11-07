echo "Transfering data to the Control Site node."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'rm -rf Heist/*'
sshpass -f password scp controlSite.zip sd203@l040101-ws03.ua.pt:Heist

echo "Decompressing data sent to the Control Site node."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'cd Heist ; unzip -uq controlSite.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'cd Heist/controlSite ; java serverSide.main.ServerControlSite 22223 l040101-ws06.ua.pt 22226'

echo "Control Site Server shutdown."