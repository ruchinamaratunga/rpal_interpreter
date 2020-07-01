package standards;

public enum NodeType {

    IDENTIFIER("<ID:%s>"),
    STRING("<STR:'%s'>"),
    INTEGER("<INT:%s>"),
    
 
    LET("let"),
    LAMBDA("lambda"),
    WHERE("where"),
    
    TAU("tau"),
    AUG("aug"),
    CONDITIONAL("->"),
   
    OR("or"),
    AND("&"),
    NOT("not"),
    GR("gr"),
    GE("ge"),
    LS("ls"),
    LE("le"),
    EQ("eq"),
    NE("ne"),
    
    PLUS("+"),
    MINUS("-"),
    NEG("neg"),
    MULT("*"),
    DIV("/"),
    EXP("**"),
    AT("@"),
    GAMMA("gamma"),
    TRUE("<true>"),
    FALSE("<false>"),
    NIL("<nil>"),
    DUMMY("<dummy>"),
    WITHIN("within"),
    SIMULTDEF("and"),
    REC("rec"),
    EQUAL("="),
    FCNFORM("function_form"),
    PAREN("<()>"),
    COMMA(","),
    YSTAR("<Y*>"),
    BETA(""),
    DELTA(""),
    ETA(""),
    TUPLE("");

    private String value;

    private NodeType(final String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }

    public static NodeType getType(String valueString) {
        for(NodeType v : values()){
            if (valueString.matches("<ID:.*>")) {
                return NodeType.IDENTIFIER;
            }
            if (valueString.matches("<STR:.*>")) {
                return NodeType.STRING;
            }
            if (valueString.matches("<INT:.*>")) {
                return NodeType.INTEGER;
            }
            if( v.getValue().equals(valueString)){
                return v;
            }
        }
        return null;
    }
}
