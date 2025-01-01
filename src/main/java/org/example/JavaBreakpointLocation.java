package org.example;

public class JavaBreakpointLocation {
    /**
     * The line number in the source file.
     */
    private int lineNumberInSourceFile = Integer.MIN_VALUE;
    /**
     * The line number in the class file.
     */
    private int lineNumber;
    /**
     * The source column of the breakpoint.
     */
    private int columnNumber = -1;
    /**
     * The declaring class name that encloses the target position.
     */
    private String className;
    /**
     * The method name and signature when the target position
     * points to a method declaration.
     */
    private String methodName;
    private String methodSignature;


    public int lineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int columnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String className() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String methodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String methodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    public int lineNumberInSourceFile() {
        return lineNumberInSourceFile == Integer.MIN_VALUE ? lineNumber : lineNumberInSourceFile;
    }

    public void setLineNumberInSourceFile(int lineNumberInSourceFile) {
        this.lineNumberInSourceFile = lineNumberInSourceFile;
    }

    public void print() {
        System.out.println("methodName: " + methodName);
        System.out.println("className " + className);
    }
}
