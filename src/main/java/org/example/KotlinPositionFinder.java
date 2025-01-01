package org.example;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class KotlinPositionFinder {

    private ParseTree tree;

    public KotlinPositionFinder(String code) {
        KotlinLexer lexer = new KotlinLexer(CharStreams.fromString(code));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KotlinParser parser = new KotlinParser(tokens);
        this.tree = parser.kotlinFile();
    }

    private void findContext(int line, int charPositionInLine, JavaBreakpointLocation javaBreakpointLocation) {
        ParseTree node = findNodeAt(tree, line, charPositionInLine);
        setContext(node, javaBreakpointLocation);
    }

    private ParseTree findNodeAt(ParseTree tree, int line, int charPositionInLine) {
        if (tree instanceof TerminalNode terminalNode) {
            Token token = terminalNode.getSymbol();
            if (token.getLine() == line) {
                if (charPositionInLine == -1 || (token.getCharPositionInLine() <= charPositionInLine && token.getCharPositionInLine() + token.getText().length() > charPositionInLine)) {
                    return tree;
                }
            }
        }

        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            ParseTree result = findNodeAt(child, line, charPositionInLine);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private void setContext(ParseTree node, JavaBreakpointLocation javaBreakpointLocation) {
        String methodName = "";
        String className = "";
        while (node != null) {
            if (node instanceof RuleContext ruleContext) {
                if (ruleContext instanceof KotlinParser.ClassDeclarationContext) {
                    if (className.isEmpty()) {
                        KotlinParser.ClassDeclarationContext classDeclarationContext = (KotlinParser.ClassDeclarationContext) ruleContext;
                        className = classDeclarationContext.simpleIdentifier().getText();
                    }
                } else if (ruleContext instanceof KotlinParser.FunctionDeclarationContext functionDeclarationContext) {
                    methodName = functionDeclarationContext.identifier().getText();
                } else if (ruleContext instanceof KotlinParser.PropertyDeclarationContext propertyContext) {
                    ParseTree lastChild = ruleContext.getChild(ruleContext.getChildCount() - 1);
                    if (lastChild.getText().startsWith("{")) {
                        KotlinParser.VariableDeclarationContext varDec = propertyContext.variableDeclaration();
                        if (varDec != null && varDec.simpleIdentifier() != null) {
                            methodName = varDec.simpleIdentifier().getText();
                        }
                    }
                } else if (ruleContext instanceof KotlinParser.AnonymousInitializerContext anonymousInitializer) {
                    methodName = anonymousInitializer.INIT().getText();
                } else if (ruleContext instanceof KotlinParser.SecondaryConstructorContext secondaryConstructorContext) {
                    methodName = secondaryConstructorContext.CONSTRUCTOR().getText();
                }
            }
            node = node.getParent();
        }
        javaBreakpointLocation.setMethodName(methodName);
        javaBreakpointLocation.setClassName(className);
    }

    public JavaBreakpointLocation locateBreakpoint(int line, int charPositionInLine) {
        JavaBreakpointLocation javaBreakpointLocation = new JavaBreakpointLocation();
        javaBreakpointLocation.setColumnNumber(charPositionInLine);
        javaBreakpointLocation.setLineNumber(line);
        this.findContext(line, charPositionInLine, javaBreakpointLocation);
        return javaBreakpointLocation;
    }
}
