package standards;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ASTNode {
    private String stringValue;
    private NodeType type;
    private String value;
    private ArrayList<ASTNode> children;
    private ASTNode parent;
    private long count;
    private int lineNumber;

    public ASTNode(String stringValue, ArrayList<ASTNode> children, ASTNode parent, long count, int lineNumber) {
        this.stringValue = stringValue;
        this.children = children;
        this.parent = parent;
        this.count = count;
        this.lineNumber = lineNumber;
        this.setTypeValue();
    }

    public ASTNode(String stringValue, ArrayList<ASTNode> children, ASTNode parent) {
        this.stringValue = stringValue;
        this.children = children;
        this.parent = parent;
        this.setTypeValue();
    }

    public String getValue() {
        return this.value;
    }

    public int getLinenumber() {
        return this.lineNumber;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public long getCount() {
        return this.count;
    }

    public ASTNode getParent() {
        return this.parent;
    }

    public NodeType getType() {
        return this.type;
    }

    public ArrayList<ASTNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<ASTNode> children) {
        this.children = children;
    }

    public void setStringValue(String type) {
        this.stringValue = type;
    }

    public void setParent(ASTNode parent) {
        this.parent = parent;
    }
    
    public void setCount(long count) {
        this.count = count;
    }
    
    public void setLinenumber(int linenumber) {
        this.lineNumber = linenumber;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void addChild(ASTNode child) {
        this.children.add(child);
    }

    public void setTypeValue() {
        this.type = NodeType.getType(this.stringValue);
        this.value = this.setValueValue();
    }

    private String setValueValue() {
        if (this.type == NodeType.IDENTIFIER || this.type == NodeType.STRING || this.type == NodeType.INTEGER) {
            String temp = this.stringValue;
            temp.replace("<", "");
            temp.replace(">", "");
            String[] items = temp.split(":");
            return (String) Array.get(items, 0);
        }
        return null;
    }

    public void changeChild(int index,ASTNode newChild) {
        this.children.set(index,newChild);
    }

    public void removeChild(int index) {
        this.children.remove(index);
    }

    public void removeAllChildren() {
        this.children = new ArrayList<ASTNode>();
    }
}