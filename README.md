# FindNeighbouringNodesInGraph
class Node {
	Rectangle rect;

	Set<Node> upNodes;
	Set<Node> downNodes;
	Set<Node> leftNodes;
	Set<Node> rightNodes;
}
Each node are connected to their nearest neighbor which intersect with them.
if multiple nearest neighbors are on the same distance then these node will be included in the set
Reactangle {
	x, y, width, height
}

A to Z are nodes

AAA means a.rect.width = 3 and a.rect.height = 1

 EEE
 EEE means e.rect.width = 3 and e.rect.height = 2

Using the example below write a program that connect all the nodes and set for each node the expected result
And traverse the graph from A to display all the Nodes
------------------------------------
    AAA   BBB
         CC DDD
      EEEE FF GGG
      EEEE    GGG
     HHHHHHHHHH

     
A {
	upNodes: [],
	downNodes: [E],
	leftNodes: [],
	rightNodes: [B],
}

B {
	upNodes: [],
	downNodes: [C, D],
	leftNodes: [A],
	rightNodes: [],
}

C {
	upNodes: [B],
	downNodes: [E],
	leftNodes: [],
	rightNodes: [D],
}

D {
	upNodes: [B],
	downNodes: [F, G],
	leftNodes: [C],
	rightNodes: [],
}

E {
	upNodes: [C],
	downNodes: [H],
	leftNodes: [],
	rightNodes: [F],
}

F {
	upNodes: [D],
	downNodes: [H],
	leftNodes: [E],
	rightNodes: [G],
}

G {
	upNodes: [D],
	downNodes: [],
	leftNodes: [F],
	rightNodes: [],
}

H {
	upNodes: [E, G],
	downNodes: [],
	leftNodes: [],
	rightNodes: [],
}

The task is divided into two parts
1. Line Sweep Algorithm to find upNoodes and downNodes of a given node in the graph
2. Sorting on basis of Y-axis and then sorting all nodes on a particular Y-axis on the asis of X-axis in order to find Left and Right nodes



