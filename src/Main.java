import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends Thread {
    int[][] mat1;
    int[][] mat2;
    int[][] mat3;
    static int row1, row2, col1, col2;
    int row, col, choice;

    Main(int[][] A, int[][] B, int[][] C, int i, int j, int choice) {
        this.mat1 = A;
        this.mat2 = B;
        this.mat3 = C;
        row = i;
        col = j;
        this.choice = choice;
    }

    public void run() {
        if (choice == 1)
            multiply();
        else if (choice == 2)
            add();
        else if (choice == 3)
            subtract();
    }

    public void multiply() {
        for (int k = 0; k < row2; k++) {
            mat3[row][col] += mat1[row][k] * mat2[k][col];
        }
    }

    public void add() {
        mat3[row][col] = mat1[row][col] + mat2[row][col];
        // System.out.println(mat3[row][col]);
    }

    public void subtract() {
        mat3[row][col] = mat1[row][col] - mat2[row][col];
    }

    public static void main(String[] args) throws InterruptedException {

        JFrame frame = new JFrame("Matrix Operation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        frame.add(inputPanel, BorderLayout.CENTER);

        JLabel label1 = new JLabel("Matrix 1 rows:");
        JTextField textField1 = new JTextField();
        inputPanel.add(label1);
        inputPanel.add(textField1);

        JLabel label2 = new JLabel("Matrix 1 columns");
        JTextField textField2 = new JTextField();
        inputPanel.add(label2);
        inputPanel.add(textField2);

        JLabel label3 = new JLabel("Matrix 2 rows:");
        JTextField textField3 = new JTextField();
        inputPanel.add(label3);
        inputPanel.add(textField3);

        JLabel label4 = new JLabel("Matrix 2 columns:");
        JTextField textField4 = new JTextField();
        inputPanel.add(label4);
        inputPanel.add(textField4);

        JButton saveButton = new JButton("Save input");

        final JTextField finalTextField1 = textField1; 
        final JTextField finalTextField2 = textField2; 
        final JTextField finalTextField3 = textField3;
        final JTextField finalTextField4 = textField4;
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input1 = finalTextField1.getText(); 
                String input2 = finalTextField2.getText();
                String input3 = finalTextField3.getText();
                String input4 = finalTextField4.getText();

                int row1 = Integer.parseInt(input1);
                int col1 = Integer.parseInt(input2);
                int row2 = Integer.parseInt(input3);
                int col2 = Integer.parseInt(input4);
            }
        });

        int[][] mat1 = new int[row1][col1];
        int[][] mat2 = new int[row2][col2];
        int[][] mat3 = new int[row1][col2];

        JPanel matrixPanel = new JPanel(new GridLayout(row1, col1));
        frame.add(matrixPanel, BorderLayout.CENTER);

        JTextField[][] matrixOneFields = new JTextField[row1][col1];
        for (int i = 0; i<row1;i++) {
            for(int j =0;j<col1; j++) {
                matrixOneFields[i][j] = new JTextField();
                matrixPanel.add(matrixOneFields[i][j]);
            }
        }

        JTextField[][] matrixTwoFields = new JTextField[row2][col2];
        for (int i = 0; i<row2;i++) {
            for(int j =0;j<col2; j++) {
                matrixTwoFields[i][j] = new JTextField();
                matrixPanel.add(matrixTwoFields[i][j]);
            }
        }

        final JTextField[][] finalMatrixOneFields = matrixOneFields;
        final JTextField[][] finalMatrixTwoFields = matrixTwoFields;
        final int[][] finalMat1 = mat1; 
        final int[][] finalMat2 = mat2;
        JButton storeButton = new JButton("Store Matrix");
        storeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < row1; i++) {
                    for (int j = 0; j < col1; j++) {
                        String input = finalMatrixOneFields[i][j].getText();
                        finalMat1[i][j] = Integer.parseInt(input); 
                    }
                }

                for (int i = 0; i < row1; i++) {
                    for (int j = 0; j < col1; j++) {
                        String input = finalMatrixTwoFields[i][j].getText();
                        finalMat2[i][j] = Integer.parseInt(input);
                    }
                }
            }
        });

        matrixPanel.add(storeButton);

        String[] operations = {"Multiply", "Add", "Subtract"};
        JComboBox operationsBox = new JComboBox();
        inputPanel.add(operationsBox);
        String selectOperation = (String) operationsBox.getSelectedItem();

        Main[][] threads = new Main[row1][col2];

        if ("Multiply".equals(selectOperation)) {
            for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j] = new Main(mat1, mat2, mat3, i, j, 1);
                threads[i][j].start();
            }
        }

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j].join();
            }
        }
        } else if ("Add".equals(selectOperation)) {
            for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j] = new Main(mat1, mat2, mat3, i, j, 2);
                threads[i][j].start();
            }
        }

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j].join();
            }
        }
        } else if ("Subtract".equals(selectOperation)) {
            for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j] = new Main(mat1, mat2, mat3, i, j, 3);
                threads[i][j].start();
            }
        }

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j].join();
            }
        }
        }

        JTextArea outpuArea = new JTextArea();

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                output.append(mat3[i][j]).append(" ");
            }
            output.append("\n");
        }

        outpuArea.setText(output.toString());
        frame.add(new JScrollPane(outpuArea),BorderLayout.SOUTH);
    }
}
