//FILE:				Node.java
//AUTHOR:			Xhien Yi Tan ( Xavier )
//ID:				18249833
//UNIT:				Artificial and Machine Intelligence - COMP3006
//PURPOSE:			A class that is responsible for Node in the graph
import java.util.*;

public class Node implements Comparable<Node> {

	//Class Fields
	private String name;
	private double distanceToGoal;
	private double fValue;

	private Node parentNode;
	private double gValue;

	private HashMap<String, Node> adjNode;
	private HashMap<String, Double> adjDistance;

	//Alternate Constructor
	public Node( String inName ) {

		setName( inName );
		gValue = 0.0;
		fValue = 0.0;
		distanceToGoal = 0.0;
		adjNode = new HashMap<String, Node>();
	 	adjDistance = new HashMap<String, Double>();
	}

	//Copy Constructor
	public Node( Node inNode ) {

		name = inNode.getName();
		fValue = inNode.getFValue();
		distanceToGoal = inNode.getDistanceToGoal();

		gValue = inNode.getGValue();
		parentNode = inNode.getParent();

		adjNode = inNode.getAdjNode();
		adjDistance = inNode.getAdjDistance();
	}

	//Mutator - set name
	private void setName( String inName ) {

		if( inName.equals("") || inName == null ) {
			throw new IllegalArgumentException( "Node's name is invalid" );

		} else {
			name = inName;
		}

	}

	//Mutator - set f value
	public void setFValue( double inFValue ) {

		if( inFValue <= 0.0 ) {
			throw new IllegalArgumentException( "f value cant be negative or zero" );
		} else {
			fValue = inFValue;
		}

	}

	//Mutator - set g value
	public void setGValue( double inGValue ) {

		if( inGValue <= 0.0 ) {
			throw new IllegalArgumentException( "Distance from one node to another cant be negative or zero" );
		} else {
			gValue = inGValue;
		}

	}

	//Mutator - set straight line distance to goal - heuristic
	public void setDistanceToGoal( double inDistance ) {

		if( inDistance < 0.0 ) {
			throw new IllegalArgumentException( "Heuristic distance to goal cant be negative" );
		} else {
			distanceToGoal = inDistance;
		}

	}

	//Mutator - set parent
	public void setParent( Node inNode ) {

		if( inNode == null ) {
			throw new IllegalArgumentException( "Parent is null, cant set parent" );
		} else {
			parentNode = inNode;
		}

	}

	//Getter - get parent
	public Node getParent() {
		return parentNode;
	}

	//Getter - get name
	public String getName() {
		return name;
	}

	//Getter - get f value
	public double getFValue() {
		return fValue;
	}

	//Getter - get g value
	public double getGValue() {
		return gValue;
	}

	//Getter - get heuristic
	public double getDistanceToGoal() {
		return distanceToGoal;
	}

	//Getter - get node
	public Node getNode( String nodeName ) {
		return adjNode.get(nodeName);
	}

	//Getter - get distance from that node
	public double getDistance( Node node ) {
		return adjDistance.get( node.getName() );
	}

	//Getter - get all the adjacent neighbours
	public HashMap<String, Node> getAdjNode() {
		return adjNode;
	}

	//Getter - get all the adjacent distances
	public HashMap<String, Double> getAdjDistance() {
		return adjDistance;
	}

	//Getter - get its children as a queue
	public Queue<Node> getChildQueue() {

		Queue<Node> children = new LinkedList<Node>();

		Iterator it = adjNode.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();

		  	Node n = adjNode.get( pair.getKey() );

			children.add( n );
		}

		return children;
	}

	//Getter - get its children as array list
	public ArrayList<Node> getChildNode() {

		ArrayList<Node> children = new ArrayList<Node>();

		Iterator it = adjNode.entrySet().iterator();
		while (it.hasNext()) {
	    		Map.Entry pair = (Map.Entry)it.next();
			Node n = adjNode.get( pair.getKey() );

			children.add( n );
		}

		return children;
	}

	//PURPOSE:	Add adjacent neighbours detials and its distance between them
	//IMPORT:	Neighbour node, the distance between them
	//EXPORT:	None
	public void addAdjDetails( Node inNode, double inDistance ) {

		if( inDistance <= 0.0 ) {
			throw new IllegalArgumentException( "Distance from one node to another cant be negative or zero" );
		} else {
			adjDistance.put( inNode.getName(), inDistance );
		}

		if( inNode == null ) {
			throw new IllegalArgumentException( "Adding a null node" );
		} else {
			adjNode.put( inNode.getName(), inNode );
		}
	}

	//PURPOSE:	Check whether this node contains this child or neighbour
	//IMPORT:	child name for reference
	//EXPORT:	does contain or not
	public boolean containChild( String childName ) {

		boolean contain = false;

		if( adjNode.containsKey( childName ) ) {
			contain = true;
		}

		return contain;

	}

	//PURPOSE:	Remove this child or neighbour
	//IMPORT:	child name for reference
	//EXPORT:	None
	public void removeNode( String childName ) {
		adjNode.remove( childName );
		adjDistance.remove( childName );
	}

	//Equals method
	public boolean equals( Node node ) {

		double f_value = node.getFValue();
		boolean equal = false;

		if( getFValue() == f_value ) {
			equal = true;
		}

		return equal;
	}

	//For implementing comparable for priority queue for A*
	@Override
	public int compareTo( Node node ) {

		double f_value = node.getFValue();
		int retVal;

		if( equals( f_value ) ) {
			retVal = 0;
		} else if( getFValue() > f_value ) {
			retVal = 1;
		} else {
			retVal = -1;
		}

		return retVal;
	}

	//PURPOSE:	Calculate the f value using heuristic and g value
	//IMPORT:	None
	//EXPORT:	None
	public void calcFValue() {
		fValue = distanceToGoal + gValue;
	}

	//ToString
	public String toString() {
		return new String( name + " " + fValue   );
	}


}
