CODEBASE="file:///home/"$1"/Heist/dirAssaultParty0/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerAssaultParty $2 localhost 22228 $3