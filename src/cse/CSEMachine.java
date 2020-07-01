package cse;

import java.util.Stack;

import standards.AST;
import standards.ASTNode;
import standards.NodeType;

public class CSEMachine {
    private ControlStructures controlStructures;
    private Stack<ASTNode> stack;

    public CSEMachine(AST ast) {
        this.controlStructures = new ControlStructures(ast.getRootNode());
        this.stack = new Stack<ASTNode>();
        
    }

    public void evaluate() {
        this.controlStructures.buildAExpresssion();
        printDeltas();
        this.evaluateControlStructure(this.controlStructures.getRootDelta(), this.controlStructures.getRootDelta().getEnvironment());       
    }

    private void evaluateControlStructure(DeltaNode rootDelta, Environment rootEnv) {
        Stack<ASTNode> control = new Stack<ASTNode>();
        control.addAll(rootDelta.getBody());
        while(!control.isEmpty()) {
            buildCurrentNode(rootDelta, rootEnv, control);
        }
    }

    public void buildCurrentNode(DeltaNode currentDelta,Environment currentEnv, Stack<ASTNode> currentControl) {
        ASTNode node = currentControl.pop();
        if(binaryOperator(node)){
            return;
        } else if(uniryOperator(node)) {
            return;
        } else {
            switch(node.getType()) {
                case IDENTIFIER:
                    System.out.println(node.getStringValue());
                    if(currentEnv.getValue(node.getValue()) != null) {
                        this.stack.push(currentEnv.getValue(node.getValue()));
                    } else if(isBuiltinfunc(node.getValue())) {
                        this.stack.push(node);
                    } else {
                        System.out.println("Error1");
                    }
                    break;

                case NIL:
                case TAU:
                    handleTuple(node);
                    break;

                case BETA:  
                    handleBetaNode((BetaNode) node, currentControl);
                    break;

                case GAMMA:
                    handleGammaNode(currentDelta, node, currentEnv, currentControl);
                    break;

                case DELTA:
                    ((DeltaNode)node).setEnvironment(currentEnv);
                    this.stack.push(node);
                    break;
                default:
                    this.stack.push(node);
					break;
            }
        }
    }

    private void handleGammaNode(DeltaNode currentDelta, ASTNode node, Environment currentEnv, Stack<ASTNode> currentControl) {
        ASTNode value1 = this.stack.pop();
        ASTNode value2 = this.stack.pop();

        if(value1.getType() == NodeType.DELTA) {
            // System.out.println(((DeltaNode)value1).getBody());
            DeltaNode deltanode = (DeltaNode) value1;

            Environment newEnv = new Environment();
            newEnv.setParent(deltanode.getEnvironment());

            if(deltanode.getVariables().size() == 1) {
                newEnv.addMapping(deltanode.getVariables().get(0), value2);
            } else {
                if(value2.getType() != NodeType.TUPLE) {
                    System.out.println("Error11");
                }

                for(int i= 0; i < deltanode.getVariables().size(); i++) {
                    newEnv.addMapping(deltanode.getVariables().get(i), value2.getChildren().get(i));
                }
            }
            this.evaluateControlStructure(deltanode,newEnv);
            return;
        } else if(value1.getType() == NodeType.YSTAR) {
            if (value2.getType() != NodeType.DELTA) {
                System.out.println("Error22");
            }

            Eta etaNode = new Eta();
            etaNode.setDelta((DeltaNode) value2);
            this.stack.push(etaNode);
            return;
        } else if (value1.getType() == NodeType.ETA) {
            this.stack.push(value2);
            this.stack.push(value1);
            this.stack.push(((Eta) value1).getDelta());
            currentControl.push(node);
            currentControl.push(node);
            return;
        } else if(value1.getType() == NodeType.TUPLE) {
            // System.out.println(value1.getChildren().toString());
            
            this.selectTuple((TupleNode)value1,value2);
            return;
        } else if (handleReservedIdentifiers(value1, value2, currentControl)) {
            return;
        } else {
            System.out.println("Error2");
        }
    }

