CODEBASE="file:///home/"$1"/Heist/dirConcentrationSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerConcentrationSite 22222 localhost 22228