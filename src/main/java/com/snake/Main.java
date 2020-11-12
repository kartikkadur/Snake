package com.snake;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean executeLoop = true;
        System.out.println("Application Start");
        while(executeLoop){
            executeLoop = executeApplicationLoop();
        }
    }

    private static boolean executeApplicationLoop(){
        int userInput;
        double result;
        double operand1;
        double operand2;
        // define scanners
        Scanner input = new Scanner(System.in);
        Scanner operand = new Scanner(System.in);
        // read the input functions
        System.out.println("\nPlease Choose functions 1 through 6:");
        System.out.println("[1] Add\n[2] Subtract\n[3] Multiply\n[4] Divide\n[5] Exponential\n[6] Logarithm\n\n[7] Exit");
        System.out.println("\nInput:");
        // user input
        userInput = input.nextInt();
        switch (userInput) {
            case 1:
                // read the operands
                System.out.println("Operand:");
                operand1 = operand.nextFloat();
                operand2 = operand.nextFloat();
                result = operand1 + operand2;
                System.out.printf("==> " + operand1 + " + " + operand2 + " = " + result);
                break;
            case 2:
                // read the operands
                System.out.println("Operand:");
                operand1 = operand.nextFloat();
                operand2 = operand.nextFloat();
                result = operand1 - operand2;
                System.out.printf("==> " + operand1 + " - " + operand2 + " = " + result);
                break;
            case 3:
                // read the operands
                System.out.println("Operand:");
                operand1 = operand.nextFloat();
                operand2 = operand.nextFloat();
                result = operand1 * operand2;
                System.out.printf("==> " + operand1 + " * " + operand2 + " = " + result);
                break;
            case 4:
                // read the operands
                System.out.println("Operand:");
                operand1 = operand.nextFloat();
                operand2 = operand.nextFloat();
                if (operand2 == 0) {
                    System.out.println("");
                    result = -1;
                } else {
                    result = operand1 / operand2;
                }
                System.out.printf("==> " + operand1 + " / " + operand2 + " = " + result);
                break;
            case 5:
                // read the operands
                System.out.println("Operand:");
                operand1 = operand.nextFloat();
                result = Math.exp(operand1);
                System.out.printf("==> exp( %.1f ) = %.1f", operand1, result);
                break;
            case 6:
                // read the operands
                System.out.println("Operand:");
                operand1 = operand.nextFloat();
                result = Math.log(operand1);
                System.out.printf("==> log( %.1f ) = %.1f", operand1, result);
                break;
            case 7:
                operand.close();
                input.close();
                System.exit(0);
                break;
            default:
                System.out.println("Please choose a number between [1-7]");
                return true;
        }
        return true;
    }
}