    private void selectTuple(TupleNode value1, ASTNode value2) {
        if(value2.getType() !=NodeType.INTEGER) {
            System.out.println("Error3");
        }

        ASTNode output = value1.getChildren().get(Integer.parseInt(value2.getValue()));
        if(output == null) {
            System.out.println("Error4");
        }
        this.stack.push(output);
    }

    private boolean handleReservedIdentifiers(ASTNode value1, ASTNode value2, Stack<ASTNode> currentControl) {
        switch(value1.getValue()) {
            case "Isinteger":
                this.checkIsTrue(value2, NodeType.INTEGER);
                return true;
            case "Isstring":
                this.checkIsTrue(value2, NodeType.STRING);
                return true;
            case "Isdummy":
                this.checkIsTrue(value2, NodeType.DUMMY);
                return true;
            case "Isfunction":
                this.checkIsTrue(value2, NodeType.DELTA);
                return true;
            case "Istuple":
                this.checkIsTrue(value2, NodeType.TUPLE);
                return true;
            case "Istruthvalue":
                if(value2.getType()==NodeType.TRUE || value2.getType() == NodeType.FALSE) {
                    pushTrueNode();
                } else {
                    pushFalseNode();
                }
                return true;
            case "Stem":
                if(value2.getType() != NodeType.STRING) {
                    System.out.println("Error5");
                }
                if(value2.getValue().isEmpty()) {
                    value2.setValue("");
                } else {
                    value2.setValue(value2.getValue().substring(0,1));
                }
                this.stack.push(value2);
                return true;
            case "Stern":
                if(value2.getType() != NodeType.STRING) {
                    System.out.println("Error6");
                }
                if(value2.getValue().isEmpty() || value2.getValue().length() == 1) {
                    value2.setValue("");
                } else {
                    value2.setValue(value2.getValue().substring(1));
                }
                this.stack.push(value2);
                return true;
            case "Conc":
            case "conc":
                currentControl.pop();
                ASTNode value3 = this.stack.pop();
                if(value2.getType() != NodeType.STRING || value3.getType()!= NodeType.STRING) {
                    System.out.println("Error7");
                } 
                ASTNode output = new ASTNode();
                output.setType(NodeType.STRING);
                output.setValue(value2.getValue()+value3.getValue());
                this.stack.push(output);
                return true;
            case "Print":
            case "print":
                String output2 =value2.getValue();
                // System.out.println(value2.getType().toString());
                output2 = output2.replace("\\t","\t");
                output2 = output2.replace("\\n", "\n");
                System.out.print(output2);
                ASTNode dummy = new ASTNode();
                dummy.setType(NodeType.DUMMY);
                this.stack.push(dummy);
                return true;
            case "ItoS":
                if(value2.getType() != NodeType.INTEGER) {
                    System.out.println("Error8");
                }
                value2.setType(NodeType.STRING);
                this.stack.push(value2);
                return true;
            case "Order":
                // System.out.println(value2.getType().toString());
                if(value2.getType() != NodeType.TUPLE) {
                    System.out.println("Error9");
                }
                ASTNode output1 = new ASTNode();
                output1.setType(NodeType.INTEGER);
                output1.setValue(Integer.toString(value2.getChildren().size()));
                this.stack.push(output1);
                return true;
            case "Null":
                if(value2.getType()!=NodeType.TUPLE) {
                    System.out.println("Error33");
                }
                if(value2.getChildren().size() == 0) {
                    pushTrueNode();
                } else {
                    pushFalseNode();
                }
                return true;
            default:
                return false;
        }
        
    }

    private void checkIsTrue(ASTNode value2, NodeType type) {
        if(value2.getType()==type) {
            pushTrueNode();
        } else {
            pushFalseNode();
        }
    }

    private void handleBetaNode(BetaNode node, Stack<ASTNode> control) {
        ASTNode value = this.stack.pop();

        if(value.getType() != NodeType.TRUE && value.getType() != NodeType.FALSE) {
            System.out.println("Error44");
        }

        if(value.getType()== NodeType.TRUE) {
            control.addAll(node.getThen());
        } else {
            control.addAll(node.getElsen());
        }
    }

