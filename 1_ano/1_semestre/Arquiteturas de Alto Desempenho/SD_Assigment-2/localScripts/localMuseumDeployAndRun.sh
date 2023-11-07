echo "Executing Museum node."
cd /home/amaral/Heist/museum
java -cp "../genclass.jar:." serverSide.main.ServerMuseum 22130 127.0.0.1 22135
echo "Museum Server shutdown."