package cse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import standards.ASTNode;
import standards.NodeType;

public class DeltaNode extends ASTNode {
    private Environment environment;
    private Stack<ASTNode> body;
    private List<String> variables;
    private int index;

    public DeltaNode() {
        this.setType(NodeType.DELTA);
        this.setBody(new Stack<ASTNode>());
        this.setVariables(new ArrayList<String>());
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<String> getVariables() {
        return variables;
    }

    public void setVariables(List<String> variables) {
        this.variables = variables;
    }

    public Stack<ASTNode> getBody() {
        return body;
    }

    public void setBody(Stack<ASTNode> body) {
        this.body = body;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void addVariables(String variable) {
        this.variables.add(variable);
    }

}