package ru.hh.school.exam.task1;

import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Date;
//import java.util.Locale;
import javax.swing.*;
import java.io.*;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
//import java.io.IOException;

public class TaskLogic1 {

    //private Vector Dots;
    public TaskLogic1(JTextArea ta){
        Vector Dots = new Vector();
        double[] Dot = new double[5];
        StringBuilder sb = new StringBuilder();
        String currentDir;
        String filePath;

        Dots = getVector(ta);

        radius(Dots);

        neibors(Dots);

        for (int i = 0; i <Dots.size(); i++) {
            Dot = (double[])Dots.get(i);
            sb.append(("Dot #" + (int)Dot[4] + ": x=" + (int)Dot[0] + ", y=" + (int)Dot[1] +
                        "; radius=" + Dot[2] + "; neighbours=" + (int)Dot[3] + ";\n\r"));
        }

        ta.setText(sb.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        currentDir = System.getProperty("user.dir");
        filePath = currentDir + "\\Task_1_" + sdf.format(new Date()) + ".txt";

        try(FileWriter writer = new FileWriter(filePath, true)){
            writer.write(sb.toString());
            writer.append('\n');
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

    }

    private Double getDoubleNum(StringTokenizer st){
        try{
            return Double.parseDouble(st.nextToken());
        }
        catch(IllegalArgumentException ex){
            System.out.println(ex.getMessage());
            if(st.hasMoreTokens()){
                //st.nextToken();
                return getDoubleNum(st);
            }
            else{
                return 0.0;
            }

        }
    }

    private Vector getVector(JTextArea ta){
        Vector Dots = new Vector();
        double[] Dot = new double[5];
        String s = ta.getText();
        int i = 0;

        StringTokenizer st = new StringTokenizer (s, " \t\n\r,.:;/*|");

        while (st.hasMoreTokens()){
            i++;
            Dot[0] = getDoubleNum(st);
            if (st.hasMoreTokens()) {
                Dot[1] = getDoubleNum(st);

            } else {
                Dot[1] = 0;
            }

            Dot[4] = i;
            Dots.add(getArray(Dot));
        }

        return Dots;
    }

    private static void radius(Vector Dots){
        double[] Dot_i;
        double[] Dot_j;
        double rad = 0;
        for (int i = 0; i < Dots.size(); i++) {
            Dot_i = getArray((double[])Dots.get(i));
            for (int j = 0; j < Dots.size(); j++) {
                Dot_j = getArray((double[])Dots.get(j));
                rad = Math.sqrt((Dot_i[0] - Dot_j[0])*(Dot_i[0] - Dot_j[0]) +
                        (Dot_i[1] - Dot_j[1])*(Dot_i[1] - Dot_j[1]));
                if (((Dot_i[2] > rad)&&(Dot_i[2] > 0)&&(rad > 0))||(Dot_i[2] == 0)) {
                    Dot_i[2] = rad;
                }
            }
            Dots.set(i, getArray(Dot_i));
        }

    }

    private static void neibors(Vector Dots){
        double[] Dot_i;
        double[] Dot_j;
        double rad = 0;
        for (int i = 0; i < Dots.size(); i++) {
            Dot_i = getArray((double[])Dots.get(i));
            for (int j = 0; j < Dots.size(); j++) {
                Dot_j = getArray((double[])Dots.get(j));
                rad = Math.sqrt((Dot_i[0] - Dot_j[0])*(Dot_i[0] - Dot_j[0]) +
                        (Dot_i[1] - Dot_j[1])*(Dot_i[1] - Dot_j[1]));
                if ((2 * Dot_i[2] >= rad)&&(rad > 0)) {
                    Dot_i[3]++;
                }
            }
            Dots.set(i, getArray(Dot_i));
        }
    }

    protected static double[] getArray(double[] array){

        int size = array.length;
        double[] result = new double[size];
        System.arraycopy(array, 0, result, 0, size);
        return result;

    }
    public static void main(String[] args){

        Vector Dots = new Vector();
        double[] Dot = new double[5];

        TaskInterface frame = new TaskInterface(" Task 1");
        TaskLogic1 v1 = new TaskLogic1(frame.getTaOut());

        //Dots = getVector(frame.getTaOut());

        v1.radius(Dots);

        v1.neibors(Dots);

        for (int i = 0; i <Dots.size(); i++) {
            Dot = (double[])Dots.get(i);
            System.out.println(Dot[0] + " " + Dot[1] + " " + Dot[2] + " " + Dot[3] + " " + Dot[4]);
        }

    }
}
