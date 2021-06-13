package com.sparta.alexanderlegon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataInput {

    static String inputFile = "resources/EmployeeRecordsLarge.csv";
    //static String inputFile = "resources/Employees.csv";
    static Thread[] thread;
    double startTime, readTime;

    static String line;
    static int threadCount = 50;
    static int numberToDo;
    static List<String> inputString = new ArrayList<>();
    static List<List<String>> toDo = new ArrayList<>();
    static int k;
    static int whileRunning = 0;


    public void readFile() {
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))) {
            startTime = System.nanoTime();
            while ((line=bufferedReader.readLine()) != null) {
                inputString.add(line);
            }
            inputString.remove(0);
            readTime = System.nanoTime() - startTime;

            int count = 0;
            System.out.println("Time taken to read file : " + readTime/1000000  + "ms");
            System.out.println("Now uploading to the database");
//            numberToDo = Math.round(inputString.size()/threadCount);
////logically change to take all find the modulus 50, take extras and dump onto another thread
//            for (int i = 0 ; i < threadCount; i++){
//                List<String> temp = new ArrayList<>();
//                for(int j =0; j < numberToDo; j++){
//                    if(inputString.get(count)!=null){
//                    temp.add(inputString.get(count));
//                    count++;}
//                }
//                toDo.add(temp);
//            }
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

            thread = new Thread[threadCount];

            for (int k = 0; k < threadCount; k++) {
                thread[k] = new Thread(new sendToDatabase());
                thread[k].setPriority(Thread.MAX_PRIORITY);
                thread[k].start();
                whileRunning++;
            }
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
                EmployeesDAO employeesDAO = new EmployeesDAO();
                String[] temp = new String[DataInput.numberToDo];
                List<String> temp2;
                temp2 = DataInput.toDo.get(k++);
                temp = temp2.toArray(new String[1]);
                employeesDAO.inputToDatabase(temp);
                DataInput.whileRunning--;
            }
        }
    }
}


