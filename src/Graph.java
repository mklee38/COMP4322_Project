import java.util.*;

/**
 * Graph class represents the network topology for LSR (Link State Routing).
 * Uses an adjacency list representation with weighted edges.
 */
public class Graph {
    private Map<String, Map<String, Integer>> adjacencyList;
    private Set<String> nodes;

    public Graph() {
        adjacencyList = new HashMap<>();
        nodes = new HashSet<>();
    }

    /**
     * Add a node to the graph
     */
    public void addNode(String node) {
        nodes.add(node);
        adjacencyList.putIfAbsent(node, new HashMap<>());
    }

    /**
     * Add a bidirectional weighted edge between two nodes
     */
    public void addEdge(String from, String to, int weight) {
        addNode(from);
        addNode(to);
        
        adjacencyList.get(from).put(to, weight);
        adjacencyList.get(to).put(from, weight);
    }

    /**
     * Get all neighbors of a given node
     */
    public Map<String, Integer> getNeighbors(String node) {
        return adjacencyList.getOrDefault(node, new HashMap<>());
    }

    /**
     * Get all nodes in the graph
     */
    public Set<String> getNodes() {
        return new HashSet<>(nodes);
    }

    /**
     * Get the weight of an edge
     */
    public int getEdgeWeight(String from, String to) {
        Map<String, Integer> neighbors = adjacencyList.get(from);
        if (neighbors != null && neighbors.containsKey(to)) {
            return neighbors.get(to);
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Check if a node exists in the graph
     */
    public boolean containsNode(String node) {
        return nodes.contains(node);
    }

    /**
     * Get the number of nodes
     */
    public int getNodeCount() {
        return nodes.size();
    }

    /**
     * Print graph structure for debugging
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String node : nodes) {
            sb.append(node).append(": ");
            Map<String, Integer> neighbors = adjacencyList.get(node);
            for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Remove a node from the graph (for dynamic topology feature)
     */
    public void removeNode(String node) {
        nodes.remove(node);
        adjacencyList.remove(node);
        for (Map<String, Integer> neighbors : adjacencyList.values()) {
            neighbors.remove(node);
        }
    }

    /**
     * Remove an edge from the graph (for dynamic topology feature)
     */
    public void removeEdge(String from, String to) {
        if (adjacencyList.containsKey(from)) {
            adjacencyList.get(from).remove(to);
        }
        if (adjacencyList.containsKey(to)) {
            adjacencyList.get(to).remove(from);
        }
    }

    /**
     * Clear the graph
     */
    public void clear() {
        adjacencyList.clear();
        nodes.clear();
    }
}
