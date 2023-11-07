CODEBASE="file:///home/"$1"/Heist/dirGeneralRepository/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerGeneralRepo 22226 localhost 22228