    private void handleTuple(ASTNode node) {
        // System.out.println(node.getChildren().size());
        TupleNode tuplenode = new TupleNode();
        if(node.getChildren().size()==0) {
            this.stack.push(tuplenode);
            return;
        }
        for (int i=0;i<node.getChildren().size();i++) {
            this.stack.pop();
        }
        tuplenode.setChildren(node.getChildren());
        this.stack.push(tuplenode);
    }

    private boolean uniryOperator(ASTNode node) {
        switch(node.getType()) {
            case NOT:
                ASTNode value = this.stack.pop();
                if(value.getType() != NodeType.TRUE && value.getType() != NodeType.FALSE) {
                    System.out.println("Error55");
                }

                if(value.getType() == NodeType.TRUE) {
                    pushFalseNode();
                } else{
                    pushTrueNode();
                }
                return true;
            case NEG:
                ASTNode value1 = this.stack.pop();
                if(value1.getType() != NodeType.INTEGER) {
                    System.out.println("Error66");
                }

                ASTNode output = new ASTNode();
                output.setType(NodeType.INTEGER);
                output.setValue(Integer.toString(-1*Integer.parseInt(value1.getValue())));
                stack.push(output);
                return true;

            default:
                return false;
        }
    }

    private boolean binaryOperator(ASTNode node) {
        switch (node.getType()) {
            case PLUS:
            case MINUS:
            case MULT:
            case DIV:
            case EXP:
            case LS:
            case LE:
            case GR:
            case GE:
                this.arithmeticOperators(node.getType());
                return true;

            case EQ:
            case NE:
                this.logicalOperators(node.getType());
                return true;
            case OR:
            case AND:
                this.logicOperators(node.getType());
                return true;

            case AUG:
                tupleOperator();
                return true;
            
            default:
                return false;
                
        }
    }

    private boolean isBuiltinfunc(String value) {
        switch(value) {
            case "Isinteger":
            case "Isstring":
            case "Istuple":
            case "Isdummy":
            case "Istruthvalue":
            case "Isfunction":
            case "ItoS":
            case "Order":
            case "Conc":
            case "conc": 
            case "Stern":
            case "Stem":
            case "Null":
            case "Print":
            case "print": 
            case "neg":
        return true;
        }
    return false;
    }

    private void tupleOperator() {
        ASTNode value1 = stack.pop();
        ASTNode value2 = stack.pop();
        
        if(value1.getType()!=NodeType.TUPLE) {
            System.out.println("Error77");
        }

        value1.addChild(value2);
        this.stack.push(value1);
    }

    private void logicOperators(NodeType type) {
        ASTNode value1 = stack.pop();
        ASTNode value2 = stack.pop();

        if((value1.getType()==NodeType.TRUE || value1.getType()==NodeType.FALSE) &&
        (value2.getType()==NodeType.TRUE || value2.getType()==NodeType.FALSE)) {
            if(type==NodeType.OR){
                if(value1.getType()==NodeType.TRUE || value2.getType()==NodeType.TRUE)
                  pushTrueNode();
                else
                  pushFalseNode();
            } else {
                if(value1.getType()==NodeType.TRUE && value2.getType()==NodeType.TRUE)
                  pushTrueNode();
                else
                  pushFalseNode();
            }
            return;
        }
        System.out.println("Error88");
    }

