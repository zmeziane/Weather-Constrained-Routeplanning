package algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.GraphConsts;
import config.MathConsts;

import tools.GraphTools;

import models.Edge;
import models.Graph;
import models.Node;

public class DijkstraAlgorithm extends AbstractWCRSolver {
	
	private Map<Node,Node> previous;
	private Map<Node,Double> distance;
	private Map<Node,Double> entryTime;
	private List<Node> path;
	
	public DijkstraAlgorithm(Graph graph) {
		super(graph);
	}
	
	@Override
	public int pathLengthTo(Node target) {
		if(path != null && distance.get(target) != MathConsts.INFINITY) {
			return path.indexOf(target) + 1;
		}
		return 0;
	}	
	
	@Override
	public double distanceTo(Node target) {
		if(path != null && distance.get(target) != MathConsts.INFINITY)
    		return distance.get(target);
    	else
    		return 0;
	}

	@Override
	public double timeTo(Node target) {
		if(path != null && entryTime.get(target) != MathConsts.INFINITY)
    		return entryTime.get(target);
    	else
    		return 0;
	}

	@Override
	public List<Node> pathTo(Node source, Node target, int startTime,
			int maxTimeSteps) {
		distance = new HashMap<Node, Double>();
		entryTime = new HashMap<Node, Double>();
		previous = new HashMap<Node, Node>();
		
		distance.put(source, 0.0);
		previous.put(source, null);
		
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(source);
		
		while(!nodes.isEmpty())
		{
			Node u = getMinimum(nodes);
			nodes.remove(u);
			
			if(u == target) {
				path = reconstructPath(previous,u);
				return path;
			}
			
			for(Edge e : u.getNeighbors()) {
				Node neighbor = GraphTools.getNeighborFromEdge(e, u);
				double alt = distance.get(u) + e.getWeight();
				double neighborEntryTime = distance.get(neighbor) != null ? distance.get(neighbor) : MathConsts.INFINITY;
				if(alt < neighborEntryTime - MathConsts.EPSILON) {
					distance.put(neighbor, alt);
					entryTime.put(neighbor,alt/GraphConsts.VEHICLE_SPEED);
					previous.put(neighbor, u);
					nodes.add(neighbor);
				}
			}
		}
		
		return null;
	}
	
	private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node currentNode) {
    	if(cameFrom.get(currentNode) != null) {
			List<Node> p = reconstructPath(cameFrom, cameFrom.get(currentNode));
			p.add(currentNode);
			return p;
		}
		else {
			List<Node> p = new ArrayList<Node>();
			p.add(currentNode);
			return p;
		}
    }
	
	private Node getMinimum(List<Node> nodes) {
		Node min = null;
		for (Node node : nodes) {
			if(min == null || distance.get(node) < distance.get(min)) {
				min = node;
			}
		}
	    
	    return min;
	}
}
