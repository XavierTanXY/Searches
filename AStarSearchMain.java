//FILE:				AStarSearchMain.java
//AUTHOR:			Xhien Yi Tan ( Xavier )
//ID:				18249833
//UNIT:				Artificial and Machine Intelligence - COMP3006 
//PURPOSE:			A class that runs for A* Search and also IO
import java.util.*;

public class AStarSearchMain {

	public static void main(String args[]) {

		String fileName = args[0];
		String heuristicFile = args[1];

		try {

			System.out.println( "Start Node: " + args[2] + ", Goal Node: " + args[3] );
			System.out.println();

			//Read file and return all nodes from file
			HashMap<String, Node> allNodes = FileIO.readGraphInfo( fileName );
			String startNodeName = args[2];
			String goalNodeName = args[3];

			//Check whether start node or goal node is in the file
			if( !allNodes.containsKey( startNodeName ) || !allNodes.containsKey( goalNodeName) )  {
				throw new IllegalArgumentException( "Start node or Goal node does not exist" );
			} else {
		    
 				//Read heuristic
				FileIO.readHeuristicInfo( heuristicFile, allNodes );	 
				Node startNode = allNodes.get( args[2] );
 				Node goalNode = allNodes.get( args[3] );	

 				System.out.println();
				AStarSearch.aStarSearch( startNode, goalNode );
			}	

		}
		catch ( IllegalArgumentException e ) {
			System.out.println( "Error : " + e.getMessage() );
		}
		catch ( NullPointerException e2 ) {
			System.out.println( "Error :" + e2.getMessage() );
		}
		catch ( Exception e3 ) {
			System.out.println( "Error :" + e3.getMessage() );
		}

	}	
}