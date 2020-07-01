package cse;

import java.util.Stack;

import standards.ASTNode;
import standards.NodeType;

public class BetaNode extends ASTNode {
    private Stack<ASTNode> then;
    private Stack<ASTNode> elsen;

    public BetaNode() {
        setType(NodeType.BETA);
        setThen(new Stack<ASTNode>());
        setElsen(new Stack<ASTNode>());
    }

    public Stack<ASTNode> getElsen() {
        return elsen;
    }

    public void setElsen(Stack<ASTNode> elsen) {
        this.elsen = elsen;
    }

    public Stack<ASTNode> getThen() {
        return then;
    }

    public void setThen(Stack<ASTNode> then) {
        this.then = then;
    }


}