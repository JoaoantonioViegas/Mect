echo "Cleaning ports."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'killall -9 -u sd203 java'

echo "Transfering data to the Museum node."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'mkdir -p Heist'
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'rm -rf Heist/*'
sshpass -f password scp dirMuseum.zip sd203@l040101-ws01.ua.pt:Heist

echo "Decompressing data sent to the Museum node."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'cd Heist ; unzip -uq dirMuseum.zip'

echo "Executing program at the heist repository."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'cd Heist/dirMuseum ; chmod +x *.sh ; ./museum_com_d.sh sd203'

echo "Museum Server shutdown."