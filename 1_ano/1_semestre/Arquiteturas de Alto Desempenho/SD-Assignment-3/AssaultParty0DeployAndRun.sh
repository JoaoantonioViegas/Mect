echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws07.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the Assault Party node 0."
sshpass -f password ssh sd203@l040101-ws07.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws07.ua.pt 'rm -rf Heist/*'
sshpass -f password scp dirAssaultParty.zip sd203@l040101-ws07.ua.pt:Heist

echo "Decompressing data sent to the Assault Party node 0."
sshpass -f password ssh sd203@l040101-ws07.ua.pt 'cd Heist ; unzip -uq dirAssaultParty.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws07.ua.pt 'cd Heist/dirAssaultParty ; chmod +x *.sh ; ./party_com_d.sh sd203 22227 0 7'

echo "Assault Party Server 0 shutdown."
