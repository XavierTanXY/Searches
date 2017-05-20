//FILE:				BeamSearch.java
//AUTHOR:			Xhien Yi Tan ( Xavier )
//ID:				18249833
//UNIT:				Artificial and Machine Intelligence - COMP3006
//PURPOSE:			A class that is responsible for Beam Search
import java.util.*;

public class BeamSearch {

	//Start name and goal name for reference
	public static String goalName;
	public static String startName;

	//PURPOSE:	A method that starts the search
	//IMPORT:	All node from file, start node, goal node, k value
	//EXPORT:	None
	public static void beamSearch( HashMap<String, Node> allNodes, Node startNode, Node goalNode, int k ) {

		goalName = goalNode.getName();
		startName = startNode.getName();

		search( startNode, k, allNodes );
	}

	//PURPOSE:	A method that does the search
	//IMPORT:	Start node, k value, All nodes from file
	//EXPORT:	None
	private static void search( Node startNode, int k, HashMap<String, Node> allNodes ) {

		Queue<LinkedList<Node>> queue = new LinkedList<LinkedList<Node>>(); //Queue of list of nodes
		ArrayList<Node> children = startNode.getChildNode(); //start node's children

		ArrayList<LinkedList<Node>> childrenList = new ArrayList<LinkedList<Node>>(); //list of children stored in array
		LinkedList<LinkedList<Node>> allPathLists = new LinkedList<LinkedList<Node>>();
		HashMap<Integer, LinkedList<LinkedList<Node>>> allPathSet = new HashMap<Integer, LinkedList<LinkedList<Node>>>();

		Set<String> noDuplicate; //set to get rid of duplicate children

		int numChildren = children.size();

		double smallest = Double.MAX_VALUE;
		double secondSmallest = Double.MAX_VALUE;
		double thirdSmallest = Double.MAX_VALUE;

		/**
		* 	Starting state:
		*	case 1: get the smallest heuristic node and put it in a list with the start node.
		*			For example: A -> B
		*	case 2: get two smallest heuristic node and put it in a list with the start node.
		*			For example: A -> B, A -> C
		*	case 3: get three smallest heuristic node and put it in a list with the start node.
		*			For example: A -> B, A -> C, A -> D
		*/

		switch( k ) {
			case 1:
				smallest = children.get(0).getDistanceToGoal();

			    	for( int i = 0; i < children.size(); i++ ) {

			    		double heuristic = children.get(i).getDistanceToGoal();

				    	//Get smallest heuristic
				      	if( heuristic < smallest) {
				       		smallest = heuristic;
				      	}

					//At the same time, create a list that put parent and its children
				      	LinkedList<Node> list = new LinkedList<Node>();
					list.addLast( startNode );
					list.addLast( children.get(i) );

					//Store that list into this  array
					childrenList.add( list );
			    	}

			    	for( LinkedList<Node> l : childrenList ) {
					
					//Whenver found the smallest heuristic, add it into the queue
					if( l.getLast().getDistanceToGoal() == smallest ) {
						queue.add( l );
					} else {
						allPathLists.addLast( l );
					}
				}

				break;
			case 2:
				for( int i = 0; i < children.size(); i++ ) {
					double heuristic = children.get(i).getDistanceToGoal();

					//Get two smallest heuristic
					if(  heuristic == smallest ) {
						secondSmallest = smallest;
					} else if( heuristic < smallest ) {
						secondSmallest = smallest;
						smallest = heuristic;
					} else if( heuristic < secondSmallest ) {
						secondSmallest = heuristic;
					}

					//At the same time, create a list that put parent and its children
					LinkedList<Node> list = new LinkedList<Node>();
					list.addLast( startNode );
					list.addLast( children.get(i) );
					childrenList.add( list );
				}

				for( LinkedList<Node> l : childrenList ) {

					//Whenver found the smallest heuristic, add it into the queue
					if( l.getLast().getDistanceToGoal() == smallest ) {
						queue.add( l );
					} else if( l.getLast().getDistanceToGoal() == secondSmallest ) {
						queue.add( l );
					} else {
						allPathLists.addLast( l );
					}
				}


				childrenList.clear();
				allPathSet.put( numChildren, allPathLists );
				break;

			case 3:

			  	for( int i = 0; i < children.size(); i++ ) {
				    	double heuristic = children.get(i).getDistanceToGoal();

				    		//Get three smallest heuristic
					if (heuristic < thirdSmallest && heuristic >= secondSmallest){
					    thirdSmallest = heuristic;
					}
					if (heuristic < secondSmallest && heuristic >= smallest){
					    thirdSmallest = secondSmallest;
					    secondSmallest = heuristic;
					}
					if (heuristic < smallest){
					    thirdSmallest = secondSmallest;
					    secondSmallest = smallest;
					    smallest = heuristic;
					}

					LinkedList<Node> list = new LinkedList<Node>();
					list.addLast( startNode );
					list.addLast( children.get(i) );
					childrenList.add( list );
			 	}


			    	for( LinkedList<Node> l : childrenList ) {

					if( l.getLast().getDistanceToGoal() == smallest ) {
						queue.add( l );
					} else if( l.getLast().getDistanceToGoal() == secondSmallest ) {
						queue.add( l );
					} else if( l.getLast().getDistanceToGoal() == thirdSmallest ) {
						queue.add( l );
					} else {
						allPathLists.addLast( l );
					}
				}

				childrenList.clear();
				allPathSet.put( numChildren, allPathLists );

				break;
		} //end of switch

		while( !queue.isEmpty() ) {
			//Dequeue a list of node
			LinkedList<Node> currentList = queue.poll();
			//Get the last node from the list
			Node n1 = currentList.getLast();

			LinkedList<LinkedList<Node>> allLists = new LinkedList<LinkedList<Node>>();
			ArrayList<Double> smallestChildren;

		/**
		* 	Searching state:
		*	case 1: if found goal, stop the program. If not search
		*	case 2: get two smallest heuristic node and put it in a list with the start node.
		*			For example: A -> B, A -> C
		*	case 3: get three smallest heuristic node and put it in a list with the start node.
		*			For example: A -> B, A -> C, A -> D
		*/

			switch( k ) {
				case 1:
					if( n1.getName().equals( goalName ) ) {
						printGoal( currentList );
						System.exit(0);

					} else {
						//Get the children from current node
						children = n1.getChildNode();

						//Append all its children to the parent
						allLists = appendChildToList( children, currentList );
						smallestChildren = getSmallest( allLists );
						addSmallestChild( smallestChildren, allLists, queue, allPathLists, k );
					}

					break;
				case 2:
					if( n1.getName().equals( goalName ) ) {
						printGoal( currentList );
						printPartialPath( allPathSet );
						inputFromUser();

					} else {
						children = n1.getChildNode();
						allLists = appendChildToList( children, currentList );
					}

					while( !queue.isEmpty() ) {
						LinkedList<Node> secondList = queue.poll();

						//Get second node in the queue
						Node n2 = secondList.getLast();

						//Found goal
						if( n2.getName().equals( goalName ) ) {

							printGoal( secondList );
							printPartialPath( allPathSet );
							inputFromUser();

						} else {

							//Get the children of second node
							ArrayList<Node> secondChildren = n2.getChildNode();
							//Append all its children to the parent
							LinkedList<LinkedList<Node>> allLists_2 = appendChildToList( secondChildren, secondList );
							//All first node's children to second's node children
							allLists.addAll( allLists_2 );
						}


					}

					//Get rid of duplicate if there are
					noDuplicate = removeDuplicateNode( allLists );
					//Get two smallest heursitic children from them
					smallestChildren = getTwoSmallest( allNodes, noDuplicate );
					//Add it into queue and all paths
					addSmallestChild( smallestChildren, allLists, queue, allPathLists, k );
					break;

				case 3:
					if( n1.getName().equals( goalName ) ) {
						printGoal( currentList );
						printPartialPath( allPathSet );
						inputFromUser();

					} else {
						children = n1.getChildNode();
						allLists = appendChildToList( children, currentList );
					}

					while( !queue.isEmpty() ) {
						LinkedList<Node> secondList = queue.poll();
						Node n2 = secondList.getLast();

						if( n2.getName().equals( goalName ) ) {
							printGoal( secondList );
							printPartialPath( allPathSet );
							inputFromUser();

						} else {
							ArrayList<Node> secondChildren = n2.getChildNode();
							LinkedList<LinkedList<Node>> allLists_2 = appendChildToList( secondChildren, secondList );

							allLists.addAll( allLists_2 );
						}


					}

					//Same as k = 2, but it gets a third child from queue if there is
					while( !queue.isEmpty() ) {
						LinkedList<Node> thirdList = queue.poll();
						Node n3 = thirdList.getLast();


						if( n3.getName().equals( goalName ) ) {
							printGoal( thirdList );
							printPartialPath( allPathSet );
							inputFromUser();

						} else {
							ArrayList<Node> thirdChildren = n3.getChildNode();
							LinkedList<LinkedList<Node>> allLists_3 = appendChildToList( thirdChildren, thirdList );

							allLists.addAll( allLists_3 );
						}


					}

					noDuplicate = removeDuplicateNode( allLists );
					smallestChildren = getThreeSmallest( allNodes, noDuplicate );
					addSmallestChild( smallestChildren, allLists, queue, allPathLists, k );

					break;
			}

		}

		//Print remaing or partial path when it finishes
		printRemainingPath( k, allPathSet );

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
			System.exit(0); //Quit the program is the user choose No
		}

