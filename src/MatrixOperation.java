
import java.util.Scanner;
public class MatrixOperation extends Thread{
    int[][] mat1;
    int[][] mat2;
    int[][] mat3;
    static int row1, row2, col1, col2;
    int row, col, choice;

    MatrixOperation(int[][] A, int[][] B, int[][] C, int i, int j, int choice) {
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
    }

    public void subtract() {
        mat3[row][col] = mat1[row][col] - mat2[row][col];
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the operation you want to perform");
        System.out.println("1. Matrix Multiplication, 2. Matrix Addition, 3. Matrix Subtraction");
        int choice = sc.nextInt();

        System.out.print("Enter the number of rows, columns of the FIRST matrix ");
        row1 = sc.nextInt();
        col1 = sc.nextInt();
        System.out.print("Enter the number of rows, columns of the SECOND matrix ");
        row2 = sc.nextInt();
        col2 = sc.nextInt();

        if (col1 != row2 && choice == 1) {
            System.out.println("Matrix multiplication is not possible");
            System.exit(0);
        }

        if (col1 != col2 && choice != 1) {
            System.out.println("Matrix addition and subtraction is not possible");
            System.exit(0);
        }

        int[][] mat1 = new int[row1][col1];
        int[][] mat2 = new int[row2][col2];
        int[][] mat3 = new int[row1][col2];

        System.out.println("Enter the elements of first matrix");
        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col1; j++) {
                mat1[i][j] = sc.nextInt();
            }
        }

        System.out.println("Enter the elements of second matrix");
        for (int i = 0; i < row2; i++) {
            for (int j = 0; j < col2; j++) {
                mat2[i][j] = sc.nextInt();
            }
        }

        MatrixOperation[][] threads = new MatrixOperation[row1][col2];

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j] = new MatrixOperation(mat1, mat2, mat3, i, j, choice);
                threads[i][j].start();
            }
        }

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                threads[i][j].join();
            }
        }

        System.out.println("Output Matrix is");

        for (int i = 0; i < row1; i++) {
            for (int j = 0; j < col2; j++) {
                System.out.print(mat3[i][j] + " ");
            }
            System.out.println();
        }
    }
}
