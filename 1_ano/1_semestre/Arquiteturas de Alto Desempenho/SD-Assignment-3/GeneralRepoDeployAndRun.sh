echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the General Repository node."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'rm -rf Heist/*'
sshpass -f password scp dirGeneralRepository.zip sd203@l040101-ws06.ua.pt:Heist

echo "Decompressing data sent to the General Repository node."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'cd Heist ; unzip -uq dirGeneralRepository.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'cd Heist/dirGeneralRepository ; chmod +x *.sh ; ./repo_com_d.sh sd203'

echo "General Repository Server shutdown."

sshpass -f password ssh sd203@l040101-ws06.ua.pt 'cd Heist/dirGeneralRepository ; less log.txt'