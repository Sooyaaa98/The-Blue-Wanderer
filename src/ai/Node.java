package ai;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Node extends JButton{
	
	Node parent;
	public int col;
	public int row;
	int gCost;
	int hCost;
	int fCost;
	boolean solid, checked, open;
	
	
	public Node(int col, int row) {
		this.col = col;
		this.row = row;
	}
	
}
