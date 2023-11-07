CODEBASE="file:///home/"$1"/Heist/dirControlSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerControlSite 22223 localhost 22228