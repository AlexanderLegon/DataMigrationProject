package com.sparta.alexanderlegon.model;

import com.sparta.alexanderlegon.util.DateChanger;

import java.io.FileReader;
import java.sql.*;
import java.util.List;
import java.util.Properties;


public class EmployeesDAO {

    private static Properties properties= new Properties();
    private final String URL = "jdbc:mysql://localhost:3306/dataMigrationProject?serverTimezone=GMT";
    private Connection connection;

    private final String updateEmployee = "INSERT INTO employees(employeeId, prefix, firstName, middleInitial, lastName, gender, email, birthDate, joinDate, salary) " +
                                          "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
    private final String updateErrEmployee = "INSERT INTO employeesErrors( employeeId, prefix, firstName, middleInitial, lastName, gender, email, birthDate, joinDate, salary) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String checkTable = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dataMigrationProject' AND TABLE_NAME = 'employees';";
    private final String checkErrTable = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dataMigrationProject' AND TABLE_NAME = 'employeesErrors';";

    private final String createTable = "CREATE TABLE employees (employeeId int(7), prefix varchar(8), firstName varchar(20), middleInitial varchar(1), lastName varchar(20), " +
                                       "gender varchar(6), email varchar(50), birthDate DATE, joinDate DATE, salary int(7), PRIMARY KEY (EmployeeId));";
    private final String createErrTable = "CREATE TABLE employeesErrors (errNo int(7) NOT NULL AUTO_INCREMENT, employeeId int(7), prefix varchar(8), firstName varchar(20), middleInitial varchar(1), lastName varchar(20), " +
            "gender varchar(6), email varchar(50), birthDate DATE, joinDate DATE, salary int(7), PRIMARY KEY (errNo));";

    private final String dropTable = "DROP TABLE employees;";
    private final String dropErrTable = "DROP TABLE employeesErrors;";

    private final String countEmpTable = "SELECT COUNT(*) FROM employees;";
    private final String countErrTable = "SELECT COUNT(*) FROM employeesErrors;";

    private Connection connectToDatabase(){
        try {
            properties.load(new FileReader("resources/login.properties"));
            connection= DriverManager.getConnection(URL,properties.getProperty("username"),properties.getProperty("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void createTable(){
        try{
            Statement statement=connectToDatabase().createStatement();
            ResultSet resultSet = statement.executeQuery(checkTable);
            if(resultSet.next() == false){
                Statement create=connectToDatabase().createStatement();
                create.executeUpdate(createTable);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable(int dropOld){
        try{
            Statement statement=connectToDatabase().createStatement();
            ResultSet resultSet = statement.executeQuery(checkTable);
            if(resultSet.next() == false){
                Statement create=connectToDatabase().createStatement();
                create.executeUpdate(createTable);
            }
            else{
                Statement drop=connectToDatabase().createStatement();
                drop.executeUpdate(dropTable);
                Statement create=connectToDatabase().createStatement();
                create.executeUpdate(createTable);
                System.out.println("Old data removed");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createErrTable(){
        try{

            Statement statement=connectToDatabase().createStatement();
            ResultSet resultSet = statement.executeQuery(checkErrTable);
            if(resultSet.next() == false){
                Statement create=connectToDatabase().createStatement();
                create.executeUpdate(createErrTable);
            }
            else{
                Statement drop=connectToDatabase().createStatement();
                drop.executeUpdate(dropErrTable);
                Statement create=connectToDatabase().createStatement();
                create.executeUpdate(createErrTable);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void inputToDatabase(List<EmployeeDTO> dataInput) {
        try {
            PreparedStatement preparedStatement = connectToDatabase().prepareStatement(updateEmployee);
            for (EmployeeDTO employee : dataInput) {
                preparedStatement.setInt(1, Integer.parseInt(employee.getEmployeeId()));
                preparedStatement.setString(2, employee.getPrefix());
                preparedStatement.setString(3, employee.getFirstName());
                preparedStatement.setString(4, employee.getMiddleInitial());
                preparedStatement.setString(5, employee.getLastName());
                preparedStatement.setString(6, employee.getGender());
                preparedStatement.setString(7, employee.getEmail());
                preparedStatement.setDate(8, new Date(DateChanger.convert(employee.getBirthDate())));
                preparedStatement.setDate(9, new Date(DateChanger.convert(employee.getJoinDate())));
                preparedStatement.setInt(10, Integer.parseInt(employee.getSalary()));
                preparedStatement.addBatch();
            }
            try {
                preparedStatement.executeBatch();

            } catch (BatchUpdateException e) {
                long[] k = e.getLargeUpdateCounts();
                    for (int j = 0; j < dataInput.size(); j++) {
                        if(k[j] == -3){
                        inputErrors(dataInput.get(j));}
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void inputErrors(EmployeeDTO employee){
        try{
            PreparedStatement preparedStatement = connectToDatabase().prepareStatement(updateEmployee);
            preparedStatement.setInt(1, Integer.parseInt(employee.getEmployeeId()));
            preparedStatement.setString(2, employee.getPrefix());
            preparedStatement.setString(3, employee.getFirstName());
            preparedStatement.setString(4, employee.getMiddleInitial());
            preparedStatement.setString(5, employee.getLastName());
            preparedStatement.setString(6, employee.getGender());
            preparedStatement.setString(7, employee.getEmail());
            preparedStatement.setDate(8, new Date(DateChanger.convert(employee.getBirthDate())));
            preparedStatement.setDate(9, new Date(DateChanger.convert(employee.getJoinDate())));
            preparedStatement.setInt(10, Integer.parseInt(employee.getSalary()));
                        preparedStatement.execute();

                }catch (SQLException ee){
            try{
                    PreparedStatement errStatement = connectToDatabase().prepareStatement(updateErrEmployee);
                    errStatement.setInt(1,Integer.parseInt(employee.getEmployeeId()));
                    errStatement.setString(2,employee.getPrefix());
                    errStatement.setString(3,employee.getFirstName());
                    errStatement.setString(4,employee.getMiddleInitial());
                    errStatement.setString(5,employee.getLastName());
                    errStatement.setString(6,employee.getGender());
                    errStatement.setString(7,employee.getEmail());
                    errStatement.setDate(8, new Date(DateChanger.convert(employee.getBirthDate())));
                    errStatement.setDate(9, new Date(DateChanger.convert(employee.getJoinDate())));
                    errStatement.setInt(10,Integer.parseInt(employee.getSalary()));
                    errStatement.executeUpdate();}
            catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    public void getNumberOfRecords(){
        try {
            Statement statement = connectToDatabase().createStatement();
            ResultSet resultSet = statement.executeQuery(countEmpTable);
            resultSet.next();
            System.out.println("Number of records inputted : " + resultSet.getInt("count(*)"));
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void getNumberOfErrRecords(){
        try {
            Statement statement = connectToDatabase().createStatement();
            ResultSet resultSet = statement.executeQuery(countErrTable);
            resultSet.next();
            System.out.println("Number of error records inputted : " + resultSet.getInt("count(*)"));
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}