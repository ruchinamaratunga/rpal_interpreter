package cse;

import standards.ASTNode;
import standards.NodeType;

public class Eta extends ASTNode{
    private DeltaNode delta;

    public Eta() {
        this.setType(NodeType.ETA);
    }

    @Override
    public String getValue() {
        return "[eta closure: "+delta.getVariables().get(0)+": "+delta.getIndex()+"]";
    }

    public DeltaNode getDelta() {
        return this.delta;
    }

    public void setDelta(DeltaNode delta) {
        this.delta = delta;
    }
}