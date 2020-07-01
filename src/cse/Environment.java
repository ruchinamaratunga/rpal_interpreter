package cse;

import java.util.HashMap;
import java.util.Map;

import standards.ASTNode;

public class Environment {
    private Map<String, ASTNode> variableMap;
    private Environment parent;

    public Environment() {
        variableMap = new HashMap<String, ASTNode>();
    }

    public Environment getParent() {
        return parent;
    }

    public void setParent(Environment parent) {
        this.parent = parent;
    }

    public ASTNode getValue(String key) {
        ASTNode tempValue = null;
        Map<String, ASTNode> map = variableMap;

        tempValue = map.get(key);

        if (tempValue != null) {
            try {
                return tempValue.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        } 
        if (parent != null) {
            return parent.getValue(key);
        } else {
            return null;
        }
    }

    public void addMapping(String key, ASTNode value) {
        this.variableMap.put(key,value);
    }
}