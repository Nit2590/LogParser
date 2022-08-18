
package com.parser;

import com.parser.event.EventLog;
import com.parser.event.EventLogLine;
import com.parser.event.EventOpr;
import com.parser.utility.DBOperations;
import com.parser.utility.LogFileReader;
import com.parser.utility.Logging;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.testng.Assert.assertTrue;

public class LogParser_Test {
        static Logging log=new Logging("Log_Parser_Test");

      String File_Path_Empty = "src/test/resources/empty.txt";
      String File_Path_InValid="src/test";
      String File_Path_InCompleteEvents = "src/test/resources/Invalid_InputEvent.txt";
      String File_Path_TimeStamp_Missing="src/test/resources/TimeStamp_Missing.txt";
      String File_Path_ValidEvent="src/test/resources/valid_InputEvent.txt";

        @BeforeSuite(alwaysRun = true)
        public void beforeTest(){
            log.info("*******Unit Test Started *******");
        }

        @Test(description = "Read an empty file")
         void readDataFromInValidFile()
        {
            log.info("Test 'readDataFromInValidFile' Started");
             LogFileReader reader = new LogFileReader();
             Map<String, List<EventLogLine>> resultMap =reader.readEventsFromFile(File_Path_Empty) ;
             Assert.assertTrue(resultMap.isEmpty());
        }

        @Test(description = "Read from invalid path")
        void readDataFromInValidPath()
        {
            log.info("Test 'readDataFromInValidPath' Started");
            LogFileReader reader = new LogFileReader();
            Map<String, List<EventLogLine>> resultMap =reader.readEventsFromFile(File_Path_InValid) ;
            Assert.assertTrue(resultMap.isEmpty());
        }

        @Test(description="Read Incomplete/Missing Info events")
        void readDataFromInValidEventslog()
        {
            log.info("Test 'readDataFromInValidEventslog' Started");
            LogFileReader reader=new LogFileReader();
            Map<String, List<EventLogLine>> resultMap =reader.readEventsFromFile(File_Path_InCompleteEvents) ;
            Assert.assertEquals(String.valueOf(resultMap.get("scsmbstgrc").get(0).getState()),"UNKNOWNSTATE");
            Assert.assertEquals(String.valueOf(resultMap.get("scsmbstgrd").get(0).getState()),"UNKNOWNSTATE");

        }

        @Test(description ="Adding Events into EventLog list with one event's timestamp  missing")
        void enterInvalidTimeStamp() {
            log.info("Test 'enterInvalidTimeStamp' Started");
            LogFileReader reader = new LogFileReader();
            Map<String, List<EventLogLine>> resultMap =reader.readEventsFromFile(File_Path_TimeStamp_Missing) ;

            EventOpr opr=new EventOpr();
            List<EventLog> listlog=opr.filterEvents(resultMap);
            Assert.assertTrue(listlog.isEmpty()==true);
        }

        @Test(description = "Adding Events into DB and verify")
        void addEventsIntoDB() {
            log.info("Test 'addEventsIntoDB' Started");
            LogFileReader reader = new LogFileReader();
            Map<String, List<EventLogLine>> resultMap =reader.readEventsFromFile(File_Path_ValidEvent) ;

            EventOpr opr=new EventOpr();
            List<EventLog> listlog=opr.filterEvents(resultMap);
            DBOperations dbmanager = new DBOperations();
            //dropping table every time new input files is traversed
            dbmanager.dropHSQLDBLTable();

            // Creating the table
            dbmanager.createHSQLDBTable();
            dbmanager.insertEvents(listlog);
            List<String> event=dbmanager.readParticularEvent("scsmbstgra");
            dbmanager.stopHSQLDB();
            Assert.assertEquals(event.size(), 5);
            Assert.assertEquals(event.get(0), "scsmbstgra");
            Assert.assertEquals(event.get(2), "12345");
            Assert.assertEquals(event.get(3), "APPLICATION_LOG");
            Assert.assertEquals(event.get(4),"TRUE");

        }

    @AfterSuite(alwaysRun = true)
    public void afterTest(){
        log.info("*******Unit Test Completed *******");
    }

}



