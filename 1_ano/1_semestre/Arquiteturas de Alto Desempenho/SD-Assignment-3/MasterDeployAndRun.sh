echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the Master Thief node."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'rm -rf Heist/*'
sshpass -f password scp dirMaster.zip sd203@l040101-ws09.ua.pt:Heist

echo "Decompressing data sent to the Master Thief node."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'cd Heist ; unzip -uq dirMaster.zip'

echo "Executing program at the Master Thief node."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'cd Heist/dirMaster ; chmod +x *.sh ; ./master_com_d.sh'

echo "Master Thief Client shutdown"