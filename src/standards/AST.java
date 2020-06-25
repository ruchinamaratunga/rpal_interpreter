package standards;

import java.util.ArrayList;
import java.util.List;

public class AST {
    private ASTNode rootnode;
    private ArrayList<ArrayList<Object>> nodeDetails;

    public AST(ASTNode rootnode, ArrayList<ArrayList<Object>> nodeDetails) {
        this.rootnode = rootnode;
        this.nodeDetails = nodeDetails;
    }

    public void ASTBuild() {
        this.nodebuild(this.rootnode, this.nodeDetails);
    }

    public void printAST() {

    }

    public void standardize() {

    }

    private void nodebuild(ASTNode node, List<ArrayList<Object>> list) {
        ArrayList<Integer> childlines = new ArrayList<Integer>();
        System.out.println(node.getType());
        for (int i = 0; i < list.size(); i++) {
            if ((long) list.get(i).get(1) == node.getCount()+1) {
                ASTNode newNode = new ASTNode((String) list.get(i).get(0), new ArrayList<ASTNode>(), node,
                        (long) list.get(i).get(1), (int) list.get(0).get(2));
                node.addChild(newNode);
                childlines.add(newNode.getLinenumber());
            }
        }
        childlines.add(list.size());
        for (int j = 0; j < node.getChildren().size(); j++) {
            this.nodebuild(node.getChildren().get(j), list.subList(childlines.get(j), childlines.get(j + 1)));
        }
        return;
    }
}

