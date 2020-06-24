package standards;

import java.util.List;

public class ASTNode {
    private NodeType type;
    private List<ASTNode> children;
    private ASTNode parent;


    public NodeType getType() {
        return type;
    }

    public ASTNode getParent() {
        return parent;
    }

    public List<ASTNode> getChildren() {
        return children;
    }

    public void setChildren(List<ASTNode> children) {
        this.children = children;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }
}