		return want;
	}

	//PURPOSE:	A method that remove duplicate node and return a set of unique node
	//IMPORT:	List of lists of node
	//EXPORT:	Set of unique node name
	private static Set<String> removeDuplicateNode( LinkedList<LinkedList<Node>> allLists ) {

		Set<String> setOfDuplicate = new HashSet<String> ();
		Set<String> setOfUnique = new HashSet<String> ();

		for (LinkedList<Node> list : allLists) {
			Node n = list.getLast();
			String name = n.getName();

			if ( !setOfUnique.add( name ) ) {
				setOfDuplicate.add( name );
			}
		}

		return setOfUnique;
	}

	//PURPOSE:	Get smallest heuristic k number of children from the array and get the node from the list
	//IMPORT:	Children, all of the list or path, queue for search, all paths at the moment, k value
	//EXPORT:	None
	private static void addSmallestChild( ArrayList<Double> smallestChildren, LinkedList<LinkedList<Node>> allLists, Queue<LinkedList<Node>> queue,  LinkedList<LinkedList<Node>> allPathLists, int k ) {

		double smallest = 0;
		double secondSmallest = 0;
		double thirdSmallest = 0;

		if( k == 1) {
			if( smallestChildren.size() > 0 ) {
				smallest = smallestChildren.get(0);
			}

			for( LinkedList<Node> l : allLists ) {

				//Get the smallest node from the list using its heuristic value, and add to queue
				if( l.getLast().getDistanceToGoal() == smallest ) {
					queue.add( l );
				} else {
					//Add the all path list if it is not
					allPathLists.addLast( l );
				}
			}


		} else if( k == 2 ) {
			if( smallestChildren.size() == 2 ) {
				smallest = smallestChildren.get(0);
				secondSmallest = smallestChildren.get(1);
			}

			//Same as k = 1 just that it gets one more child than k = 1
			for( LinkedList<Node> l : allLists ) {

				if( l.getLast().getDistanceToGoal() == smallest ) {
					queue.add( l );
				} else if( l.getLast().getDistanceToGoal() == secondSmallest ) {
					queue.add( l );
				} else {
					allPathLists.addLast( l );
				}
			}

		} else if( k == 3 ) {
			if( smallestChildren.size() == 3 ) {
				smallest = smallestChildren.get(0);
				secondSmallest = smallestChildren.get(1);
				thirdSmallest = smallestChildren.get(2);
			}

			//Same as k = 2 just that it gets one more child than k = 2
			for( LinkedList<Node> l : allLists ) {

					if( l.getLast().getDistanceToGoal() == smallest ) {
						queue.add( l );
					} else if( l.getLast().getDistanceToGoal() == secondSmallest ) {
						queue.add( l );
					} else if( l.getLast().getDistanceToGoal() == thirdSmallest ) {
						queue.add( l );
					} else {
						allPathLists.addLast( l );
					}
				}
		}

	}

	//PURPOSE:	Get three smallest heuristic node from all nodes that are read from file
	//IMPORT:	All nodes, unique name of children - no duplicate name
	//EXPORT:	Array of three smallest heuristic value
	private static ArrayList<Double> getThreeSmallest( HashMap<String, Node> allNodes, Set<String> childrenName  ) {

		ArrayList<Double> smallestChildren = new ArrayList<Double>();
		ArrayList<Double> returnChildren = new ArrayList<Double>();

		for( String name: childrenName ) {
			smallestChildren.add( allNodes.get( name ).getDistanceToGoal() );
		}

		double smallest = Double.MAX_VALUE;
		double secondSmallest = Double.MAX_VALUE;
		double thirdSmallest = Double.MAX_VALUE;

    		for( double heuristic : smallestChildren ) {

		    	if( heuristic < thirdSmallest && heuristic >= secondSmallest ) {
				thirdSmallest = heuristic;
		    	}
		    	if( heuristic < secondSmallest && heuristic >= smallest ) {
		      	  thirdSmallest = secondSmallest;
				secondSmallest = heuristic;
		    	}
		    	if( heuristic < smallest ) {
		      	  thirdSmallest = secondSmallest;
		      	  secondSmallest = smallest;
		      	  smallest = heuristic;
		   	}
    		}


		returnChildren.add( smallest );
		returnChildren.add( secondSmallest );
		returnChildren.add( thirdSmallest );


	    	return returnChildren;
	}

	//PURPOSE:	Get two smallest heuristic node from all nodes that are read from file
	//IMPORT:	All nodes, unique name of children - no duplicate name
	//EXPORT:	Array of two smallest heuristic value
	private static ArrayList<Double> getTwoSmallest( HashMap<String, Node> allNodes, Set<String> childrenName ) {
		ArrayList<Double> smallestChildren = new ArrayList<Double>();
		ArrayList<Double> returnChildren = new ArrayList<Double>();

		for( String name: childrenName ) {
			smallestChildren.add( allNodes.get( name ).getDistanceToGoal() );
		}

		double smallest = Double.MAX_VALUE;
		double secondSmallest = Double.MAX_VALUE;

		for( double heuristic : smallestChildren ) {

			if(  heuristic == smallest ) {
				secondSmallest = smallest;
			} else if( heuristic < smallest ) {
				secondSmallest = smallest;
				smallest = heuristic;
			} else if( heuristic < secondSmallest ) {
				secondSmallest = heuristic;
			}

		}

		returnChildren.add( smallest );
		returnChildren.add( secondSmallest );

		return returnChildren;
	}

	//PURPOSE:	Get smallest heuristic node from all nodes that are read from file
	//IMPORT:	All nodes, unique name of children - no duplicate name
	//EXPORT:	Array of one smallest heuristic value
	private static ArrayList<Double> getSmallest( LinkedList<LinkedList<Node>> allPath ) {
		ArrayList<Double> smallestChildren = new ArrayList<Double>();

		double minValue = Double.MAX_VALUE;

		for( LinkedList<Node> list : allPath ) {

			double heuristic = list.getLast().getDistanceToGoal();
			if( heuristic < minValue ) {
	       			minValue = heuristic;
	    		}

		}

	  	smallestChildren.add( minValue );

	    	return smallestChildren;
	}

	//PURPOSE:	Append children to the last of the parent list
	//IMPORT:	Children, current parent list
	//EXPORT:	All lists that contains all path
	private static LinkedList<LinkedList<Node>> appendChildToList( ArrayList<Node> children, LinkedList<Node> currentList  ) {
		LinkedList<LinkedList<Node>> allLists = new LinkedList<LinkedList<Node>>();
		LinkedList<Node> newList;

		Node prevParent = currentList.getLast();

		/**
		* Create a new list each time for each children in case
		* the parent branches out to more than one children
		*/
		for( int i = 0; i < children.size(); i++ ) {
			newList = new LinkedList<Node>();
			Node child = children.get(i);

			if( !child.getName().equals( prevParent.getName() ) ) {

				if( !checkExisted( currentList, child ) ) { //Check if the child is already in the path

					/**
					* Then move the current path list to this new list with the new child
					* without affecting the original list.
					*/
					for( Node n : currentList ) {
						newList.addLast( n );
					}

					newList.addLast( child );
					allLists.addLast( newList );
				}
			}

		}

		return allLists;

	}

	//PURPOSE:	Check whether a node is already in the path
	//IMPORT:	List of path known as path, current node
	//EXPORT:	Whether the node is in the path
	private static boolean checkExisted( LinkedList<Node> currentList, Node child ) {

		boolean existed = false;

		for( Node n : currentList ) {

			if( n.getName().equals( child.getName() ) ) {
				existed = true;
				break;
			}
		}

		return existed;
	}

	//PURPOSE:	Print goal path
	//IMPORT:	List of goal path
	//EXPORT:	None
	private static void printGoal( LinkedList<Node> list ) {

		System.out.println( "=============== FOUND GOAL ==============" );
		printPath( list );
	}

	//PURPOSE:	Print path
	//IMPORT:	List of path
	//EXPORT:	None
	private static void printPath( LinkedList<Node> list ) {

		int i = 0;

		for( Node n : list ) {
			//For output format only
			if (i > 0) {
	    			System.out.print(" --> ");
	  		}

      			System.out.print(n.getName());
      			i++;
		}

		System.out.println();
	}

	//PURPOSE:	Print remaining path
	//IMPORT:	k value, all reaming path in set
	//EXPORT:	None
	private static void printRemainingPath( int k, HashMap<Integer, LinkedList<LinkedList<Node>>> allPathSet ) {

		System.out.println( "=============== No More Solution ===============");
		 if( k == 2 || k == 3) {
			printPartialPath( allPathSet );
		}

	}

	//PURPOSE:	Print partial path
	//IMPORT:	all reaming path in set
	//EXPORT:	None
	private static void printPartialPath( HashMap<Integer, LinkedList<LinkedList<Node>>> allPathSet ) {

		System.out.println( "=============== Partial Path ==============" );
		Iterator it = allPathSet.entrySet().iterator();
		while (it.hasNext()) {
	  		Map.Entry pair = (Map.Entry)it.next();

	   	 	LinkedList<LinkedList<Node>> l = allPathSet.get( pair.getKey() );

	    		for( LinkedList<Node> ll : l ) {
	    			printPath( ll );
	   		}

		}

	}

}
