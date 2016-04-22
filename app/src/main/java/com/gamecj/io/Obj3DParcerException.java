package com.gamecj.io;

public class Obj3DParcerException extends Exception {
	public static final String ERROR_VERTEX1 = "error parcer in VERTEX position not float element 1";
	public static final String ERROR_VERTEX2 = "error parcer in VERTEX position not float element 2";
	public static final String ERROR_VERTEX3 = "error parcer in VERTEX position not float element 3";
	
	public static final String ERROR_NORMAL1 = "error parcer in NORMAL position not float element 1";
	public static final String ERROR_NORMAL2 = "error parcer in NORMAL position not float element 2";
	public static final String ERROR_NORMAL3 = "error parcer in NORMAL position not float element 3";
	
	public static final String ERROR_TEXTCOORD1 = "error parcer in TEXTCOORD position not float element 1";
	public static final String ERROR_TEXTCOORD2 = "error parcer in TEXTCOORD position not float element 2";
	
	public static final String ERROR_INDEX = "error parcer in INDEX position UNKNOWN index";
	
	private int lineNumber = 0;
	private String fileObj3dName;
	private static final long serialVersionUID = 1L;
	public Obj3DParcerException(String message) {super(message);}
	public Obj3DParcerException(String message, int lineNumber) {super(message); this.lineNumber = lineNumber;}
	public Obj3DParcerException(String message, int lineNumber, String fileName) {super(message); this.lineNumber = lineNumber; fileObj3dName = fileName;}
	public Obj3DParcerException(String message, String fileName) {super(message); fileObj3dName = fileName;}
	public int getLineNumber() { return lineNumber; }
	public String getFileName() { return fileObj3dName; }
}
