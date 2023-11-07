echo "Transfering data to the General Repository node."
cd /home/amaral/Heist/generalRepository
java -cp "../genclass.jar:." serverSide.main.ServerGeneralRepo 22135
echo "General Repository Server shutdown."