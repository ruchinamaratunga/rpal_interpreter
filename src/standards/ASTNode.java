package standards;

import java.util.ArrayList;

public class ASTNode {
    private String type;
    private ArrayList<ASTNode> children;
    private ASTNode parent;
    private long count;
    private int linenumber;

    public ASTNode(String type, ArrayList<ASTNode> children, ASTNode parent, long count, int i) {
        this.type = type;
        this.children = children;
        this.parent = parent;
        this.count = count;
        this.linenumber = i;
    }

    public int getLinenumber() {
        return linenumber;
    }

    public String getType() {
        return type;
    }

    public long getCount() {
        return count;
    }

    public ASTNode getParent() {
        return parent;
    }

    public ArrayList<ASTNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ASTNode> children) {
        this.children = children;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }
    
    public void setCount(long count) {
        this.count = count;
    }

    public void addChild(ASTNode child) {
        this.children.add(child);
    }
    
    public void setLinenumber(int linenumber) {
        this.linenumber = linenumber;
    }
}