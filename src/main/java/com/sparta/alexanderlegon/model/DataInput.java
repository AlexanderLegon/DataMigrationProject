package com.sparta.alexanderlegon.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataInput {

    static String inputFileLarge = "resources/EmployeeRecordsLarge.csv";
    static String inputFile = "resources/Employees.csv";
    static Thread[] thread;
    double startTime, readTime;

    static String line;
    static int threadCount = 101;
    static int numberToDo;
    static List<String> inputString = new ArrayList<>();
    static List<List<String>> toDo = new ArrayList<>();
    static int u;
    public static int whileRunning = 0;


    public void readFile(int chooseFile) {
        String fileChoice;
        if(chooseFile ==1 ){
            fileChoice = inputFile;
        }
        else{
            fileChoice = inputFileLarge;
        }
        try (

                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileChoice))) {
            startTime = System.nanoTime();
            while ((line=bufferedReader.readLine()) != null) {
                inputString.add(line);
            }
            inputString.remove(0);
            readTime = System.nanoTime() - startTime;

            int count = 0;
            System.out.println("Time taken to read file : " + readTime/1000000  + "ms");
            System.out.println("Now uploading to the database");
            numberToDo = inputString.size()%(threadCount-1);
            int noPerThread = (inputString.size() - numberToDo)/(threadCount-1);
            for (int i = 0 ; i < (threadCount-1); i++){
                List<String> temp = new ArrayList<>();
                for(int j =0; j < noPerThread; j++){
                    if(inputString.get(count)!=null){
                    temp.add(inputString.get(count));
                    count++;}
                }
                toDo.add(temp);
            }
            List<String> tempo = new ArrayList<>();
            for(int l = (inputString.size() - numberToDo); l<inputString.size();l++){
                tempo.add(inputString.get(count));
                count++;}
            toDo.add(tempo);
            sendToDatabase sendToDatabased = new sendToDatabase();
            sendToDatabased.startThreads();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class  sendToDatabase implements Runnable {

        @Override
        public void run() {
            synchronized (this) {
                List<String> temp2 = DataInput.toDo.get(u++);
                List<EmployeeDTO> empDTO = new ArrayList<>();
                for (String employee: temp2) {
                    EmployeeDTO employeeData = new EmployeeDTO(employee);
                    empDTO.add(employeeData);
                }
                EmployeesDAO employeeDAO = new EmployeesDAO();
                employeeDAO.inputToDatabase(empDTO);
                DataInput.whileRunning--;
            }
        }

        public void startThreads(){
            thread = new Thread[threadCount];

            for (int k = 0; k < threadCount; k++) {
                thread[k] = new Thread(new sendToDatabase());
                thread[k].setPriority(Thread.MAX_PRIORITY);
                thread[k].start();
                whileRunning++;
            }
        }
    }
}


