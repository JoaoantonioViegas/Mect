echo "Transfering data to the Ordinary Thieves node."
cd /home/amaral/Heist/ordinaryThieves
java -cp "../genclass.jar:." clientSide.main.ClientOrdinaryThief 127.0.0.1 22131 127.0.0.1 22132 127.0.0.1 22130 127.0.0.1 22133 127.0.0.1 22134 127.0.0.1 22135
echo "Ordinary Thief Server shutdown."