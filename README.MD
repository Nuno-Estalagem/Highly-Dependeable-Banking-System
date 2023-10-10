To run the project's servers open a terminal at the "SEC-BFTB directory".Then execute the bash that is located inside the SEC-BFTB directory using the command "./CreateServers.bash <byzantine clients>" where byzantine clients is the number of fails you wish to tolerate and wait for the servers to alert you that they are listening
The number of byzantine servers should be 2 as the tests were made for them to be 2
Afterwards, execute the bash move.bash using the command "./move.bash" to move the server certificates do the user project
To run the client's tests open a terminal in the "SEC-BFTB-Client" directory and execute the command "mvn clean test"
If you wish to run the client make sure you pass 2 arguments, the number of faults to tolerate and the total number of servers and it must be done after executing the move.bash script in order for the client to access each server certificate
To run the client use the command mvn exec:java -Dexec.args="<num byzantine servers><num servers>". For the code to run perfectly the perfect input would be the same number of byzantine servers and clients and the same number of servers as the ones created by the Create bash  

FINAL NOTE:
Even though all tests pass their respective assert equals, they still ended up outputing some "server died" prints. We suspect that it may be due to the way we developed the tests, as this does not happen when executting the client regularly. 
The error comes from generating a proof of work hash during the tests.
