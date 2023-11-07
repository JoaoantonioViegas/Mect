echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the Ordinary Thieves node."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'rm -rf Heist/*'
sshpass -f password scp dirThieves.zip sd203@l040101-ws10.ua.pt:Heist

echo "Decompressing data sent to the Ordinary Thieves node."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'cd Heist ; unzip -uq dirThieves.zip'

echo "Executing program at the Ordinary Thieves node."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'cd Heist/dirThieves ; chmod +x *.sh ; ./thieves_com_d.sh'

echo "Ordinary Thieves Client shutdown"