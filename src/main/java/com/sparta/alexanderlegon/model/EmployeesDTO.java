package com.sparta.alexanderlegon.model;

import com.sparta.alexanderlegon.model.EmployeesDAO;

public class EmployeesDTO {

    private String[] input;

    public static void dataTransferObject(){

    }
    public void sendToDatabase(String[] inputStrings){
        input = inputStrings;
        EmployeesDAO employeesDAO = new EmployeesDAO();
        employeesDAO.inputToDatabase(input);
    }
}
