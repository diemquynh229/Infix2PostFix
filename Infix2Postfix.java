/**
 * 
 */
package edu.iastate.cs228.hw3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/**
 * @author quynhduong
 *
 */
public class Infix2Postfix {

	private static boolean checkError; // true if error

	/**
	 * The main method to scan the input.txt file check the error and create
	 * output.txt file
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		File input = new File("input.txt");
		try (Scanner sc = new Scanner(input)) {
			String result = "";
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				// System.out.println(infixToPostfix(line));
				// System.out.print(checkParenthesis(line));
				String error1 = checkParenthesis(line);
				String error2 = checkOperand(line);
				String error3 = checkOperator(line);
				if (checkError == false) {
					result += infixToPostfix(line) + "\n";
				} else {
					if (!error2.isEmpty()) {
						result += error2 + "\n";
					} else if (!error1.isEmpty()) {
						result += error1 + "\n";
					} else if (!error3.isEmpty()) {
						result += error3 + "\n";
					}
				}
				checkError = false;
			}

			// This is for checking the output
			System.out.println(result);
			System.out.println();

			FileOutputStream output = new FileOutputStream("output.txt");
			output.write(result.getBytes());
			System.out.println("Output file is created  in the same directory as the input file.");
			output.close();
		}

	}

	/**
	 * Convert from infix to postfix
	 * 
	 * @param exp
	 * @return
	 */
	public static String infixToPostfix(String exp) {
		String postfix = new String("");
		Stack<String> stack = new Stack<>();

		for (String token : exp.split(" ")) {

			if (isOperand(token)) {
				postfix += token + " ";

			} else if (token.equals("(")) {
				stack.push(token);

			} else if (token.equals(")")) {
				while (!stack.isEmpty() && !stack.peek().equals("(")) {
					postfix += stack.pop() + " ";
				}
				stack.pop();

			} else { // token is an operator
				while (!stack.isEmpty() && evaluatePre(token) <= evaluatePre(stack.peek())
						&& !stack.peek().equals("(")) {

					postfix += stack.pop() + " ";
				}
				stack.push(token);
			}

		}

		// pop all the operators from the stack
		while (!stack.isEmpty()) {
			if (stack.peek().equals("(")) {
				return "error expression";
			}
			postfix += stack.pop() + " ";
		}
		return postfix;
	}

	/**
	 * The method helps compare the precedence of two operators
	 * 
	 * @param str
	 * @return 1 for the lowest precedence and 3 is the highest precedence
	 */
	public static int evaluatePre(String str) {
		switch (str) {
		case "+":
		case "-":
			return 1;

		case "*":
		case "/":
		case "%":
			return 2;

		case "^":
			return 3;
		}
		return -1;
	}

	/**
	 * Check if the token is operand
	 * 
	 * @param str
	 * @return true if the token is operand
	 */
	public static boolean isOperand(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Check the error for no opening and no closing parenthesis
	 * 
	 * @param str
	 * @return error
	 */
	public static String checkParenthesis(String str) {
		String result = "";
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ') {
				continue;
			} else {
				if (str.charAt(i) == '(') {
					stack.push(str.charAt(i));
				} else if (str.charAt(i) == ')') {
					if (stack.isEmpty()) {
						result = "Error: no opening parenthesis detected";
						checkError = true;
						return result;
					} else {
						stack.pop();
					}
				}
			}
		}
		if (!stack.isEmpty()) {
			result = "Error: no closing parenthesis detected";
			checkError = true;
			return result;
		}
		return result;

	}

	/**
	 * Check error for too many operand
	 * 
	 * @param str
	 * @return error
	 */
	public static String checkOperand(String str) {
		String result = "";
		String[] tokens = str.split(" ");
		int n = tokens.length;
		for (int k = 0; k < n - 1; k++) {
			if (tokens[k].equals(")") || tokens[k].equals("(")) {

				for (int j = k; j < n - 1; j++) {
					tokens[j] = tokens[j + 1];
				}
				n--;
			}

			for (int i = 0; i < n - 1; i++) {
				if (isOperand(tokens[i]) && isOperand(tokens[i + 1])) {
					result = "Error: too many operands" + "(" + tokens[i + 1] + ")";
					checkError = true;
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * Check whether the token is operator
	 * 
	 * @param str
	 * @return true if the token is operator
	 */
	public static boolean isOperator(String str) {
		if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/") || str.equals("%")
				|| str.equals("^")) {
			return true;
		}
		return false;
	}

	/**
	 * Check the error for too many operator
	 * 
	 * @param str
	 * @return error
	 */
	public static String checkOperator(String str) {
		String result = "";
		String[] tokens = str.split(" ");
		for (int j = tokens.length - 1; j > 0; j--) {
			if (tokens[j].equals(")") || tokens[j].equals("(")) {
				continue;
			}
			if (isOperator(tokens[j])) {
				result = "Error: too many operator" + "(" + tokens[j] + ")";
				checkError = true;
				return result;
			} else {
				break;
			}
		}
		for (int i = 0; i < tokens.length - 1; i++) {
			if ((isOperator(tokens[i]) && isOperator(tokens[i + 1]))) {
				result = "Error: too many operator" + "(" + tokens[i + 1] + ")";
				checkError = true;
				return result;
			}
		}
		return result;
	}

}