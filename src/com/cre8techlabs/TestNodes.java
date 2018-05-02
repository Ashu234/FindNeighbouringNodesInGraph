package com.cre8techlabs;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class TestNodes {
	/*
AAA   BBB
     CC DDD
  EEEE FF GGG
  EEEE    GGG
 HHHHHHHHHH
	 */
	public static void main(String[] args) {
		Node a = createNode(0, 0, 3, 1);
		Node b = createNode(6, 0, 3, 1);
		Node c = createNode(5, 1, 2, 1);
		Node d = createNode(8, 1, 3, 1);
		Node e = createNode(2, 2, 4, 2);
		Node f = createNode(7, 2, 2, 1);
		Node g = createNode(10, 2, 3, 2);
		Node h = createNode(1, 4, 10, 1);
		
		
		linkNodes(a, b, c, d, e, f, g, h);

		assertNodeLinked(a, new Node[] {}, new Node[] {e}, new Node[] {}, new Node[] {b});
		assertNodeLinked(b, new Node[] {}, new Node[] {c, d}, new Node[] {a}, new Node[] {});
		assertNodeLinked(c, new Node[] {b}, new Node[] {}, new Node[] {}, new Node[] {d});
		assertNodeLinked(d, new Node[] {b}, new Node[] {f, g}, new Node[] {}, new Node[] {});
		assertNodeLinked(e, new Node[] {c}, new Node[] {h}, new Node[] {}, new Node[] {f});
		assertNodeLinked(f, new Node[] {d}, new Node[] {h}, new Node[] {e}, new Node[] {g});
		assertNodeLinked(g, new Node[] {d}, new Node[] {}, new Node[] {f}, new Node[] {});
		assertNodeLinked(h, new Node[] {e, g}, new Node[] {}, new Node[] {}, new Node[] {});
		
		displayRecursivelyAllNodesFromParentNode(a);
		
		
		
	}
	private static List<Node> getModList(Node[] nodes) {
		// TODO Auto-generated method stub
		List modList = new ArrayList();
		for(Node n: nodes)
		{
			modList.add(n);
		}
		return modList;
	}
	
	private static void linkNodes(Node...nodes) {
		// TODO to complete
		Node[] myNodes = nodes;
		double maxX = 0;
		for(Node n: myNodes)
		{
			if(n.getRect().x + n.getRect().getWidth() > maxX)
			{
				maxX = n.getRect().x + n.getRect().getWidth();
			}
		}
		System.out.println("print maxX :" + maxX);
		
		/**Vertical Line sweep **/
		List<Node> activeNodesList = new ArrayList<Node>();
		/*i<=maxX so that active list becomes empty in the end*/
		for(int i = 0; i<= maxX; i++)
		{
			/*check active list for removal*/
			List<Node> removedNodes = removeFromActiveNodesList(activeNodesList, i);
			/*Update the removed Node*/
			if(!removedNodes.isEmpty()){
				updateRemovedNodes(removedNodes, activeNodesList);
				removedNodes = null;
			}
			
			/*check nodes for addition to active list*/
			List<Node> nodesList= findNodesStartingOnSameVertcalLine(i, myNodes);
			if(!nodesList.isEmpty())
			{
				/*logic for line sweep*/
				/*create active list or queue*/
				addToActiveNode(activeNodesList, nodesList);
				
				
			}
			
		}
		
		/* Find Left and Right */
		RectangleComparatorYAxis compYaxis = new RectangleComparatorYAxis();
	    RectangleComparatorXAxis compXaxis = new RectangleComparatorXAxis();
	    List<Node> mlist = getModList(myNodes);
		/*Sort Nodes on Y-axis*/
	    Collections.sort(mlist, compYaxis);
	    Iterator<Node> it = mlist.iterator();
	    List<Node> sameYaxisList = new ArrayList<Node>();
	    int lastYvalue = 0;
	      while(it.hasNext()){
	    	  
	    	  Node n1 = it.next();
	    	  //Node n2 = it.hasNext()?it.next():null;
	    	  if(sameYaxisList.isEmpty()){
	    		  sameYaxisList.add(n1);
	    		  lastYvalue = n1.getRect().y;
	    		  
	    	  }
	    	  else
	    	  {
	    		  if(n1.getRect().y != lastYvalue){
	    			  //process the sameYaxisList
	    			   Collections.sort(sameYaxisList, compXaxis);
	    			   processSameYAxisList(sameYaxisList);
	    			  //empty sameYaxisList
	    			   sameYaxisList.clear();
	    			  //add new node to sameYaxisList
	    			   sameYaxisList.add(n1);
	    			  //update lastYvalue
	    			   lastYvalue = n1.getRect().y;
	    		  }
	    		  else
	    		  {
	    			  sameYaxisList.add(n1);
	    		  }
	    		  
	    		  
	    	  }	    	  
	      }//while ends
	      //processYaxisList
	      Collections.sort(sameYaxisList, compXaxis);
	      processSameYAxisList(sameYaxisList);
		
	}
	
	private static void processSameYAxisList(List<Node> sameYaxisList) {
		// TODO Auto-generated method stub
		Node n = null;
		for(Node nNxt: sameYaxisList){
			
			if(n != null){
				
				n.setRightNodes(new HashSet<INode>());
				n.getRightNodes().add(nNxt);
				nNxt.setLeftNodes(new HashSet<INode>());
				nNxt.getLeftNodes().add(n);
			}
			
			n = nNxt;
		}
		
		
	}
	/*
	AAA   BBB
	     CC DDD
	  EEEE FF GGG
	  EEEE    GGG
	 HHHHHHHHHH
		 */
	private static void updateRemovedNodes(List<Node> removedNodes, List<Node> activeNodesList) {
		// TODO Auto-generated method stub
		for(Node removedNode: removedNodes){
			int minHeightDown = 0;
			int minHeightUp = 0;
			for(Node activeNode: activeNodesList){
				
				//condition for Down Node
				if(activeNode.getRect().y   > removedNode.getRect().y)
				{	
					minHeightDown = activeNode.getRect().y - removedNode.getRect().y;
					//condition if downNode set is Empty
					if(removedNode.getDownNodes() == null){
						removedNode.setDownNodes(new HashSet<INode>());
						removedNode.getDownNodes().add(activeNode);
						//update Active Node's Up Node
						updateActiveUpNodes(removedNode, minHeightDown, activeNode);
					}
					else{
						//get first element because all elements will have same distance from active node
						Node firstNode = (Node) removedNode.getDownNodes().iterator().next();
						if(((firstNode.getRect().y + firstNode.getRect().height -1) - (removedNode.getRect().y + removedNode.getRect().height - 1)) > minHeightDown){
							removedNode.getDownNodes().clear();
							removedNode.getDownNodes().add(activeNode);
							
							//update Active Node's Up Nodes
							updateActiveUpNodes(removedNode, minHeightDown, activeNode);
						}
						else if(minHeightDown == firstNode.getRect().y - removedNode.getRect().y){
							removedNode.getDownNodes().add(activeNode);
							//update Active Node's Up Nodes
							updateActiveUpNodes(removedNode, minHeightDown, activeNode);
						}
						
					}
				}
				else//condition for UpNode
				{
					minHeightUp = removedNode.getRect().y - activeNode.getRect().y;
					//condition if Up Node set is Empty
					if(removedNode.getUpNodes() == null){
						removedNode.setUpNodes(new HashSet<INode>());
						removedNode.getUpNodes().add(activeNode);
						
						//update Active Node's Down Node
						updateActiveDownNodes(removedNode, minHeightUp, activeNode);
					}
					else{
						//get first element because all elements will have same distance from active node
						Node firstNode = (Node) removedNode.getUpNodes().iterator().next();
						if(((removedNode.getRect().y + removedNode.getRect().height - 1) - (firstNode.getRect().y + firstNode.getRect().height - 1)) > minHeightUp){
							removedNode.getUpNodes().clear();
							removedNode.getUpNodes().add(activeNode);
							//minHeightUp = removedNode.getRect().y - activeNode.getRect().y;
							//update Active Node's Down Node
							updateActiveDownNodes(removedNode, minHeightUp, activeNode);
						}
						else if(minHeightUp == removedNode.getRect().y - firstNode.getRect().y){
							removedNode.getUpNodes().add(activeNode);
							//update Active Node's Down Node
							updateActiveDownNodes(removedNode, minHeightUp, activeNode);
						}
						
					}
					
				}
				
			}
			
		}
		
		
	}
	private static void updateActiveDownNodes(Node removedNode, int minHeightUp, Node activeNode) {
		if(activeNode.getDownNodes() == null){
			activeNode.setDownNodes(new HashSet<INode>());
			activeNode.getDownNodes().add(removedNode);
		}
		else{ //if active node already has down nodes
			
			//get first element because all elements will have same distance from active node
			Node firstNode = (Node) activeNode.getDownNodes().iterator().next();
			if(((firstNode.getRect().y + firstNode.getRect().height -1) - (activeNode.getRect().y + activeNode.getRect().height -1)) > minHeightUp){   //all its Down Nodes have more distance than removed node
				activeNode.getDownNodes().clear();
				activeNode.getDownNodes().add(removedNode);
			}
			else if(minHeightUp == ((firstNode.getRect().y + firstNode.getRect().height -1) - (activeNode.getRect().y + activeNode.getRect().height -1))){
				activeNode.getDownNodes().add(removedNode);
			}
		}
	}
	private static void updateActiveUpNodes(Node removedNode, int minHeightDown, Node activeNode) {
		if(activeNode.getUpNodes() == null)
		{
			activeNode.setUpNodes(new HashSet<INode>());
			activeNode.getUpNodes().add(removedNode);
		}
		else//Condition where Active node has already Up nodes
		{
			//get first element because all elements will have same distance from active node
			Node firstNode = (Node) activeNode.getUpNodes().iterator().next();
			if(((activeNode.getRect().y + activeNode.getRect().height -1) - (firstNode.getRect().y + firstNode.getRect().height -1)) > minHeightDown){  //all its up Nodes have more distance than removed node
				activeNode.getUpNodes().clear();
				activeNode.getUpNodes().add(removedNode);
			}
			else if(minHeightDown == ((activeNode.getRect().y + activeNode.getRect().height -1) - (firstNode.getRect().y + firstNode.getRect().height -1))){
				activeNode.getUpNodes().add(removedNode);
			}
		}
	}
	private static List<Node> removeFromActiveNodesList(List<Node> activeNodesList, int i) {
		// TODO Auto-generated method stub
		List<Node> removedNodes = new ArrayList<Node>();
		Iterator<Node> it = activeNodesList.iterator();
		while(it.hasNext()){
			Node n = it.next();
			if((n.getRect().x + n.getRect().getWidth()) - i == 0){
				removedNodes.add(n);
				it.remove();
			}
		}
		return removedNodes;
	}
	
	
	private static void addToActiveNode(List<Node> activeNodesList, List<Node> nodesList) {
		// TODO Auto-generated method stub
		for(Node n: nodesList)
		{
			activeNodesList.add(n);
		}
		
	}
	
	private static List<Node> findNodesStartingOnSameVertcalLine(int i, Node[] myNodes) {
		List<Node> nodeList = new ArrayList<Node>();
		for(Node n: myNodes)
		{
			if(n.getRect().x == i)
			{
				nodeList.add(n);
			}
		}
		
		return nodeList;
	}
	private static void displayRecursivelyAllNodesFromParentNode(Node a) {
		// TODO to complete
		
		
	}
	private static void assertNodeLinked(Node node, Node[] up, Node[] down, Node[] left, Node[] right) {
		if (!containsAll(node.getUpNodes(), Arrays.asList(up))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getDownNodes(), Arrays.asList(down))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getLeftNodes(), Arrays.asList(left))) {
			throw new AssertionError("The conditions are not meet");
		}
		if (!containsAll(node.getRightNodes(), Arrays.asList(right))) {
			throw new AssertionError("The conditions are not meet");
		}
	}

	private static boolean containsAll(Collection<INode> nodes, Collection<INode> nodes2) {
		if (nodes.size() != nodes2.size())
			return false;
		if (nodes.isEmpty() && nodes2.isEmpty())
			return true;
		if (nodes.containsAll(nodes2) && nodes2.containsAll(nodes)) {
			return true;
		}
		return false;
	}

	private static Node createNode(int x, int y, int width, int height) {
		Node result = new Node();
		result.setRect(new Rectangle(x, y, width, height));
		
		return result;
	}
	
	private static class RectangleComparatorYAxis implements Comparator
    {

       public int compare(Object firstObject, Object secondObject)
       {
          Node n1 = (Node) firstObject;
          Rectangle r1 = n1.getRect();
          Node n2 = (Node) secondObject;
          Rectangle r2 = n2.getRect();

          if (r1.y < r2.y)
          {
             return -1;
          }
          if (r1.y > r2.y)
          {
             return 1;
          }
          else 
          {
          	return 1;
          }
       }
    }//cass ends
	
	private static class RectangleComparatorXAxis implements Comparator
     {

        public int compare(Object firstObject, Object secondObject)
        {
           // order based on area, ascending
           Node n1 = (Node) firstObject;
           Rectangle r1 = n1.getRect();
           Node n2 = (Node) secondObject;
           Rectangle r2 = n2.getRect();

           if (r1.x < r2.x)
           {
              return -1;
           }
           if (r1.x > r2.x)
           {
              return 1;
           }
           else  
           {
           	return 1;
           }
        }
     }//cass ends
}
