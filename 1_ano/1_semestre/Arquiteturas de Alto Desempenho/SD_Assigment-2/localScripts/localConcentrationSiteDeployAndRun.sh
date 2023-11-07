echo "Transfering data to the Concentration Site node."
cd /home/amaral/Heist/concentrationSite
java -cp "../genclass.jar:." serverSide.main.ServerConcentrationSite 22131 127.0.0.1 22135
echo "Concentration Site Server shutdown."
