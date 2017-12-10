package eu.janvdb.aoc2016.day22;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.collection.Vector;

public class State {

	private final Map<Location, Node> nodeMap;
	private final Location dataLocation, emptyLocation;
	private final Vector<Tuple2<Node, Node>> moves;

	public State(Seq<Node> nodes) {
		this(nodes.toMap(node -> Tuple.of(node.getLocation(), node)), getInitialLocation(nodes), getEmptyLocation(nodes), Vector.empty());
	}

	private static Location getInitialLocation(Seq<Node> nodes) {
		int maxX = nodes.map(node -> node.getLocation().getX()).max().getOrElseThrow(IllegalArgumentException::new);
		return new Location(maxX, 0);
	}

	private static Location getEmptyLocation(Seq<Node> nodes) {
		return nodes.find(node -> node.getUse()==0)
				.map(Node::getLocation)
				.getOrElseThrow(IllegalArgumentException::new);
	}


	public State(Map<Location, Node> nodeMap, Location dataLocation, Location emptyLocation, Vector<Tuple2<Node, Node>> moves) {
		this.nodeMap = nodeMap;
		this.dataLocation = dataLocation;
		this.emptyLocation = emptyLocation;
		this.moves = moves;
	}

	public Stream<State> getNextStates() {
		Node emptyNode = nodeMap.get(emptyLocation).get();

		return emptyLocation.findNeighbourLocations()
				.flatMap(nodeMap::get)
				.filter(emptyNode::canTakeDataFrom)
				.filter(node -> !moves.contains(Tuple.of(node, emptyNode)))
				.map(node -> moveData(node, emptyNode));
	}

	private State moveData(Node fromNode, Node toNode) {
		Map<Location, Node> newNodeMap = nodeMap
				.put(fromNode.getLocation(), fromNode.emptied())
				.put(toNode.getLocation(), toNode.withExtraData(fromNode.getUse()));
		Location newDataLocation = fromNode.getLocation().equals(dataLocation) ? toNode.getLocation() : dataLocation;
		Location newEmptyLocation = toNode.getLocation().equals(emptyLocation) ? fromNode.getLocation() : emptyLocation;

		return new State(newNodeMap, newDataLocation, newEmptyLocation, moves.append(Tuple.of(fromNode, toNode)));
	}

	@Override
	public String toString() {
		return "State{" +
				"numberOfMoves=" + moves.size() +
				", dataLocation=" + dataLocation +
				", emptyLocation=" + emptyLocation +
				", moves=" + moves +
				'}';
	}

	public int getNumberOfMoves() {
		return moves.length();
	}

	public boolean isEndState() {
		return dataLocation.getX() == 0 && dataLocation.getY() == 0;
	}
}
