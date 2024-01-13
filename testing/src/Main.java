import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends Thread {
    int[][] mat1;
    int[][] mat2;
    int[][] mat3;
    int row, col, choice;

    public static int[][] getMatrixInput(int rows, int cols, String matrixName) {
        JDialog inputDialog = new JDialog();
        inputDialog.setTitle(matrixName);
        inputDialog.setModal(true);
        inputDialog.setSize(300, 200);

        JPanel inputPanel = new JPanel(new GridLayout(rows, cols));
        inputDialog.add(inputPanel, BorderLayout.NORTH);

        JTextField[][] fields = new JTextField[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                fields[i][j] = new JTextField();
                inputPanel.add(fields[i][j]);
            }
        }

        JButton saveButton = new JButton("Save input");
        inputPanel.add(saveButton);
        saveButton.setBackground(Color.GREEN);

        int[][] matrix = new int[rows][cols];
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        matrix[i][j] = Integer.parseInt(fields[i][j].getText());
                    }
                }
                inputDialog.dispose();
            }
        });

        inputDialog.setVisible(true);

        return matrix;
    }

    Main(int[][] A, int[][] B, int[][] C, int i, int j, int choice) {
        this.mat1 = A;
        this.mat2 = B;
        this.mat3 = C;
        this.row = i;
        this.col = j;
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
        for (int k = 0; k < mat2.length; k++) {
            mat3[row][col] += mat1[row][k] * mat2[k][col];
        }
    }

    public void add() {
        mat3[row][col] = mat1[row][col] + mat2[row][col];
    }

    public void subtract() {
        mat3[row][col] = mat1[row][col] - mat2[row][col];
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Matrix Operation");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(300, 200);

        JPanel inputPanel = new JPanel(new GridLayout(0, 1));
        inputPanel.setBackground(Color.black);
        frame.add(inputPanel, BorderLayout.NORTH);

        JLabel label1 = new JLabel("Matrix 1 rows:");
        JTextField textField1 = new JTextField();
        inputPanel.add(label1);
        inputPanel.add(textField1);
        label1.setForeground(Color.WHITE);
        textField1.setBackground(Color.gray);

        JLabel label2 = new JLabel("Matrix 1 columns:");
        JTextField textField2 = new JTextField();
        inputPanel.add(label2);
        inputPanel.add(textField2);
        label2.setForeground(Color.WHITE);
        textField2.setBackground(Color.gray);

        JLabel label3 = new JLabel("Matrix 2 rows:");
        JTextField textField3 = new JTextField();
        inputPanel.add(label3);
        inputPanel.add(textField3);
        label3.setForeground(Color.WHITE);
        textField3.setBackground(Color.gray);

        JLabel label4 = new JLabel("Matrix 2 columns:");
        JTextField textField4 = new JTextField();
        inputPanel.add(label4);
        inputPanel.add(textField4);
        label4.setForeground(Color.WHITE);
        textField4.setBackground(Color.gray);

        String[] operations = { "Multiply", "Add", "Subtract" };
        JComboBox operationsBox = new JComboBox(operations);
        inputPanel.add(operationsBox);

        JButton saveButton = new JButton("Save input");
        inputPanel.add(saveButton);
        saveButton.setBackground(Color.GREEN);

        JTextArea outputArea = new JTextArea();
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row1 = Integer.parseInt(textField1.getText());
                int col1 = Integer.parseInt(textField2.getText());
                int row2 = Integer.parseInt(textField3.getText());
                int col2 = Integer.parseInt(textField4.getText());

                int[][] mat1 = new int[row1][col1];
                int[][] mat2 = new int[row2][col2];
                int[][] mat3 = new int[row1][col2];

                Main[][] threads = new Main[row1][col2];

                mat1 = getMatrixInput(row1, col1, "Matrix 1");

                mat2 = getMatrixInput(row2, col2, "Matrix 2");

                String selectOperation = (String) operationsBox.getSelectedItem();

                if ("Multiply".equals(selectOperation)) {
                    for (int i = 0; i < row1; i++) {
                        for (int j = 0; j < col2; j++) {
                            threads[i][j] = new Main(mat1, mat2, mat3, i, j, 1);
                            threads[i][j].start();
                        }
                    }
                } else if ("Add".equals(selectOperation)) {
                    for (int i = 0; i < row1; i++) {
                        for (int j = 0; j < col2; j++) {
                            threads[i][j] = new Main(mat1, mat2, mat3, i, j, 2);
                            threads[i][j].start();
                        }
                    }
                } else if ("Subtract".equals(selectOperation)) {
                    for (int i = 0; i < row1; i++) {
                        for (int j = 0; j < col2; j++) {
                            threads[i][j] = new Main(mat1, mat2, mat3, i, j, 3);
                            threads[i][j].start();
                        }
                    }
                }

                for (int i = 0; i < row1; i++) {
                    for (int j = 0; j < col2; j++) {
                        try {
                            threads[i][j].join();
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                StringBuilder output = new StringBuilder();
                for (int i = 0; i < row1; i++) {
                    for (int j = 0; j < col2; j++) {
                        output.append(mat3[i][j]).append(" ");
                    }
                    output.append("\n");
                }
                outputArea.setText(output.toString());
            }
        });

        JLabel outputTag = new JLabel("Output :", SwingConstants.LEFT);
        inputPanel.add(outputTag);
        outputTag.setForeground(Color.RED);
        outputTag.setBackground(Color.YELLOW);

        frame.setVisible(true);
    }
}