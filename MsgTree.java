package edu.iastate.cs228.hw3;

import java.util.Stack;

/**
 * 
 * @author Will Roepsch
 *
 */
public class MsgTree {
	public char payloadChar;
	public MsgTree left;
	public MsgTree right;
	// Need static char idx to the tree string for recursive solution
	private static int staticCharIdx = 0;

	/**
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString string pulled from data file
	 */
	public MsgTree(String encodingString) {
		if (encodingString.length() < 2 || encodingString == null) {		//building tree and testing if no info in file
			return;
		}
		Stack<MsgTree> stack = new Stack<>();
		int index = 0;
		this.payloadChar = encodingString.charAt(index++);
		MsgTree current = this;
		String last = "in";
		stack.push(this);
		while (index < encodingString.length()) {
			MsgTree node = new MsgTree(encodingString.charAt(index++));
			if (last.equals("in")) {
				current.left = node;
				if (node.payloadChar == '^') {
					current = stack.push(node);
					last = "in";
				} else {
					if (!stack.empty())
						current = stack.pop();
					last = "out";
				}
			} else { 
				current.right = node;
				if (node.payloadChar == '^') {
					current = stack.push(node);
					last = "in";
				} else {
					if (!stack.empty())
						current = stack.pop();
					last = "out";
				}
			}
		}
	}

	/**
	 * Constructor for a single node with null children
	 * 
	 * @param payloadChar
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/**
	 * Method to print characters and their binary codes
	 * 
	 * @param root
	 * @param code
	 */
	public static void printCodes(MsgTree root, String code) {
		System.out.println("character code\n-------------------------");			//reads spaces and returns
		for (char symbol : code.toCharArray()) {
			getCode(root, symbol, binCode = "");
			System.out.println("    " + (symbol == '\n' ? "\\n" : symbol + " ") + "    " + binCode);
		}
	}

	private static String binCode;

	/**
	 * Gets code and recursivly calls itself setting the alphabet
	 * 
	 * @param root
	 * @param ch
	 * @param path
	 * @return
	 */
	private static boolean getCode(MsgTree root, char ch, String path) {
		if (root != null) {
			if (root.payloadChar == ch) {
				binCode = path;
				return true;
			}
			return getCode(root.right, ch, path + "1")|| getCode(root.left, ch, path + "0");
		}
		return false;
	}

	/**
	 * Decodes message using the pulled code alphabet
	 * 
	 * @param codes
	 * @param msg
	 */
	public void decode(MsgTree codes, String msg) {
		System.out.println("MESSAGE:");					//reads spaces and will get rid of unnessary returnes or add as needed
		MsgTree current = codes;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);
			current = (ch == '0' ? current.left : current.right);
			if (current.payloadChar != '^') {
				getCode(codes, current.payloadChar, binCode = "");
				sb.append(current.payloadChar);
				current = codes;
			}
		}
		System.out.println(sb.toString());
		statistc(msg, sb.toString());
	}

	/**
	 * Extra credit statistics. Pulls the encoded and decoded strings data to print
	 * 
	 * @param encodeStr
	 * @param decodeStr
	 */
	private void statistc(String encodeStr, String decodeStr) {
		System.out.println("STATISTICS:");
		System.out.println(String.format("Avg bits/char:\t%.1f", encodeStr.length() / (double) decodeStr.length()));			//does easy math to return stats
		System.out.println("Total Characters:\t" + decodeStr.length());
		System.out.println(
				String.format("Space Saving:\t%.1f%%", (1d - decodeStr.length() / (double) encodeStr.length()) * 100));
	}
}



/*main file should return 
 *  what is the file that you want to decode
 *  decode()      complete
 * print print codes
 * print "MESSAGE:"decoded message
 * 
 */
