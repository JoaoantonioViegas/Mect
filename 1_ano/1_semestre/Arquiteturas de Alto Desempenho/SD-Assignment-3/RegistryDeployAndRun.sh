echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the registry node."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'mkdir -p Heist'
sshpass -f password scp dirRegistry.zip sd203@l040101-ws08.ua.pt:Heist

echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'cd Heist ; unzip -uq dirRegistry.zip'

echo "Executing program at the registry node."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'cd Heist/dirRegistry ; chmod +x *.sh ; ./registry_com_d.sh sd203'

echo "Registry node shutdown."