    private void logicalOperators(NodeType type) {
        ASTNode value1 = stack.pop();
        ASTNode value2 = stack.pop();

        if(value1.getType()== NodeType.TRUE || value1.getType() == NodeType.FALSE) {
            if(value1.getType() != NodeType.TRUE || value1.getType() != NodeType.FALSE) {
                System.out.println("Not compatible type");
            }
            if (value1.getType() == value2.getType()) {
                if(type == NodeType.EQ) {
                    pushTrueNode();
                } else {
                    pushFalseNode();
                } 
            } else {
                if(type == NodeType.EQ) {
                    pushFalseNode();
                } else {
                    pushTrueNode();
                }
            }
            return;
        }
        if (value1.getType() != value2.getType()) {
            System.out.println("Error99");
        }

        if(value1.getType() == NodeType.STRING) {
            if (value1.getValue().equals(value2.getValue())) {
                if(type == NodeType.EQ) {
                    pushTrueNode();
                } else {
                    pushFalseNode();
                } 
            } else {
                if(type == NodeType.EQ) {
                    pushFalseNode();
                } else {
                    pushTrueNode();
                }
            }
        } else if (value1.getType() == NodeType.INTEGER) {
            if (Integer.parseInt(value1.getValue())==Integer.parseInt(value2.getValue())) {
                if(type == NodeType.EQ) {
                    pushTrueNode();
                } else {
                    pushFalseNode();
                } 
            } else {
                if(type == NodeType.EQ) {
                    pushFalseNode();
                } else {
                    pushTrueNode();
                }
            }
        } else {
            System.out.println("Error00");
        }
    }


    private void arithmeticOperators(NodeType type) {
        ASTNode value1 = stack.pop();
        ASTNode value2 = stack.pop();

        // if(value1.getType() != NodeType.INTEGER || value2.getType() != NodeType.INTEGER) {
        //     System.out.println("Error in Operators");
        // }

        ASTNode result = new ASTNode();
        result.setType(NodeType.INTEGER);

        switch(type) {

            case PLUS:
                result.setValue(Integer.toString(Integer.parseInt(value1.getValue())+Integer.parseInt(value2.getValue())));
                break;

            case MINUS:
                result.setValue(Integer.toString(Integer.parseInt(value1.getValue())-Integer.parseInt(value2.getValue())));
                break;

            case MULT:
                result.setValue(Integer.toString(Integer.parseInt(value1.getValue())*Integer.parseInt(value2.getValue())));
                break;

            case DIV:
                result.setValue(Integer.toString(Integer.parseInt(value1.getValue())/Integer.parseInt(value2.getValue())));
                break;

            case EXP:
                result.setValue(Integer.toString((int) Math.pow(Integer.parseInt(value1.getValue()), Integer.parseInt(value2.getValue()))));
                break;

            case LS:
                if(value1.getValue().charAt(0) < value2.getValue().charAt(0)) {
                    this.pushTrueNode();
                } else {
                    this.pushFalseNode();
                }
                return;
            
            case LE:
                if(value1.getValue().charAt(0) <= value2.getValue().charAt(0)) {
                    this.pushTrueNode();
                } else {
                    this.pushFalseNode();
                }
                return;

            case GR:
                if(value1.getValue().charAt(0) > value2.getValue().charAt(0)) {
                    this.pushTrueNode();
                } else {
                    this.pushFalseNode();
                }
                return;
            
            case GE:
                if (value1.getValue().charAt(0) >= value2.getValue().charAt(0)) {
                    this.pushTrueNode();
                } else {
                    this.pushFalseNode();
                }
                return;
            default:
                break;
        }
        stack.push(result);
        return;
    }

    private void pushTrueNode() {
        ASTNode trueNode = new ASTNode();
        trueNode.setValue("true");
        trueNode.setType(NodeType.TRUE);
        stack.push(trueNode);
    }

    private void pushFalseNode() {
        ASTNode falseNode = new ASTNode();
        falseNode.setValue("false");
        falseNode.setType(NodeType.FALSE);
        stack.push(falseNode);
    }

    public void printDeltas() {
        for(DeltaNode delta: this.controlStructures.getDeltas()) {
            System.out.format("Delta%d : ", delta.getIndex());
            for(ASTNode node: delta.getBody()) {
                System.out.print(node.getType().toString()+"["+node.getValue()+"]"+" ");
            }
            System.out.println("");
        }
    }

    public void printDelta(DeltaNode deltanode) {
            System.out.format("Delta%d : ", deltanode.getIndex());
            for(ASTNode node: deltanode.getBody()) {
                System.out.print(node.getType().toString()+" ");
            }
            System.out.println("");
    }
}