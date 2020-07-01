package standards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AST {
    private ASTNode rootnode;
    private ArrayList<ArrayList<Object>> nodeDetails;

    public AST(ASTNode rootnode, ArrayList<ArrayList<Object>> nodeDetails) {
        this.rootnode = rootnode;
        this.nodeDetails = nodeDetails;
    }
    
    public ASTNode getRootNode() {
        return this.rootnode;
    }

    public void ASTBuild() {
        this.nodebuild(this.rootnode, this.nodeDetails, 0, this.nodeDetails.size());
    }
    
    public void ASTStandardize() {
        this.standardize(this.rootnode);
    }

    public void ASTPrintTree() {
        this.printTree(this.rootnode, 0);
    }

    private void nodebuild(ASTNode node, List<ArrayList<Object>> list, int k, int l) {
        // System.out.println(node.getStringValue());
        ArrayList<Integer> childlines = new ArrayList<Integer>();
        for (int i = k; i < l; i++) {
            if ((long) list.get(i).get(1) == node.getCount()+1) {
                ASTNode newNode = new ASTNode((String) list.get(i).get(0), new ArrayList<ASTNode>(), node,
                        (long) list.get(i).get(1),(int) list.get(i).get(2));
                node.addChild(newNode);
                childlines.add(newNode.getLinenumber());
            }
        }

        childlines.add(l);
        for (int j = 0; j < node.getChildren().size(); j++) {
            // List<Integer> new_range = childlines.subList(j, j+2);
            int new_k = childlines.get(j);
            int new_l = childlines.get(j+1);           
            this.nodebuild(node.getChildren().get(j), list, new_k, new_l);
        }
        return;
    }

    private void standardize(ASTNode parentNode) {
        
        if (parentNode.getChildren().size() == 0) {
            return;
        }

        for(ASTNode child: parentNode.getChildren()) {
            // System.out.println(parentNode.getType().toString());
            standardize(child);
        }
        
        
        switch(parentNode.getType()) {
            
            case LET:

                if (parentNode.getChildren().get(0).getType() != NodeType.EQUAL) {
                    System.out.println("LET node is not in the correct format");
                }
                // System.out.println(parentNode.getChildren().toString());
                ASTNode temp = parentNode.getChildren().get(1);
                ASTNode e = parentNode.getChildren().get(0).getChildren().get(1);
                // System.out.println(temp.getType().toString());
                // System.out.println(e.getType().toString());
                // System.out.println(parentNode.getChildren().get(0).getChildren().get(1).getType().toString());
                parentNode.changeChild(1, parentNode.getChildren().get(0).getChildren().get(1));
                parentNode.getChildren().get(0).changeChild(1, temp); 
                parentNode.setType(NodeType.GAMMA);
                parentNode.getChildren().get(0).setType(NodeType.LAMBDA);
                break;

            case WHERE:

                temp = parentNode.getChildren().get(1);
                parentNode.changeChild(1, parentNode.getChildren().get(0));
                parentNode.changeChild(0, temp);
                parentNode.setType(NodeType.LET);
                
                ASTNode pp = parentNode.getChildren().get(1);
                parentNode.changeChild(1, parentNode.getChildren().get(0).getChildren().get(1));
                parentNode.getChildren().get(0).changeChild(1, pp); 
                parentNode.setType(NodeType.GAMMA);
                parentNode.getChildren().get(0).setType(NodeType.LAMBDA);
                break;

            case FCNFORM:

                // System.out.println();
                // parentNode.printDecendant(0);
                int l = parentNode.getChildren().size();
                List<ASTNode> variables = parentNode.getChildren().subList(1,l-1);
                ASTNode expression = parentNode.getChildren().get(l-1);
                ASTNode p = parentNode.getChildren().get(0);
                parentNode.removeAllChildren();
                parentNode.addChild(p);
                // System.out.println(variables.toString());
                // System.out.println(expression.getType().toString());
                this.lambdaLoop(variables,expression, parentNode);
                parentNode.setType(NodeType.EQUAL);
                // parentNode.printDecendant(0);
                break;
            
            // case TAU:
        
            //     ArrayList<ASTNode> children1 = parentNode.getChildren();
            //     parentNode.removeAllChildren();
            //     parentNode.setType(NodeType.GAMMA);
            //     this.tauLoop(parentNode,children1);
            //     break;

            case AT:

                ASTNode e1 = parentNode.getChildren().get(0);
                ASTNode n = parentNode.getChildren().get(1);
                ASTNode e2 = parentNode.getChildren().get(2);
                parentNode.removeChild(2);
                parentNode.removeChild(1);
                parentNode.removeChild(0);
                parentNode.setType(NodeType.GAMMA);
                ArrayList<ASTNode> children = new ArrayList<ASTNode>();
                children.add(n);
                children.add(e1);
                ASTNode gamma = new ASTNode("gamma", children, parentNode);
                e1.setParent(gamma);
                n.setParent(gamma);
                parentNode.addChild(gamma);
                parentNode.addChild(e2);
                break;

            case WITHIN:
         
                if (parentNode.getChildren().get(0).getType() != NodeType.EQUAL || parentNode.getChildren().get(1).getType() != NodeType.EQUAL) {
                    System.out.println("Within node has an error");
                }
                ASTNode eq1 = parentNode.getChildren().get(0);
                ASTNode eq2 = parentNode.getChildren().get(1);
                parentNode.removeChild(1);
                parentNode.removeChild(0);
                ASTNode x1 = eq1.getChildren().get(0);
                ASTNode x2 = eq2.getChildren().get(0);
                parentNode.addChild(x2);
                x2.setParent(parentNode);
                parentNode.setType(NodeType.EQUAL);
                parentNode.addChild(eq1);
                eq1.setType(NodeType.GAMMA); 
                eq1.changeChild(0, eq2);
                eq2.setParent(eq1);
                eq2.setType(NodeType.LAMBDA);  
                eq2.changeChild(0, x1);
                x1.setParent(eq2);
                break;
            
            case REC:
      
                ASTNode eq = parentNode.getChildren().get(0);
                // eq.printDecendant(0);
                ASTNode x = eq.getChildren().get(0);
                ASTNode gammaNode = new ASTNode("gamma", new ArrayList<ASTNode>(), parentNode);
                ASTNode ystarNode = new ASTNode("<Y*>", new ArrayList<ASTNode>(), gammaNode);
                parentNode.removeChild(0);
                parentNode.setType(NodeType.EQUAL);
                parentNode.addChild(x);
                // System.out.println(parentNode.getChildren().get(0).getStringValue().toString());
                x.setParent(parentNode);
                parentNode.addChild(gammaNode);
                gammaNode.addChild(ystarNode);
                gammaNode.addChild(eq);
                eq.setType(NodeType.LAMBDA);
                eq.setParent(gammaNode);
                // return;
                // parentNode.printDecendant(0);
                break;

            case SIMULTDEF:
         
                ASTNode commaNode = new ASTNode(",", new ArrayList<ASTNode>(), parentNode);
                ASTNode tauNode = new ASTNode("tau", new ArrayList<ASTNode>(), parentNode);
                for(ASTNode child: parentNode.getChildren()) {
                    this.simultdefLoop(commaNode,tauNode,child);
                    parentNode.removeChild(0);
                }
                parentNode.addChild(commaNode);
                parentNode.addChild(tauNode);
                parentNode.setType(NodeType.EQUAL);
                break;

            case LAMBDA:
            
                ASTNode var = parentNode.getChildren().get(0);
                ASTNode exp  = parentNode.getChildren().get(1);
                this.lambdaLoop(var.getChildren(), exp, parentNode);
                break;

            default:
                break;
        }
        return;
    }

    // private void tauLoop(ASTNode parentNode, ArrayList<ASTNode> children1) {
    //     if (children1.size() == 1) {
    //         ASTNode gamma = new ASTNode("gamma", new ArrayList<ASTNode>(), parentNode);
    //         ASTNode aug = new ASTNode("aug", new ArrayList<ASTNode>(), gamma);
    //         ASTNode nil = new ASTNode("<nil>", new ArrayList<ASTNode>(), gamma);
    //         gamma.addChild(aug);
    //         gamma.addChild(nil);
    //         parentNode.addChild(gamma);
    //         parentNode.addChild(children1.get(0));
    //     }
    //     ASTNode gamma = new ASTNode("gamma", new ArrayList<ASTNode>(), parentNode);
    //     ASTNode aug = new ASTNode("aug", new ArrayList<ASTNode>(), gamma);
    //     ASTNode gamma1 = new ASTNode("gamma", new ArrayList<ASTNode>(), gamma);
    //     gamma.addChild(aug);
    //     gamma.addChild(gamma1);
    //     parentNode.addChild(gamma);
    //     ASTNode e = children1.remove(children1.size()-1);
    //     e.setParent(parentNode);
    //     parentNode.addChild(e);
    //     this.tauLoop(gamma1, children1);
        
    // }

    private void simultdefLoop(ASTNode commaNode, ASTNode tauNode, ASTNode childNode) {
        ASTNode x = childNode.getChildren().get(0);
        ASTNode e = childNode.getChildren().get(1);
        x.setParent(commaNode);
        e.setParent(tauNode);
        commaNode.addChild(x);
        tauNode.addChild(e);
    }

    private void lambdaLoop(List<ASTNode> variables, ASTNode expression, ASTNode parentNode) {
        if (variables.size() == 0) {
            parentNode.addChild(expression);
            expression.setParent(parentNode);
            return;
        }
        if (variables.size() == 1) {
            ArrayList<ASTNode> children = new ArrayList<ASTNode>();
            children.add(variables.get(0));
            children.add(expression);
            ASTNode lambda = new ASTNode("lambda", children, parentNode);
            parentNode.addChild(lambda);
            return; 
        }
        ArrayList<ASTNode> children = new ArrayList<ASTNode>();
        children.add(variables.get(0));
        ASTNode lambda = new ASTNode("lambda", children, parentNode);
        parentNode.addChild(lambda);
        List<ASTNode> new_variables = variables.subList(1, variables.size());
        this.lambdaLoop(new_variables, expression, lambda);
        
        return;
    }

    private void printTree(ASTNode parentNode,int i) {
        
        if (parentNode.getChildren().size() == 0) {
            System.out.println(buildString('.', i)+parentNode.getType().toString()+"["+parentNode.getValue()+"]");
            return;
        }
        System.out.println(buildString('.', i)+parentNode.getType().toString()+"["+parentNode.getValue()+"]");
        for(int j=0;j<parentNode.getChildren().size();j++) {
            this.printTree(parentNode.getChildren().get(j), i+1);   
        }
    }

    static String buildString(char c, int n) {
        char[] arr = new char[n];
        Arrays.fill(arr, c);
        return new String(arr);
    }




}

