CODEBASE="http://l040101-ws0"$4".ua.pt/"$1"/classes/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerAssaultParty $2 l040101-ws08.ua.pt 22228 $3