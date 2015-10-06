package ru.hh.school.exam.task2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class TaskInterface2 extends JFrame {

    //private Vector Dots;
    private JTextArea taOut;
    private String TAString;

    public String getTAString() {
        return TAString;
    }

    public JTextArea getTaOut() {
        return taOut;
    }

    TaskInterface2(String s){
        super(s);
        Font f = new Font("ROMAN_BASELINE", Font.ROMAN_BASELINE, 14);
        setFont(f);

        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //gbc.ipadx = 20;
        gbc.insets = new Insets(10,10,10,10);
        gbc.ipady = 2;
        gbc.weightx = 1;
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.gridwidth = 3;
        JLabel l1 = new JLabel("Choose file:", JLabel.LEFT);
        add(l1, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 3;
        FileOpenTextField fotf = new FileOpenTextField();
        add(fotf, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 3;
        JLabel l2 = new JLabel("Or enter coordinates from keyboard:");
        add(l2, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 3;
        JTextField tf2 = new JTextField();
        add(tf2, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JTextArea ta1 = new JTextArea(10, 30);
        ta1.setEditable(false);
        JScrollPane sp = new JScrollPane(ta1);
        add(sp, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JButton b1 = new JButton("Calculate");
        add(b1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JButton b2 = new JButton("Reset");
        add(b2, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        JButton b3 = new JButton("Exit");
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(b3, gbc);

        fotf.setTa(ta1);
        fotf.setTf(tf2);
        tf2.addActionListener(new TextMove(tf2, ta1));
        b1.addActionListener(new ButtonCalculate(ta1));
        b2.addActionListener(new ButtonReset(ta1, tf2, fotf));
        taOut = ta1;
        TAString = ta1.getText();

        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class TextMove implements ActionListener{
        private JTextField tf;
        private JTextArea ta;

        TextMove (JTextField tf, JTextArea ta) {
            this.tf = tf;
            this.ta = ta;
        }

        public void actionPerformed(ActionEvent ae){
            if (ta.getText()==""){
                ta.setText(tf.getText() + "\n");
            }
            else {
                ta.append(tf.getText() + "\n");
            }
            tf.setText("");
        }
    }

    private class TextFieldChange {
        private FileOpenTextField fotf;
        private JTextArea ta;

        TextFieldChange(FileOpenTextField fotf, JTextArea ta) {
            this.fotf = fotf;
            this.ta = ta;

            try {
                ta.read(new FileReader(fotf.getFilePath()), null);
            } catch (IOException ex) {
                System.out.println("problem accessing file" + fotf.getFilePath());
            }

        }
    }

    private class FileOpenTextField extends JTextField implements MouseListener {
        private String FilePath;
        private JTextArea ta;
        private JTextField tf;

        public void setTf(JTextField tf) {
            this.tf = tf;
        }

        public void setTa(JTextArea ta) {
            this.ta = ta;
        }

        public String getFilePath() {
            return FilePath;
        }

        private void setFilePath(String filePath) {
            FilePath = filePath;
        }

        public FileOpenTextField() {
            super.setText("Click for file choose!");
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);
            addMouseListener(this);
        }

        public void mouseClicked(MouseEvent e){
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setFileFilter(new TxtFileFilter());
            switch (fc.showDialog(null, "Open")){
                case JFileChooser.APPROVE_OPTION:
                    File selectedFile = fc.getSelectedFile();
                    File directory = fc.getCurrentDirectory();
                    this.setFilePath(selectedFile.getPath());
                    this.setText(selectedFile.getPath());
                    if (ta != null){ new TextFieldChange(this, ta);}
                    if (tf != null){ tf.setEnabled(false);}
                    break;
                case JFileChooser.CANCEL_OPTION:
                    break;
                case JFileChooser.ERROR_OPTION:
                    System.err.println("Error");
                    break;
            }
        }

        public void mousePressed(MouseEvent e){}
        //public void mouseDragged(MouseEvent e){}
        public void mouseReleased(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        //public void mouseMoved(MouseEvent e){}

        private class TxtFileFilter extends javax.swing.filechooser.FileFilter{
            @Override
            public boolean accept(File file) {
                // Allow only directories, or files with ".txt" extension
                return file.isDirectory() || file.getAbsolutePath().endsWith(".txt");
            }
            @Override
            public String getDescription() {
                return "Text documents (*.txt)";
            }
        }
    }

    private class ButtonCalculate implements ActionListener{
        private JTextArea ta;

        public ButtonCalculate(JTextArea ta) {
            this.ta = ta;
        }

        public void actionPerformed(ActionEvent ae){
            TaskLogic2 t = new TaskLogic2(ta);
        }
    }

    private class ButtonReset implements ActionListener{
        private JTextField tf;
        private JTextArea ta;
        private FileOpenTextField fotf;

        public ButtonReset(JTextArea ta, JTextField tf, FileOpenTextField fotf) {
            this.ta = ta;
            this.tf = tf;
            this.fotf = fotf;
        }

        public void actionPerformed(ActionEvent e){
            ta.setText("");
            tf.setEnabled(true);
            tf.setText("");
            fotf.setText("Click for file choose!");
        }
    }

    public static void main(String[] args){
        TaskInterface2 f = new TaskInterface2(" Task 2");
    }

}
