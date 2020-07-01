import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import cse.CSEMachine;
import standards.AST;
import standards.ASTNode;

public class App {
    public static void main(String[] args) throws Exception {
        ArrayList<String> inputarray = new ArrayList<String>();
        
        try {
            File inputfile = new File(args[0]);
            Scanner reader = new Scanner(inputfile);
            while (reader.hasNextLine()) {
              String data = reader.nextLine().trim();
              inputarray.add(data);
              System.out.println(data);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error occured when reading a file");
            e.printStackTrace();
        }
        buildAST(inputarray);
    }

    public static AST buildAST(ArrayList<String> inputarray) {
        ArrayList<ArrayList<Object>> nodeDetails = new ArrayList<ArrayList<Object>>();
        for(int i = 0; i < inputarray.size(); i++) {
            nodeDetails.add(getNodeDetails(inputarray.get(i),i)) ;
        }
        ASTNode rootnode = new ASTNode((String) nodeDetails.get(0).get(0),new ArrayList<ASTNode>(), null,(long) nodeDetails.get(0).get(1), (int) nodeDetails.get(0).get(2));
        
        AST ast = new AST(rootnode,nodeDetails);
        ast.ASTBuild();
        // ast.ASTPrintTree();
        ast.ASTStandardize();
        ast.ASTPrintTree();
        CSEMachine cseMachine = new CSEMachine(ast);
        cseMachine.evaluate();
        // cseMachine.printDeltas();
        return null;
    }

    public static ArrayList<Object> getNodeDetails(String node,int i) { 
        ArrayList<Object> outputarray = new ArrayList<Object>();
        long count = node.chars().filter(ch -> ch == '.').count();
        String name = node.replaceAll("[.]","");
        outputarray.add(name);
        outputarray.add(count);
        outputarray.add(i);
        return outputarray;
    }
}






// int x = 0;
        // int limit = nodeDetails.size();
        // ArrayList<ASTNode> nodes = new ArrayList<ASTNode>();
        // nodes.add(new ASTNode((String) nodeDetails.get(0).get(0),new ArrayList<ASTNode>(), null,(long) nodeDetails.get(0).get(1), (int) nodeDetails.get(0).get(2)));
        // while(x != limit-1) {
        //     int y = x+1;
        //     ASTNode temp = nodes.get(x);
            
        //     long z = temp.getCount();
        //     long y_count = (long) nodeDetails.get(y).get(1);
        //     while(y_count != z && y != limit-1) {
        //         System.out.println(y_count);
        //         if (y_count == z+1 &&  temp.getLinenumber() > (int)nodeDetails.get(y).get(2)) {
        //             ASTNode newNode = new ASTNode((String) nodeDetails.get(y).get(0),new ArrayList<ASTNode>(), temp,(long) nodeDetails.get(y).get(1), (int) nodeDetails.get(0).get(2));
        //             temp.addChild(newNode);
        //             nodes.add(newNode);
        //             System.out.println(newNode.getType());
        //         } 
        //         y++;
        //         y_count = (long) nodeDetails.get(y).get(1);
                
        //     }
        //     x++;
        // }