import java.io.*;
import java.util.*;

/**
 * LSAFileParser class handles reading and parsing LSA (Link State Advertisement) files
 */
public class LSAFileParser {
    
    /**
     * Parse an LSA file and build a graph
     * File format:
     * A: B:5 C:3 D:5
     * B: A:5 C:4 E:3 F:2
     * ...
     */
    public static Graph parseFile(String filename) throws IOException {
        Graph graph = new Graph();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                
                parseLine(line, graph);
            }
        }
        
        return graph;
    }

    /**
     * Parse a single line from the LSA file
     * Format: "NodeA: NodeB:cost NodeC:cost ..."
     */
    private static void parseLine(String line, Graph graph) {
        // Split by colon but only on the first colon to separate node from its neighbors
        int colonIndex = line.indexOf(':');
        if (colonIndex < 0) {
            System.err.println("Invalid line format: " + line);
            return;
        }

        String sourceNode = line.substring(0, colonIndex).trim();
        graph.addNode(sourceNode);

        // Parse neighbors
        String neighborsString = line.substring(colonIndex + 1);
        String[] neighbors = neighborsString.split("\\s+");

        for (String neighborSpec : neighbors) {
            neighborSpec = neighborSpec.trim();
            if (neighborSpec.isEmpty()) continue;

            // Parse "NodeB:cost"
            int neighborColonIndex = neighborSpec.indexOf(':');
            if (neighborColonIndex > 0 && neighborColonIndex < neighborSpec.length() - 1) {
                String destNode = neighborSpec.substring(0, neighborColonIndex).trim();
                try {
                    int cost = Integer.parseInt(neighborSpec.substring(neighborColonIndex + 1).trim());
                    graph.addEdge(sourceNode, destNode, cost);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid cost format: " + neighborSpec);
                }
            }
        }
    }

    /**
     * Write graph back to LSA file format (for dynamic topology feature)
     */
    public static void writeFile(String filename, Graph graph) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            Set<String> nodes = graph.getNodes();
            List<String> sortedNodes = new ArrayList<>(nodes);
            Collections.sort(sortedNodes);

            for (String node : sortedNodes) {
                writer.print(node + ":");
                
                Map<String, Integer> neighbors = graph.getNeighbors(node);
                List<String> sortedNeighbors = new ArrayList<>(neighbors.keySet());
                Collections.sort(sortedNeighbors);

                for (String neighbor : sortedNeighbors) {
                    int cost = neighbors.get(neighbor);
                    writer.print(" " + neighbor + ":" + cost);
                }
                writer.println();
            }
        }
    }

    /**
     * Validate if source node exists in the graph
     */
    public static boolean validateSourceNode(Graph graph, String node) {
        return graph.containsNode(node);
    }
}
