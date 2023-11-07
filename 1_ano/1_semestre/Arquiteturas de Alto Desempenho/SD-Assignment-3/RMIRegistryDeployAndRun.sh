echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'killall -9 -u sd203 rmiregistry'

echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'rm -rf Heist/*'
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMIRegistry.zip sd203@l040101-ws08.ua.pt:Heist

echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'cd Heist ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'cd Heist/dirRMIRegistry ; cp interfaces/*.class /home/sd203/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/sd203'

echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'chmod +x *.sh ; ./set_rmiregistry_d.sh sd203 22228'

echo "RMIregistry node shutdown."