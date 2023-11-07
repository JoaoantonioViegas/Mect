echo "Transfering data to the Master Thief node."
cd /home/amaral/Heist/masterThief
java -cp "../genclass.jar:." clientSide.main.ClientMasterThief 127.0.0.1 22131 127.0.0.1 22132 127.0.0.1 22133 127.0.0.1 22134 127.0.0.1 22135
echo "Master Thief Server shutdown."