import java.util.*;

/**
 * DijkstraAlgorithm class implements Dijkstra's shortest path algorithm
 * with support for both step-by-step execution and compute-all modes.
 */
public class DijkstraAlgorithm {
    private Graph graph;
    private String sourceNode;
    private Map<String, Integer> distances;
    private Map<String, String> previousNodes;
    private Set<String> visited;
    private Queue<String> stepQueue;
    private List<String> visitOrder;

    public DijkstraAlgorithm(Graph graph, String sourceNode) {
        this.graph = graph;
        this.sourceNode = sourceNode;
        this.distances = new HashMap<>();
        this.previousNodes = new HashMap<>();
        this.visited = new HashSet<>();
        this.stepQueue = new LinkedList<>();
        this.visitOrder = new ArrayList<>();
        
        // Initialize: source has distance 0, all others have infinity
        for (String node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE);
            previousNodes.put(node, null);
        }
        distances.put(sourceNode, 0);
    }

    /**
     * Execute Dijkstra's algorithm in single-step mode
     * Returns list of nodes in the order they are discovered
     */
    public List<String> executeSingleStep() {
        while (visited.size() < graph.getNodes().size()) {
            // Find unvisited node with minimum distance
            String current = getMinDistanceUnvisitedNode();
            if (current == null) break; // No more reachable nodes

            visited.add(current);
            visitOrder.add(current);

            // Relax edges
            Map<String, Integer> neighbors = graph.getNeighbors(current);
            for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
                String neighbor = entry.getKey();
                int weight = entry.getValue();

                if (!visited.contains(neighbor)) {
                    int newDistance = distances.get(current) + weight;
                    if (newDistance < distances.get(neighbor)) {
                        distances.put(neighbor, newDistance);
                        previousNodes.put(neighbor, current);
                    }
                }
            }
        }
        return visitOrder;
    }

    /**
     * Execute Dijkstra's algorithm in compute-all mode
     * Same as executeSingleStep, but we don't expose intermediate results
     */
    public void executeComputeAll() {
        executeSingleStep();
    }

    /**
     * Get the shortest distance to a node
     */
    public int getDistance(String node) {
        return distances.get(node);
    }

    /**
     * Get the shortest path to a node as a string (e.g., "A>B>C")
     */
    public String getPath(String node) {
        if (distances.get(node) == Integer.MAX_VALUE) {
            return "unreachable";
        }

        LinkedList<String> path = new LinkedList<>();
        String current = node;
        while (current != null) {
            path.addFirst(current);
            current = previousNodes.get(current);
        }
        return String.join(">", path);
    }

    /**
     * Get all distances as a map
     */
    public Map<String, Integer> getDistances() {
        return new HashMap<>(distances);
    }

    /**
     * Get all paths as a map
     */
    public Map<String, String> getPaths() {
        Map<String, String> paths = new HashMap<>();
        for (String node : graph.getNodes()) {
            paths.put(node, getPath(node));
        }
        return paths;
    }

    /**
     * Get all previous nodes (for tree construction)
     */
    public Map<String, String> getPreviousNodes() {
        return new HashMap<>(previousNodes);
    }

    /**
     * Get the order in which nodes were visited
     */
    public List<String> getVisitOrder() {
        return new ArrayList<>(visitOrder);
    }

    /**
     * Find the unvisited node with minimum distance
     */
    private String getMinDistanceUnvisitedNode() {
        String minNode = null;
        int minDistance = Integer.MAX_VALUE;

        for (String node : graph.getNodes()) {
            if (!visited.contains(node) && distances.get(node) < minDistance) {
                minNode = node;
                minDistance = distances.get(node);
            }
        }

        return minNode;
    }

    /**
     * Get the source node
     */
    public String getSourceNode() {
        return sourceNode;
    }

    /**
     * Check if all nodes have been visited
     */
    public boolean isComplete() {
        return visited.size() == graph.getNodes().size();
    }

    /**
     * Get the next node to visit in single-step mode
     * Call this repeatedly to execute one step at a time
     */
    public String getNextStep() {
        if (visited.size() >= graph.getNodes().size()) {
            return null;
        }

        String current = getMinDistanceUnvisitedNode();
        if (current == null) return null;

        visited.add(current);
        visitOrder.add(current);

        // Relax edges
        Map<String, Integer> neighbors = graph.getNeighbors(current);
        for (Map.Entry<String, Integer> entry : neighbors.entrySet()) {
            String neighbor = entry.getKey();
            int weight = entry.getValue();

            if (!visited.contains(neighbor)) {
                int newDistance = distances.get(current) + weight;
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previousNodes.put(neighbor, current);
                }
            }
        }

        // Don't report the source node as "found"
        if (current.equals(sourceNode)) {
            return getNextStep();
        }

        return current;
    }

    /**
     * Format results for display
     */
    public String getResultsAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Source ").append(sourceNode).append(":\n");
        
        // Sort nodes for consistent output
        List<String> sortedNodes = new ArrayList<>(graph.getNodes());
        Collections.sort(sortedNodes);

        for (String node : sortedNodes) {
            if (!node.equals(sourceNode)) {
                String path = getPath(node);
                int distance = distances.get(node);
                if (distance == Integer.MAX_VALUE) {
                    sb.append(node).append(": unreachable\n");
                } else {
                    sb.append(node).append(": Path: ").append(path)
                            .append(" Cost: ").append(distance).append("\n");
                }
            }
        }

        return sb.toString();
    }
}
