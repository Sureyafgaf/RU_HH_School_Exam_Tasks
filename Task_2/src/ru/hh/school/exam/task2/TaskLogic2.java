package ru.hh.school.exam.task2;

import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Arrays;

public class TaskLogic2 {

    public TaskLogic2(JTextArea ta){
        Vector Weights = new Vector();
        int[] Weight;
        int[] targetWeightCol;
        int[] halfWeightCol;
        int[] secondHalfCol;

        StringBuilder sb = new StringBuilder();
        String currentDir;
        String filePath;
        String division2;

        Weight = getVector(ta);

        Arrays.sort(Weight);

        MassCollection mc = new MassCollection(Weight,100);
        // ���������� ������� ������
        sb.append(("Task: " + "\n\r"));
        for (int i = 0; i <Weight.length; i++) {
            sb.append((Weight[i] + "; "));
        }
        sb.append(("\n\r"));

        if (mc.boolHalf) {
            halfWeightCol = mc.getHalfWeightCol();
            secondHalfCol = mc.getSecondHalfCol();
            sb.append(("Balance is reachable:" + ";\n\r"));
            for (int i = 0; i <Weight.length; i++) {
                if(halfWeightCol[i] > 0) {sb.append((halfWeightCol[i] + "; "));}
            }
            sb.append((" - "));
            for (int i = 0; i <Weight.length; i++) {
                if(secondHalfCol[i] > 0) {sb.append((secondHalfCol[i] + "; "));}
            }
            sb.append(("\n\r"));
        }
        else{
            sb.append(("Balance is not reachable:" + ";\n\r"));
        }

        if (mc.boolTarget) {
            targetWeightCol = mc.getTargetWeightCol();
            sb.append(("YES. Target weight (100) could be picked" + ";\n\r"));
            for (int i = 0; i <Weight.length; i++) {
                if(targetWeightCol[i] > 0) {sb.append((targetWeightCol[i] + "; "));}
            }
            sb.append(("\n\r"));
        }
        else{
            sb.append(("NO. Target weight (100) could not be picked" + ";\n\r"));
        }

        ta.setText(sb.toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
        currentDir = System.getProperty("user.dir");
        filePath = currentDir + "\\Task_2_" + sdf.format(new Date()) + ".txt";

        try(FileWriter writer = new FileWriter(filePath, true)){
            writer.write(sb.toString());
            writer.append('\n');
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

    }

    private class MassCollection {

        private int[] Weight;           // ������� �����
        private int targetWeight = 100;  // ������� �����

        private boolean boolTarget;   // ����� �� ������� �� ������ ������� �����
        private boolean boolHalf;     // ������� �� �������

        private int sumWeight;      // ��������� ��� ������� ���������
        private int halfSumWeight;  // �������� ���������� ���� ������� ���������

        private int[] targetWeightCol; // ����� ��� �������� ������� �����
        private int[] halfWeightCol;   // ����� ��� ������� �������
        private int[] secondHalfCol;   // ������ �������� �� ������� �������

        public void setWeight(int[] weight) {
            Weight = weight;
        }

        public void setTargetWeght(int targetWeght) {
            this.targetWeight = targetWeght;
        }

        public boolean isBoolTarget() {
            return boolTarget;
        }

        public boolean isBoolHalf() {
            return boolHalf;
        }

        public int[] getTargetWeightCol() {
            return targetWeightCol;
        }

        public int[] getHalfWeightCol() {
            return halfWeightCol;
        }

        public int[] getSecondHalfCol() {

            for (int i=0; i<Weight.length; i++){
                if (halfWeightCol[i] == 0){
                    secondHalfCol[i] = Weight[i];
                }
                else{
                    secondHalfCol[i] = 0;
                }
            }

            return secondHalfCol;
        }

        public MassCollection(int[] weight, int targetWeght) {
            Weight = weight;
            targetWeight = targetWeght;

            targetWeightCol = new int[Weight.length]; // ����� ��� �������� ������� �����
            halfWeightCol = new int[Weight.length];   // ����� ��� ������� �������
            secondHalfCol = new int[Weight.length];

            sumWeight = getSumm(Weight);
            halfSumWeight = getHalfSumWeight();

            if (sumWeight>targetWeght) {
                checkTarget();
            }

            if (halfSumWeight != 0){
                checkHalf();
            }
        }

        private void checkHalf(){
            String nBinary;
            long nComb = (long)(Math.pow(2, Weight.length) - 1); // ���������� ���������� ��������� ����������

            M1:for (long n=1; n < nComb; n++){

                nBinary = Long.toBinaryString(n);
                nBinary = addZeroCharAndReverse(nBinary, Weight.length);

                for(int i=0; i < Weight.length; i++){
                    if(nBinary.charAt(i) == '1'){
                        halfWeightCol[i] = Weight[i];
                    }
                    else{
                        halfWeightCol[i] = 0;
                    }
                }
                if (getSumm(halfWeightCol)==halfSumWeight){
                    break M1;
                }
            }

            if (halfSumWeight == getSumm(halfWeightCol)){
                boolHalf = true;
            }
            else{
                boolHalf = false;
            }
        }

        private void checkTarget() {

            String nBinary;
            long nComb = (long)(Math.pow(2, Weight.length) - 1); // ���������� ���������� ��������� ����������

            M1:for (long n=1; n < nComb; n++){

                nBinary = Long.toBinaryString(n);
                nBinary = addZeroCharAndReverse(nBinary, Weight.length);

                for(int i=0; i < Weight.length; i++){
                    if(nBinary.charAt(i) == '1'){
                        this.targetWeightCol[i] = Weight[i];
                    }
                    else{
                        this.targetWeightCol[i] = 0;
                    }
                }
                if (getSumm(this.targetWeightCol)==targetWeight){
                    break M1;
                }

            }

            if (targetWeight == getSumm(this.targetWeightCol)){
                boolTarget = true;
                //return this.targetWeightCol;
            }
            else{
                boolTarget = false;
                //return null;
            }

        }

        private int getSumm(int[] Weight){
        /*���������� ����� ������ �������*/
            int sum = 0;
            for (int i=0; i < Weight.length; i++){
                sum = sum + Weight[i];
            }
            return sum;
        }

        private String addZeroCharAndReverse(String s, int nMax){
            // ��������� ������ ������� ������ � ���������� nMax � ������ ������� ����� (�������������� ������)
            StringBuilder sb =new StringBuilder("0");
            nMax = nMax - s.length();
            for (int i=1; i<nMax; i++){
                sb.append("0");
            }
            sb.append(s);
            sb.reverse();
            return sb.toString();
        }

        private int getMaxNonExceeding(int target, int[] Array) {
            //���������� ������ ������������� ����� �������, �� ������������ �������� target
            int i = Array.length - 1;
            while ((i > 0) && ((target < Array[i])||(Array[i] == 0))) {
                i--;
            }
            return i;
        }

        private int getHalfSumWeight(){
            if (sumWeight%2 == 0) {
                halfSumWeight = sumWeight/2;
                boolHalf = true;
            }
            else {
                halfSumWeight = 0;
                boolHalf = false;
            }
            return halfSumWeight;
        }

    }

    private Double getDoubleNum(StringTokenizer st){
        /*��������������� ��������� ������ �� ������ � �����*/
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

    private int[] getVector(JTextArea ta){
        Vector Weights = new Vector();
        String s = ta.getText();
        Double W;

        StringTokenizer st = new StringTokenizer (s, " \t\n\r,.:;+-/*|");

        while (st.hasMoreTokens()){

            W = getDoubleNum(st);

            Weights.add(W);
        }

        int[] Weight = new int[Weights.size()];

        for (int i = 0; i < Weights.size(); i++){
            Weight[i] = (int)(double)Weights.get(i);
        }

        return Weight;
    }

    protected static double[] getArray(double[] array){

        int size = array.length;
        double[] result = new double[size];
        System.arraycopy(array, 0, result, 0, size);
        return result;

    }

}
