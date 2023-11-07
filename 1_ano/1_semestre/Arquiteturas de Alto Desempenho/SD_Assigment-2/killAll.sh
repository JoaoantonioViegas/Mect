echo "Killing Museum java process."
sshpass -f password ssh sd203@l040101-ws01.ua.pt 'killall -9 -u sd203 java'
echo "Killing Concentration Site java process."
sshpass -f password ssh sd203@l040101-ws02.ua.pt 'killall -9 -u sd203 java'
echo "Killing Control Site java process."
sshpass -f password ssh sd203@l040101-ws03.ua.pt 'killall -9 -u sd203 java'
echo "Killing Assault Party 0 java process."
sshpass -f password ssh sd203@l040101-ws04.ua.pt 'killall -9 -u sd203 java'
echo "Killing Assault Party 1 java process."
sshpass -f password ssh sd203@l040101-ws05.ua.pt 'killall -9 -u sd203 java'
echo "Killing General Repository java process."
sshpass -f password ssh sd203@l040101-ws06.ua.pt 'killall -9 -u sd203 java'
echo "Killing Extra 1 java process."
sshpass -f password ssh sd203@l040101-ws07.ua.pt 'killall -9 -u sd203 java'
echo "Killing Extra 2 java process."
sshpass -f password ssh sd203@l040101-ws08.ua.pt 'killall -9 -u sd203 java'
echo "Killing Master Thief java process."
sshpass -f password ssh sd203@l040101-ws09.ua.pt 'killall -9 -u sd203 java'
echo "Killing Ordinary Thieves java process."
sshpass -f password ssh sd203@l040101-ws10.ua.pt 'killall -9 -u sd203 java'