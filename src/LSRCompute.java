import java.io.*;
import java.util.*;

/**
 * LSRCompute - Main class for Link State Routing computation
 * Usage:
 *   java LSRCompute <file.lsa> <source_node> [SS|CA]
 *   java LSRCompute <file.lsa> <source_node>  (defaults to CA)
 */
public class LSRCompute {
    private static final String DEFAULT_MODE = "CA";

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        String filename = args[0];
        String sourceNode = args[1];
        String mode = args.length > 2 ? args[2].toUpperCase() : DEFAULT_MODE;

        // Validate mode
        if (!mode.equals("SS") && !mode.equals("CA")) {
            System.err.println("Error: Mode must be SS (Single-Step) or CA (Compute-All)");
            printUsage();
            System.exit(1);
        }

        try {
            // Parse the LSA file
            Graph graph = LSAFileParser.parseFile(filename);

            // Validate source node
            if (!LSAFileParser.validateSourceNode(graph, sourceNode)) {
                System.err.println("Error: Source node '" + sourceNode + "' not found in network");
                System.exit(1);
            }

            // Create Dijkstra solver
            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, sourceNode);

            // Execute based on mode
            if (mode.equals("SS")) {
                executeSingleStepMode(dijkstra);
            } else {
                executeComputeAllMode(dijkstra);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: File '" + filename + "' not found");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Execute in Single-Step mode
     */
    private static void executeSingleStepMode(DijkstraAlgorithm dijkstra) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Single-Step Mode ===");
        System.out.println("Press ENTER to execute each step...\n");

        String nextNode;
        while ((nextNode = dijkstra.getNextStep()) != null) {
            // Determine source and predecessor
            String path = dijkstra.getPath(nextNode);
            int cost = dijkstra.getDistance(nextNode);
            
            System.out.print("Found " + nextNode + ": Path: " + path + " Cost: " + cost);
            System.out.print(" [press any key to continue]");
            System.out.flush();
            
            scanner.nextLine();
        }

        // Print summary
        System.out.println("\n\n=== Summary ===");
        System.out.println(dijkstra.getResultsAsString());

        scanner.close();
    }

    /**
     * Execute in Compute-All mode
     */
    private static void executeComputeAllMode(DijkstraAlgorithm dijkstra) {
        System.out.println("=== Compute-All Mode ===\n");

        // Run Dijkstra
        dijkstra.executeComputeAll();

        // Print results
        System.out.println(dijkstra.getResultsAsString());
    }

    /**
     * Print usage information
     */
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  java LSRCompute <file.lsa> <source_node> [SS|CA]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  <file.lsa>      - Path to the LSA file");
        System.out.println("  <source_node>   - Starting node (e.g., 'A')");
        System.out.println("  [SS|CA]         - Execution mode (optional)");
        System.out.println("                    SS = Single-Step (interactive)");
        System.out.println("                    CA = Compute-All (default)");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java LSRCompute routes.lsa A SS");
        System.out.println("  java LSRCompute routes.lsa B CA");
    }
}
