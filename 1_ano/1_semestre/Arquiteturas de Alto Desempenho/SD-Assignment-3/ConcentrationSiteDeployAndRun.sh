echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the Concentration Site node."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'rm -rf Heist/*'
sshpass -f password scp dirConcentrationSite.zip sd203@l040101-ws02.ua.pt:Heist

echo "Decompressing data sent to the Concentration Site node."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'cd Heist ; unzip -uq dirConcentrationSite.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'cd Heist/dirConcentrationSite ; chmod +x *.sh ; ./concentration_com_d.sh sd203'

echo "Concentration Site Server shutdown."