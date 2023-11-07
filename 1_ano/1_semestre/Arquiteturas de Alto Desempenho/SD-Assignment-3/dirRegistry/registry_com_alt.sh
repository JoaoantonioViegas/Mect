CODEBASE="file:///home/"$1"/Heist/dirRegistry/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerRegisterRemoteObject 22211 localhost 22228
