package cse;

import standards.ASTNode;
import standards.NodeType;

public class TupleNode extends ASTNode {
    
    public TupleNode() {
        setType(NodeType.TUPLE);
    }

    @Override
    public String getValue() {
        if (this.getChildren().size() != 0) {
            return "nil";
        }
        String output = "(";
        for (int i = 0; i < this.getChildren().size()-1;i++) {
            output += this.getChildren().get(i).getValue() + ", ";
        }
        output += this.getChildren().get(this.getChildren().size()-1);
        output += " )";
        return output;
    }
}