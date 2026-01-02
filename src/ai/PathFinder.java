package ai;

import java.util.ArrayList;

import main.GamePanel;

public class PathFinder {
	
	GamePanel gp;
	Node[][] node;
	ArrayList<Node> openList = new ArrayList<>();
	public ArrayList<Node> pathList = new ArrayList<>();
	Node startNode, currentNode, goalNode;
	boolean goalReached = false;
	int step = 0;
	
	public PathFinder(GamePanel gp) {
		
		this.gp = gp;
		
		instantiateNode();
	}
	
	public void instantiateNode() {
		node = new Node[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		
		while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
			node[col][row] = new Node(col,row);
			
			col++;
			if(col == gp.maxWorldCol) {
				row++;
				col = 0;
			}
		}
	}
	
	public void resetNodes() {
		
		int col = 0;
		int row = 0;
		
		while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
			// Reset open, solid and checked state
			node[col][row].open = false;
			node[col][row].checked = false;
			node[col][row].solid = false;
			
			col++;
			if(col == gp.maxWorldCol) {
				row++;
				col = 0;
			}
		}
		
		// RESET THE OPENLIST
		openList.clear();
		pathList.clear();
		goalReached = false;
		step = 0;
	}
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
		
		resetNodes();
		
		// set Start and Goal Node
		startNode = node[startCol][startRow];
		currentNode = startNode;
		goalNode = node[goalCol][goalRow];
		openList.add(currentNode);
		
		int col = 0;
		int row = 0;
		
		while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			// set Solid Node
			int tileNum = gp.tileM.mapTileNum[gp.currentMap][col][row];
			if(gp.tileM.tile[tileNum].collision == true ) {
				node[col][row].solid = true;
			}
			
			// set Interactive Tile also as solid
			
			for(int i=0; i<gp.iTile[1].length; i++) {
				if(gp.iTile[gp.currentMap][i] != null && gp.iTile[gp.currentMap][i].destructible == true) {
					
					int itCol = gp.iTile[gp.currentMap][i].worldX / gp.tileSize;
					int itRow = gp.iTile[gp.currentMap][i].worldY / gp.tileSize;
					
					node[itCol][itRow].solid = true;
				}
			}
			
			// setCost
			getCost(node[col][row]);
			
			col++;
			if(col == gp.maxWorldCol) {
				row++;
				col = 0;
			}
		}
		
	}

	private void getCost(Node node) {
		
		// Get "G COST" (The Distance between Current Node and Start Node)
		int xDistance = Math.abs(node.col - startNode.col);
		int yDistance = Math.abs(node.row - startNode.row);
		node.gCost = xDistance + yDistance;
		// Get "H COST" (The Distance between Current Node and Goal Node)
		xDistance = Math.abs(node.col - goalNode.col);
		yDistance = Math.abs(node.row - goalNode.row);
		node.hCost = xDistance + yDistance;		
		// Get "F COST" (F COST = G COST + H COST)
		node.fCost = node.gCost + node.hCost;
		
	}
	
	public boolean search() {
		while(goalReached == false && step < 500) {
			
			int col = currentNode.col;
			int row = currentNode.row;
			
			// Check the current Node
			currentNode.checked = true;
			openList.remove(currentNode);
			
			// OPEN UPPER NODE
			if(row -1 >= 0 ) {
				openNode(node[col][row-1]);
			}
			
			// OPEN Left NODE
			if(col - 1 >= 0 ) {
				openNode(node[col - 1][row]);
			}
			// OPEN DOWN NODE
			if(row + 1 < gp.maxWorldRow ) {
				openNode(node[col][row+1]);
			}
			// OPEN UPPER NODE
			if(col + 1 < gp.maxWorldCol ) {
				openNode(node[col+1][row]);
			}

			int bestNodeIndex = 0;
			int bestNodefCost = 999;
			
			// FIND THE BEST NODE
			for(int i=0; i<openList.size(); i++) {
				
				// CHECK if the node's FCOST is better
				if(openList.get(i).fCost < bestNodefCost) {
					bestNodefCost = openList.get(i).fCost;
					bestNodeIndex = i;
				}
				// IF FCOST IS SAME, CHECK WHICH HAS LESS G COST
				else if (openList.get(i).fCost == bestNodefCost) {
					if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
						bestNodeIndex = i;
					}
				}
			}
			
			// IF THERE IS NO NODE IN THE OPENLIST, THEN WE GONNA EXIT
			if(openList.size() == 0) break;
			
			// AFTER THE LOOP, OPENLIST(BESTNODEINDEX) is The CURRENTNODE
			currentNode = openList.get(bestNodeIndex);
			
			if(currentNode == goalNode) {
				goalReached = true;
				trackThePath();
			}
			step++;
		}
		return goalReached;
	}

	public void openNode(Node node) {
		 if(node.open == false && node.checked == false && node.solid == false) {
			 
			 node.open = true;
			 node.parent = currentNode;
			 openList.add(node);
		 }
	}
	
	private void trackThePath() {
		
		Node current = goalNode;
		
		while(current != startNode) {
			
			pathList.add(0, current);
			current = current.parent;
			
		}
	}
}







