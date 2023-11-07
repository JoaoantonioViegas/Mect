CODEBASE="file:///home/"$1"/test/Restaurant/dirThieves/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientOrdinaryThief localhost 22228