# DataMigrationProject
This project takes data from a csv file and uploads it to 2 database tables, 1 for valid records and a second for duplicates/error records.

This Project used Threading, reading files, and database querys using jdbc.

Employees.csv has 10000 records with 57 dupelicate records.
EmployeeRecordsLarge.csv has 65499 records with 0 duplicate records.

Performance Tests:

Time taken for employees 
Total time : 3.238 seconds,
Read time : 8.5ms,
Migration time : 3.231 seconds.

Time taken for employeeRecordsLarge
Total time : 12.9057s,
Read time : 39ms,
Migration time : 12.9053 seconds.

To use the program you have to input 1 to create new database or any other number to add to existing.
To select which file press 1 for employees and any other number for EmployeeRecordsLarge.

It will then add all the correct records to the employees table and all the duplicates/errors to Employeeserrors table
