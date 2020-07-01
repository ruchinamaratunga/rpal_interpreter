package cse;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

import standards.ASTNode;
import standards.NodeType;

public class ControlStructures {
    private ASTNode rootNode;
    private ArrayList<DeltaNode> deltas;
    private ArrayDeque<UnfinishedDelta> unfinishedDeltas;
    // private DeltaNode currentDelta;
    private DeltaNode rootDelta;
    private int index;

    public ControlStructures(ASTNode rootNode) {
        this.rootNode = rootNode;
        this.setDeltas(new ArrayList<DeltaNode>());
        this.unfinishedDeltas = new ArrayDeque<UnfinishedDelta>();
        this.index = 0;
        UnfinishedDelta urootDelta = new UnfinishedDelta();
        urootDelta.node = rootNode;
        urootDelta.body = new Stack<ASTNode>();
        unfinishedDeltas.add(urootDelta);
        this.rootDelta = new DeltaNode();
        this.deltas.add(this.rootDelta);
        this.rootDelta.setBody(urootDelta.body);
        this.rootDelta.setEnvironment(new Environment());
        this.rootDelta.setIndex(1);
        this.currentDelta = this.rootDelta;
    }

    public DeltaNode buildDelta(ASTNode node) {
        UnfinishedDelta delta = new UnfinishedDelta();
        delta.node = node;
        delta.body = new Stack<ASTNode>();
        unfinishedDeltas.add(delta);

        DeltaNode d = new DeltaNode();
        d.setBody(delta.body);
        d.setIndex(this.index+1);
        this.currentDelta = d;
        this.deltas.add(d);
        return d;
    }

    public void buildAExpresssion() {
        this.completeStack();
    }

    private void completeStack() {
        while(!this.unfinishedDeltas.isEmpty()) {
            UnfinishedDelta temp = unfinishedDeltas.pop();
            buildBody(temp.node, temp.body);
        }
    }

    private void buildBody(ASTNode node, Stack<ASTNode> body) {
        if(node.getType() == NodeType.LAMBDA) {
            DeltaNode new_delta = buildDelta(node.getChildren().get(1));
            if(node.getChildren().get(0).getType() == NodeType.COMMA) {
                for(ASTNode child: node.getChildren()) {
                    new_delta.addVariables(child.getValue());
                }
            } else {
                new_delta.addVariables(node.getChildren().get(0).getValue());
            }
            body.push(new_delta);
            return;
        } else if(node.getType() == NodeType.CONDITIONAL) {
            ASTNode B = node.getChildren().get(0);
            ASTNode then = node.getChildren().get(1);
            ASTNode elsen = node.getChildren().get(2);
            
            BetaNode beta = new BetaNode();

            buildBody(then, beta.getThen());
            buildBody(elsen, beta.getElsen());

            body.push(beta);
            buildBody(B, body);
            return;
        }

        body.push(node);
        for(int i=0;i< node.getChildren().size();i++) {
            buildBody(node.getChildren().get(i), body);
        }
    }

    private class UnfinishedDelta {
        Stack<ASTNode> body;
        ASTNode node;
    }

    public ArrayList<DeltaNode> getDeltas() {
        return deltas;
    }

    public void setDeltas(ArrayList<DeltaNode> deltas) {
        this.deltas = deltas;
    }

    public DeltaNode getRootDelta() {
        // System.out.println(this.rootDelta.toString());
        return this.rootDelta;
    }

    public void setDeltas(DeltaNode rootDelta) {
        this.rootDelta = rootDelta;
    }

}

