#!/bin/bash
ByzServersNum=$1
ServerPort=8080
NumberOfServers=$((ByzServersNum*3+1))
  for (( i = 0; i < NumberOfServers; i++ )); do
    RUN="mvn exec:java -Dexec.args=\"${ServerPort} ${NumberOfServers} ${ByzServersNum}\""
    gnome-terminal -- bash -c "mvn clean compile;${RUN};exec bash"
    ((ServerPort++))
  done
