/**
 * Created by Docent on 05.10.2015.
 */
import java.io.*;

public class FileWorker {

    public static void write(String fileName, String text) {
        //���������� ����
        File file = new File(fileName);

        try {
            //���������, ��� ���� ���� �� ���������� �� ������� ���
            if(!file.exists()){
                file.createNewFile();
            }

            //PrintWriter ��������� ����������� ������ � ����
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());

            try {
                //���������� ����� � ����
                out.print(text);
            } finally {
                //����� ���� �� ������ ������� ����
                //����� ���� �� ���������
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exists(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
    }

    public static String read(String fileName) throws FileNotFoundException {
        //���� ����. ������ ��� ���������� ������
        StringBuilder sb = new StringBuilder();

        exists(fileName);

        File file = new File(fileName);

        try {
            //������ ��� ������ ����� � �����
            BufferedReader in = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            try {
                //� ����� ��������� ��������� ����
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                //����� �� �������� ������� ����
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        //���������� ���������� ����� � �����
        return sb.toString();
    }

    public static void add(String nameFile, String newText) throws FileNotFoundException {
        exists(nameFile);
        StringBuilder sb = new StringBuilder();
        String oldFile = read(nameFile);
        sb.append(oldFile);
        sb.append(newText);
        write(nameFile, sb.toString());
    }
    public static void update(String nameFile, String newText) throws FileNotFoundException {
        exists(nameFile);
        write(nameFile, newText);
    }
}
