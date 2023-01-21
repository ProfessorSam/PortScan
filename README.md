## Port Sanner

This a simple multithreaded Portscanner implemented in Java. This does not come in any way close 
to Nmap in any way. 

### How to use

Compile the project with java 17 and maven. Use ``mvn clean install`` at the project root where 
the pom.xml is located. Next go to the target directory and execute the Main class via ``java -cp 
PortScan-1.0-SNAPSHOT.jar com.github.professorSam.portScan.PortScan``

1. Set the host to can ``host <host>``
2. (Optional) set the thread count ``threads <count>``. More threads = faster scan, but easier 
   to detect.
3. Scan the host ``scan``
4. Print out the results ``print``
5. Exit with ``exit``

***Attention, this project is only a demonstration and should never be used without permission!***