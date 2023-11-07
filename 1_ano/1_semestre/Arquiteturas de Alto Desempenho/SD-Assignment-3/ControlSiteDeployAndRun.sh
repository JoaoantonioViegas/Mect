echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the Control Site node."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'rm -rf Heist/*'
sshpass -f password scp dirControlSite.zip sd203@l040101-ws03.ua.pt:Heist

echo "Decompressing data sent to the Control Site node."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'cd Heist ; unzip -uq dirControlSite.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'cd Heist/dirControlSite ; chmod +x *.sh ; ./control_com_d.sh sd203'

echo "Control Site Server shutdown."