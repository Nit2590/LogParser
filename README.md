# LogParser
A small utility to parse server logs and filter them into DB table 

Project Requirement ->

  Our custom-build server logs different events to a file named any  logfile.txt. Every event has 2 entries in the file
  - one entry when the event was started and another when the event was finished. The entries in the file have
  no specific order (a finish event could occur before a start event for a given id) Every line in the file is a
  JSON object containing the following event data:
  • id - the unique event identifier
  • state - whether the event was started or finished (can have values "STARTED" or "FINISHED"
  • timestamp - the timestamp of the event in milliseconds
  Application Server logs also have the following additional attributes:
  • type - type of log
  • host - hostname
  
  Input Server Logs:-
  
  {"id":"scsmbstgra", "state":"STARTED", "type":"APPLICATION_LOG", "host":"12345", "timestamp":1491377495212}
{"id":"scsmbstgra", "state":"FINISHED", "type":"APPLICATION_LOG", "host":"12345", "timestamp":1491377495217}

  
The program should:
• Take the path to any logfile.txt as an input argument
• Parse the contents of logfile.txt
• Flag any long events that take longer than 4ms
• Write the found event details to file-based HSQLDB (http://hsqldb.org/) in the working folder The
application should create a new table if necessary and store the following values:
• Event id
• Event duration
• Type and Host if applicable
• Alert (true if the event took longer than 4ms, otherwise false) 

Steps to Run the Utility-->
  
  Project Requirement
    1. Java 1.8
    2. Maven
    
1. Download all the dependencies mentinoned in the maven pom.xml
2. open mvn terminal/ Open windows cmd
3. pass command -> mvn exec:java ( to run default file)
4. pass command -> mvn  -Dexec.args="External file Path" (to run external files)
5. Output can be seen in the console log . The Table created and Enteries within table is visible in file ->\DBLogsEvent\EventTracker.script
6. Logs file an be found at location-> \log\EventParser.txt


