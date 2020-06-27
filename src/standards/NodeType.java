package standards;

public enum NodeType {

    IDENTIFIER("<ID:%s>"),
    STRING("<STR:'%s'>"),
    INTEGER("<INT:%s>"),
    
    //Expressions
    LET("let"),
    LAMBDA("lambda"),
    WHERE("where"),
    
    //Tuple expressions
    TAU("tau"),
    AUG("aug"),
    CONDITIONAL("->"),
    
    //Boolean Expressions
    OR("or"),
    AND("&"),
    NOT("not"),
    GR("gr"),
    GE("ge"),
    LS("ls"),
    LE("le"),
    EQ("eq"),
    NE("ne"),
    
    //Arithmetic Expressions
    PLUS("+"),
    MINUS("-"),
    NEG("neg"),
    MULT("*"),
    DIV("/"),
    EXP("**"),
    AT("@"),
    
    //Rators and Rands
    GAMMA("gamma"),
    TRUE("<true>"),
    FALSE("<false>"),
    NIL("<nil>"),
    DUMMY("<dummy>"),
    
    //Definitions
    WITHIN("within"),
    SIMULTDEF("and"),
    REC("rec"),
    EQUAL("="),
    FCNFORM("function_form"),
    
    //Variables
    PAREN("<()>"),
    COMMA(","),

    YSTAR("<Y*>");
    
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
