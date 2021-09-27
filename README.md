# Software-II

C195 Scheduling Application

An appointment management system for Western Governors University C195 course

Prerequisites:  
Scene Builder  
mySQL  
JDK 11   
JavaFX 11  
MySQL Driver V8.0.x  
 
Deployment:   

Change language to either french or english by removing or adding comment forward slashes  
from schedapp\SchedApp.java line 32. 

If the user does not want to type in the user ID and password each login attempt, remove 
the comment forward slashes from controller\LoginFormController.java line 72 and 73.

The application is very straighforward. Adding, updating, and removing appontments are all done on the
same screen. Customers screen is also the same. 

The default state of fields are set to add a new customer or appointment. The "Add" button will only
need to be pressed when switching from "Update" so that fields are cleared and that the proper SQL
statement is used when the "SAVE" button is pressed. 

Report Choice:

The report is a simple pie chart that represents the total number of customers for each day. The data
is from all the appoinments saved in the database. The chart serves to give the user a visual 
representation of the busiest and slowest days. 

Author: Audrey Hababag  
Application Version: 1.0.001  
Version Date: 25-OCT-2020  
IDE: Apache Netbeans IDE 12.1 updated to version NetBeans 8.2 Patch 2  
Java: 11.0.8; Java HotSpot(TM) 64-Bit Server VM 11.0.8+10-LTS  
JavaFX: 11.0.2 by Gluon licensed under GPL v2 + Classpath  
MySQL Driver: MySQL (Connector/j) 8.0.20   
Runtime: Java(TM) SE Runtime Environment 11.0.8+10-LTS  
System: Windows 10 version 10.0 running on amd64; Cp1252; en_US (nb)  


