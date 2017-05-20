//FILE:             FileIO.java
//AUTHOR:           Xhien Yi Tan ( Xavier )
//ID:               18249833
//UNIT:             Artificial and Machine Intelligence - COMP3006
//PURPOSE:          A class is responsible of reading from a file
import java.io.*;
import java.util.*;

public class FileIO
{
    //PURPOSE:	Reading the file and process nodes
	//IMPORT:	A file name
	//EXPORT:	All nodes from the file
	public static HashMap<String, Node> readGraphInfo( String filename ) throws IOException {

		String strLine;
		Node returnNode;

		HashMap<String, Node> allNodes = new HashMap<String, Node>();

		try {

			//Open the file
			FileInputStream fstream = new FileInputStream(filename);
			//Create a reader to read the stream
			DataInputStream in = new DataInputStream(fstream);
			//To read the stream one line at a time
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			//Read File Line By Line
			while ( ( strLine = br.readLine() ) != null ) {
				processGraph( strLine, allNodes );
			}

			System.out.println( "Finish Reading Graph..." );

			in.close(); //Close File
		}
		catch ( IOException e ) { //Catch IO exception if any
			System.out.println( "Error in file processing: " + e.getMessage() );
		} catch( Exception e2 ) {
			throw new IOException( "Invalid file format found: " + e2.getMessage() );
		}

		return allNodes;

	}

	//PURPOSE:	Add details to each neighbour node
	//IMPORT:	Parent node, child node, the distance between them
	//EXPORT:	None
	private static void addDetails( Node parentNode, Node childNode, double distance ) {

		parentNode.addAdjDetails( childNode, distance );

		childNode.addAdjDetails( parentNode, distance );
	}

	//PURPOSE:	Process each line from the file to make node
	//IMPORT:	Line from file, all nodes
	//EXPORT:	None
	private static void processGraph( String strLine, HashMap<String, Node> allNodes ) {

		String firstToken = "", secondToken = "", thirdToken = "";
		StringTokenizer strTok;

		Node node, adjNode, tempNode;
		int distance;
		boolean found, found2;

		List<Node> nodeToAdd = new ArrayList<>();

		//Creates a StringTokenizer using string line
		strTok = new StringTokenizer( strLine," " );

		while( strTok.hasMoreTokens() )	{

			found = false;
			found2 = false;

			//Get each tokens from the line
			firstToken = strTok.nextToken();
			secondToken = strTok.nextToken();
			thirdToken = strTok.nextToken();

			distance = Integer.parseInt( thirdToken );

			//When reading first node from the file
			if( allNodes.size() == 0 ) {

				node = new Node( firstToken );
				adjNode = new Node( secondToken );

				addDetails( node, adjNode, distance );

				//put theses nodes into hash map to store
				allNodes.put( node.getName(), node );
				allNodes.put( adjNode.getName(), adjNode );


			} else {

				//If the hash map contains this node
				if( allNodes.containsKey( firstToken ) ) {

					//Creates its neighbour
					adjNode = new Node( secondToken );

					//Get the node that has already created from has map
					Node parentNode = allNodes.get( firstToken );

					//If the hash map contains its neighbour
					if( allNodes.containsKey( secondToken ) ) {

						//Get the node that has already created from has map
						Node childNode = allNodes.get( secondToken );
						addDetails( parentNode, childNode, distance );

					} else {

						addDetails( parentNode, adjNode, distance);
						allNodes.put( adjNode.getName(), adjNode );
					}

				} else {

					node = new Node( firstToken );
					allNodes.put( node.getName(), node );
					adjNode = new Node( secondToken );

					//If the hash map contains this node
					if( allNodes.containsKey( secondToken ) ) {

						Node childNode = allNodes.get( secondToken );
						addDetails( node, childNode, distance );

					} else {

						addDetails( node, adjNode, distance );
						allNodes.put( adjNode.getName(), adjNode );

					}
				}
			}
		}
	}

	//PURPOSE:	Read heuristic from file to node
	//IMPORT:	File name, all nodes
	//EXPORT:	None
	public static void readHeuristicInfo( String filename, HashMap<String, Node> allNodes ) throws IOException {
		String strLine;
		Node returnNode;

		try {

			//Open the file
			FileInputStream fstream = new FileInputStream(filename);
			//Create a reader to read the stream
			DataInputStream in = new DataInputStream(fstream);
			//To read the stream one line at a time
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			//Get rid of empty line
			strLine = br.readLine();
			strLine = br.readLine();

			//Read File Line By Line
			while ( ( strLine = br.readLine() ) != null ) {
				processHeuristic( strLine, allNodes );
			}

			System.out.println( "Finish Reading Heuristic..." );

			in.close(); //Close File
		}
		catch ( IOException e ) { //Catch IO exception if any
			System.out.println( "Error in file processing: " + e.getMessage() );
		} catch( Exception e2 ) {
			throw new IOException( "Invalid file format found: " + e2.getMessage() );
		}
	}

	//PURPOSE:	Process heuristic to node
	//IMPORT:	Line from file, all nodes
	//EXPORT:	None
	private static void processHeuristic( String strLine, HashMap<String, Node> allNodes ) {

		String firstToken = "", secondToken = "", thirdToken = "";
		StringTokenizer strTok;

		Node node = null;
		double heuristicDistance;
		boolean found;

		//Creates a StringTokenizer using string line
		strTok = new StringTokenizer( strLine," " );

		while( strTok.hasMoreTokens() )	{

			firstToken = strTok.nextToken();
			secondToken = strTok.nextToken();

			heuristicDistance = Double.parseDouble( secondToken );

			//If the hash map contains this node, set its heuristic
			if( allNodes.containsKey( firstToken ) ) {
				node = allNodes.get( firstToken );
				node.setDistanceToGoal( heuristicDistance );
			}

		}

	}

}
