echo "Transfering data to the Control Site node."
cd /home/amaral/Heist/controlSite
java -cp "../genclass.jar:." serverSide.main.ServerControlSite 22132 127.0.0.1 22135
echo "Control Site Server shutdown."