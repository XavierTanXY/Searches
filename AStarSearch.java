//FILE:				AStarSearch.java
//AUTHOR:			Xhien Yi Tan ( Xavier )
//ID:				18249833
//UNIT:				Artificial and Machine Intelligence - COMP3006 
//PURPOSE:			A class that is responsible for Memory limited A* Search 
import java.util.*;

public class AStarSearch {

	//Start name and goal name for reference
	public static String goalName;
	public static String startName;

	//Limit for this search
	public final static int MAX_LIMIT = 15;

	//PURPOSE:	A method that starts the search
	//IMPORT:	Start node, Goal node
	//EXPORT:	None
	public static void aStarSearch( Node startNode, Node goalNode ) {

		goalName = goalNode.getName();
		startName = startNode.getName();

		search( startNode, goalNode );
	}

	//PURPOSE:	Method that does the search, if memory is full, delete node from search tree to make room
	//IMPORT:	Start node, Goal node
	//EXPORT:	None
	private static void search( Node startNode, Node goalNode ) {

		//All goals path
		LinkedList<LinkedList<String>> allGoals = new LinkedList<LinkedList<String>>();

		//Priority queue acts for the search tree
		PriorityQueue<Node> searchTree = new PriorityQueue<Node>(); 

		//Priority queue for this whole search
		PriorityQueue<Node> queue = new PriorityQueue<Node>();

		//Start the search by adding start node to queue
		queue.add( startNode );

		while( !queue.isEmpty() ) {
	
			Node currentNode = queue.poll();
			
			//Get children from this cuurent node
			ArrayList<Node> children = currentNode.getChildNode();

			//If the search tree is full, decide which node to delete from search tree
			if( searchTree.size() == MAX_LIMIT ) {

				//Get current path from this current node
				LinkedList<Node> currentPath = getPath( currentNode );

				//Get stack of nodes so that we can get the highest f value each time
				Stack<Node> stackNode = getLargestNode( searchTree );

				//Check nodes that are not part of the path and remove it
				boolean existedInPath = check( currentPath, stackNode, searchTree );
			
				if( !existedInPath ) {
					//After making room current node, add the current node to tree
					searchTree.add( currentNode );

					//Other than the goal node, set their parents to current node
					if( !currentNode.getName().equals( goalName ) ) {
						setParent( currentNode, children, queue );
					}
					
					checkPathWithTree( currentPath, searchTree );
				
				} else {
					/**
					 * No more room for adding current node, because the path from start node to current node has 
					 * the maximum number of nodes, so it discards the current node from the path and also remove the second last 
					 * node from the search tree, so that it has room for others
					 */
						System.out.println( "=============== MEMORY FULL ==============" );		
						System.out.println( "Nothing can be done, discard this node: " + currentPath.removeLast().getName() );			
						printPath( currentPath );

				}


			} else {
				/**
				 * If the search tree is not full, add current node to the tree.
				 * Then set their parent to current node
				 */
				searchTree.add( currentNode );

				if( !currentNode.getName().equals( goalName ) ) {
					setParent( currentNode, children, queue );
				}

			}

			//Check for goal node everytime
			if( currentNode.getName().equals( goalName ) ) {
				printGoal( currentNode, allGoals );
				inputFromUser();	
			}

		}

		System.out.println( "=============== NO MORE SOLUTION ==============" );
	}

	//PURPOSE:	Transfer the search tree from queue into a stack, so always 
	//			get the largest f value from the top of the stack
	//IMPORT:	Current search tree
	//EXPORT:	Stack that has highest f value on the top
	private static Stack<Node> getLargestNode( PriorityQueue<Node> searchTree ) {
		Node node = null;
		PriorityQueue<Node> tempTree = new PriorityQueue<Node>();

		//Transfer to a temporary queue so that the original tree wont be changed
		for( Node n : searchTree ) {
			tempTree.add( n );
		}

		Stack<Node> stack = new Stack<Node>();

		while( (node = tempTree.poll()) != null) {
		    stack.push( node );
		}

		return stack;

	}

	//PURPOSE:	Get current path from starting node to this current node
	//IMPORT:	Current node
	//EXPORT:	List of node that shows the path
	private static LinkedList<Node> getPath( Node n ) {

		LinkedList<Node> currentPath = new LinkedList<Node>();
		Node prevParent = n.getParent();

		currentPath.addFirst( n );
		while( prevParent != null ) {

			//Get its parant everytime and insert to list
			currentPath.addFirst( prevParent );
			prevParent = prevParent.getParent();
		}
		
		return currentPath;
	} 

	//PURPOSE:	Check whether a node in the path is in the search tree, if no, add it to tree
	// 			
	//IMPORT:	Cuurent path from a node, Current search tree
	//EXPORT:	None
	private static void checkPathWithTree( LinkedList<Node> currentPath, PriorityQueue<Node> searchTree ) {

		ArrayList<Node> list = new ArrayList<Node>();
		boolean existed = true;

		for( Node n : currentPath ) {

			String name = n.getName();
			double fValue = n.getFValue();
			
			for( Node nn : searchTree ) {
				existed = true;

				String firstName = nn.getName();
				double secondFValue = nn.getFValue();

				//Node from path is in the search tree
				if( name.equals( name ) && fValue == secondFValue ) {
					break;
				} else {
					existed = false;
				}
			}

			if( !existed ) {
				list.add( n );
			}
		}

		if( list.size() != 0 ) {

			//Trying to get all nodes from path that are not in the search tree to put it in tree
			for( Node n : list ) {

				//If the search tree is full, decide which node to delete from search tree
				if( searchTree.size() == MAX_LIMIT ) {

					Stack<Node> stackNode = getLargestNode( searchTree );
						
					check( currentPath, stackNode, searchTree );
					searchTree.add( n );

				} else { 
					//If the tree is not full, just add it
					searchTree.add( n );
				}
			}

		}

	}

