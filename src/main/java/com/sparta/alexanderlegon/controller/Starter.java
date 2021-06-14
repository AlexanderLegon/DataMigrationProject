package com.sparta.alexanderlegon.controller;

import com.sparta.alexanderlegon.model.DataInput;
import com.sparta.alexanderlegon.model.EmployeesDAO;

import java.util.Scanner;

public class Starter {

    static double startTime, readTime, startTimet, readTimet;

    public static void start(){
        Thread.currentThread().setName("StarterThread");
        Scanner input = new Scanner(System.in);
        System.out.println("Press 1 to create a new employees table, input any other number to add new employees to existing list");
        int howToAdd = input.nextInt();
        EmployeesDAO employeesDAO = new EmployeesDAO();
        Scanner chooseFile = new Scanner(System.in);
        System.out.println("Press 1 for Small file, input any other number for large file");
        int chooseFiles = chooseFile.nextInt();
        startTime = System.nanoTime();
        startTimet = System.nanoTime();
        if(howToAdd == 1){
            employeesDAO.createTable(1);
            employeesDAO.createErrTable();
            DataInput dataInput = new DataInput();

            dataInput.readFile(chooseFiles);
            try {
                while(DataInput.whileRunning != 0){
                    Thread.sleep( 50);}

            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            EmployeesDAO employeesDAOT = new EmployeesDAO();
            employeesDAOT.getNumberOfRecords();
            employeesDAOT.getNumberOfErrRecords();

        }

        else{
                employeesDAO.createTable();
                employeesDAO.createErrTable();
                DataInput dataInput = new DataInput();
                dataInput.readFile(chooseFiles);
            try {
                while(DataInput.whileRunning != 0){
                    Thread.sleep( 50);}

            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            EmployeesDAO employeesDAOT = new EmployeesDAO();
            employeesDAOT.getNumberOfRecords();
            employeesDAOT.getNumberOfErrRecords();
        }
        readTime = System.nanoTime() - startTime;
        System.out.println("Time taken to complete data migration : " + readTime / 1000000000 + "s");
        readTimet = System.nanoTime() - startTimet;
        System.out.println("Time taken to complete data read and migration : " + readTimet/1000000000 + "s");
    }
}