	//PURPOSE:	Check for node that is not in the current path, so that it can be 
	//			deleted in search tree for more memory later on
	//IMPORT:	Current path, Stack of nodes to get highest f value each time, Current search tree
	//EXPORT:	Found a node that can be deleted or not
	private static boolean check( LinkedList<Node> currentPath, Stack<Node> stackNode, PriorityQueue<Node> searchTree ) {
		
		boolean existedInPath = false;

		while( !stackNode.isEmpty() ) {
			Node node = stackNode.pop();

			existedInPath = false;
			for( Node pathNode : currentPath ) {
				if( node.getName().equals( pathNode.getName() ) && node.getFValue() == pathNode.getFValue() ) {
					existedInPath = true;
					break;
				}
			}

			if( !existedInPath ) {
				searchTree.remove( node ); //Found something that can be deleted, so delete from tree
				break;
			} 

		}

		return existedInPath;
	}

	//PURPOSE:	Print goal path
	//IMPORT:	Start node, All goals path
	//EXPORT:	None
	private static void printGoal( Node goalNode, LinkedList<LinkedList<String>> allGoals ) {

		System.out.println( "=============== FOUND GOAL ==============" );
		LinkedList<String> newGoal = new LinkedList<String>();
		Stack<Node> displayStack = new Stack<Node>();
		Node prevParent = goalNode.getParent();
		int i = 0;
		
		//Push it onto the stack because if not the output is in reversed order
		while( prevParent != null ) {
			displayStack.push( prevParent );
			prevParent = prevParent.getParent();

		}

		while( !displayStack.isEmpty() ) {
			if (i > 0) {
				System.out.print(" --> ");
			}

	       		String nodeName = displayStack.pop().getName();
	       		newGoal.addLast( nodeName );
			System.out.print( nodeName );
			i++;
		}

		newGoal.addLast( goalName );
		allGoals.addLast( newGoal );

		System.out.print( " --> " + goalNode.getName()  );
		System.out.print( ", Total f-cost with: " + goalNode.getFValue() );
		System.out.println();
	}

	//PURPOSE:	A method that gets input from user whether they want to stop or continue the search
	//IMPORT:	None
	//EXPORT:	Continue or not
	private static boolean inputFromUser() {
		System.out.println( "Do you still want to continue to find alternate solution? (1) Yes, (2) No" );

		Scanner sc = new Scanner(System.in);
		int input = sc.nextInt();
		boolean want = false;

		if( input == 1 ) {
			want = true;
		} 

		if( !want ) {
			System.exit(0); //Exit the program if the use chooses No
		}

		return want;
	}

	//PURPOSE:	Set the nodes' parent and calculate the f value as the search go, then enqueue
	//IMPORT:	Parent node, List of its children, queue for the search
	//EXPORT:	None
	private static void setParent( Node parent, ArrayList<Node> children, PriorityQueue<Node> queue ) {

		//Create a list of parents
		LinkedList<Node> parents = new LinkedList<Node>();

		//If parent node is the start node
		if( parent.getName().equals( startName ) ) {

			for( Node n : children ) {

				n.setParent( parent );
				n.setGValue( 0.0 + parent.getDistance( n ) ); //0.0 here is because of start node
				n.calcFValue();

				queue.add( n );
				
			}
		} else {

			if( parent.getParent() != null ) {

				Node prevParent = parent.getParent();

				parents.addLast( prevParent );

				//Add all the parents to the list
				while( prevParent.getParent() != null ) {
			
					prevParent = prevParent.getParent();
					parents.addLast( prevParent );
					
				}


				for( Node c : children ) {
					
					//Check whether this node is already in the path, if yes dont add
					boolean existed = checkExisted( parents, c );

					//It does not exist, so add to the path and queue
					if( !existed ) {

						//Create a new node using copy constructor but set to different parent
						Node newNode = new Node( c );

						newNode.setParent( parent );
						newNode.setGValue( parent.getGValue() + parent.getDistance( newNode ) );
						newNode.calcFValue( );

						queue.add( newNode );
					}
				}

			}
		
		}


	}

	//PURPOSE:	Check whether a node is already in the path
	//IMPORT:	List of path known as path, current node
	//EXPORT:	Whether the node is in the path 
	private static boolean checkExisted( LinkedList<Node> list, Node node ) {
		boolean existed = false;

		for( Node n : list ) {

			if( n.getName().equals( node.getName() ) ) {
				existed = true;
				break;
			} 
		}

		return existed;
	}

	//PURPOSE:	Print path method
	//IMPORT:	List of nodes
	//EXPORT:	None
	private static void printPath( LinkedList<Node> list ) {

		int i = 0;

		for( Node n : list ) {
			if (i > 0) {
	           		System.out.print(" --> ");
	        	}

       			System.out.print(n.getName());
       			i++;
		}

		System.out.println();
	}

}
