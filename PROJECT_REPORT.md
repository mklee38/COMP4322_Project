# COMP4322 Link State Routing Protocol - Project Report

## Group Information
- **Group Number**: [To be filled]
- **Group Members**:
  - Name: _________________ Student ID: ______________
  - Name: _________________ Student ID: ______________
  - Name: _________________ Student ID: ______________

## Executive Summary

This project implements a Java-based Link State Routing (LSR) protocol simulator that emulates the process done by routers exchanging Link State Advertisements (LSAs). The system performs Dijkstra's algorithm to compute shortest paths from a source router to all other routers in the network.

### Key Features Implemented
- **Mandatory**: LSA file parsing, Dijkstra's algorithm, path reconstruction, single-step and compute-all execution modes, CLI interface
- **Optional**: Full GUI with Swing, dynamic topology management, file persistence, multi-node support

---

## 1. Summary of LSR Algorithm

### 1.1 Overview
Link State Routing is a routing protocol where each router maintains a database of the network topology and disseminates updates about the state of its directly connected links.

### 1.2 How LSR Works
1. **Link State Advertisement (LSA)**: Each router announces the state and cost of its directly connected links
2. **Flooding**: LSAs are propagated to all routers in the network
3. **Topology Database**: All routers build a complete view of the network topology
4. **Dijkstra's Algorithm**: Each router independently calculates shortest paths to all other nodes

### 1.3 Dijkstra's Algorithm
Dijkstra's algorithm finds the shortest path from a source node to all other nodes in a weighted graph:

**Algorithm Steps**:
1. Initialize distances: source = 0, all others = ∞
2. Create set S of visited nodes (initially empty)
3. While unvisited nodes exist:
   a. Select unvisited node u with minimum distance
   b. Add u to S
   c. For each neighbor v of u:
      - If distance[u] + cost(u,v) < distance[v]:
        - Update distance[v]
        - Record u as previous node in shortest path to v

**Time Complexity**: O(V²) with simple implementation, O((V+E)logV) with priority queue

**Properties**:
- Guarantees shortest path in graphs with non-negative weights
- Produces shortest path tree (SPT) from source
- Single-source, all-destinations algorithm

---

## 2. System Design and Implementation

### 2.1 Architecture Overview

The implementation follows a modular, object-oriented design:

```
┌─────────────────────────────────────────────────────┐
│         User Interface Layer                         │
│  ┌──────────────────┐        ┌──────────────────┐   │
│  │   CLI Interface  │        │   GUI Interface  │   │
│  │   (LSRCompute)   │        │  (LSRComputeGUI) │   │
│  └────────┬─────────┘        └────────┬─────────┘   │
└────────────────────────────────────────────────────┐
             │ Uses               │ Uses
             ▼                    ▼
┌──────────────────────────────────────────────────┐
│      Algorithm and Data Structure Layer           │
│  ┌──────────────────┐    ┌──────────────────┐   │
│  │   DijkstraAlgo.  │    │     Graph        │   │
│  │   (Computation)  │    │   (Topology)     │   │
│  └────────┬─────────┘    └────────┬─────────┘   │
└──────────────────────────────────────────────────┘
             │ Uses               │ Uses
             ▼                    ▼
┌──────────────────────────────────────────────────┐
│         File I/O Layer                            │
│  ┌──────────────────────────────────────────┐   │
│  │      LSAFileParser                       │   │
│  │    (File Parsing & Writing)              │   │
│  └──────────────────────────────────────────┘   │
└──────────────────────────────────────────────────┘
```

### 2.2 Class Structure

#### Graph.java
Represents the network topology as an adjacency list.

**Key Methods**:
- `addNode(String node)`: Add a node to the graph
- `addEdge(String from, String to, int weight)`: Add bidirectional edge
- `getNeighbors(String node)`: Get adjacent nodes
- `removeNode/removeEdge()`: Dynamic topology removal

**Data Structure**:
```java
Map<String, Map<String, Integer>> adjacencyList
// node -> (neighbor -> cost)
```

#### DijkstraAlgorithm.java
Implements Dijkstra's shortest path algorithm with step-by-step support.

**Key Methods**:
- `executeSingleStep()`: Execute algorithm for single-step mode
- `executeComputeAll()`: Execute algorithm in batch mode
- `getNextStep()`: Get next node visited (for step-by-step visualization)
- `getDistance(String node)`: Query shortest distance
- `getPath(String node)`: Reconstruct and return shortest path

**Algorithm Features**:
- Bidirectional relaxation
- Path reconstruction via previous node tracking
- Unreachable node detection

#### LSAFileParser.java
Handles reading and writing LSA files.

**Key Methods**:
- `parseFile(String filename)`: Parse LSA file into Graph
- `writeFile(String filename, Graph graph)`: Persist graph to file
- `validateSourceNode()`: Check if node exists

**File Format**:
```
Node: Neighbor1:cost Neighbor2:cost ...
```

#### LSRCompute.java
Command-line interface main program.

**Features**:
- Argument parsing
- Mode selection (SS/CA)
- Error handling
- Interactive stepping in SS mode
- Result formatting and display

#### LSRComputeGUI.java
Full-featured Java Swing GUI application.

**Components**:
- File loader for LSA files
- Source node selector
- Execution mode buttons
- Network topology viewer
- Results display
- Dynamic topology editor (add/remove nodes and links)

### 2.3 Data Structures

**Primary Data Structures**:
1. **Adjacency List**: Graph representation (HashMap<String, HashMap<String, Integer>>)
2. **Distance Array**: Array of shortest distances to each node (HashMap<String, Integer>)
3. **Previous Array**: Previous node in shortest path (HashMap<String, String>)
4. **Visited Set**: Set of already processed nodes (HashSet<String>)

**Rationale**:
- Adjacency list chosen for sparse graphs with O(V+E) operations
- HashMaps allow O(1) average node lookups
- Visited set prevents reprocessing nodes

---

## 3. Implementation Details

### 3.1 File Parsing Algorithm

1. Read file line by line
2. Skip comments (lines starting with #) and empty lines
3. For each valid line:
   - Split by first colon to get source node and neighbors
   - Parse each neighbor specification "Node:cost"
   - Add bidirectional edges to graph

**Error Handling**:
- Invalid line format detection
- Cost parsing with try-catch
- Source node validation before algorithm execution

### 3.2 Single-Step Mode Implementation

In single-step mode, the `getNextStep()` method is called repeatedly:

```
Iteration 1: Process source, relax edges, return first destination
Iteration 2: Process source edge's destination, relax further
...
Until all reachable nodes visited
```

**Interactive Flow**:
1. Display: "Found X: Path: ... Cost: ..."
2. Wait for user input (ENTER key)
3. Continue to next step

### 3.3 Path Reconstruction

Paths are reconstructed by tracing backwards through previous nodes:

```java
String current = destination;
while (current != null) {
    path.addFirst(current);
    current = previousNodes.get(current);
}
// Result: [source, ..., destination]
```

### 3.4 Dynamic Topology Modifications

GUI provides interfaces to:
- Add nodes dynamically
- Remove nodes (removes all incident edges)
- Add edges with specified costs
- Remove edges (for link failure simulation)

All changes update the graph data structure immediately.

---

## 4. Test Results

### Test 1: Example Network (6 nodes, from project specification)

**Network**: A-B-C-D-E-F with various costs
**Source A**:
```
B: Path A>B Cost 5
C: Path A>C Cost 3
D: Path A>C>D Cost 4
E: Path A>C>D>E Cost 7
F: Path A>B>F Cost 7
```

**Verification**: Matches expected output from project description ✓

### Test 2: Multiple Source Nodes

**Source E** (same network):
```
B: Path E>B Cost 3
C: Path E>D>C Cost 4
D: Path E>D Cost 3
F: Path E>F Cost 5
A: Path E>D>C>A Cost 7
```

**Verification**: Correct shortest paths computed ✓

### Test 3: Single-Step Mode

- Successfully displays each node as discovered
- Correct path and cost shown at each step
- Summary table matches CA mode results ✓

### Test 4: Error Handling

- Invalid source node rejected with message ✓
- File not found handled gracefully ✓
- Invalid cost values caught ✓

### Test 5: Larger Network

Successfully tested with 8-node complex network ✓

**Conclusion**: All tests pass. Algorithm correctly computes shortest paths.

---

## 5. Features Implemented

### Mandatory Features ✓
- [x] Parse LSA files with proper format validation
- [x] Build graph representation of network topology
- [x] Implement Dijkstra's algorithm
- [x] Reconstruct and display shortest paths
- [x] Support single-step interactive mode with pausing
- [x] Support compute-all batch mode
- [x] Display results in specified format
- [x] Command-line interface

### Optional Features ✓
- [x] **GUI with Java Swing**: Full interactive interface with file dialogs, dropdowns, text areas
- [x] **Dynamic Topology**: Add/remove nodes and links at runtime through GUI
- [x] **File Persistence**: Modified topologies can be saved back to LSA files
- [x] **Comprehensive Testing**: Multiple test cases with various network sizes

---

## 6. Code Quality

### Code Organization
- **Modularity**: Clear separation of concerns (parsing, algorithm, UI)
- **Reusability**: Core algorithm independent of UI implementation
- **Maintainability**: Consistent naming, clear structure, proper abstractions

### Documentation
- **JavaDoc Comments**: All public methods documented
- **Inline Comments**: Complex logic explained
- **Code Readability**: Variable names self-documenting

### Error Handling
- **Graceful Degradation**: Invalid inputs handled without crashes
- **User Feedback**: Clear error messages for troubleshooting
- **Validation**: Input validation at multiple levels

### Design Patterns Used
- **Strategy Pattern**: Different execution modes (SS vs CA)
- **Observer Pattern**: GUI updates based on computation results
- **Factory Pattern**: Graph and algorithm creation

---

## 7. Known Limitations and Future Enhancements

### Known Limitations
1. No visual graph drawing (text representation only in CLI)
2. Assumes non-negative weights
3. Single-threaded implementation

### Possible Enhancements
1. Visual network topology rendering with node/edge drawing
2. Animation of Dijkstra's algorithm execution
3. Support for multiple routing algorithms (Bellman-Ford, Floyd-Warshall)
4. Network simulation with multiple routers running simultaneously
5. Export results to JSON/XML/PDF formats
6. Network traffic simulation
7. Dynamic link failure and recovery simulation

---

## 8. Conclusion

This project successfully implements a complete Link State Routing protocol simulator with:
- Core functionality exceeding project requirements
- User-friendly GUI and CLI interfaces
- Robust error handling and validation
- Clean, maintainable, well-documented code
- Comprehensive test coverage

The implementation demonstrates understanding of:
- Routing protocols and network algorithms
- Dijkstra's shortest path algorithm
- Software engineering principles
- GUI development with Java Swing
- File I/O and data structure design

---

## References

- Dijkstra, E. W. (1959). "A note on two problems in connexion with graphs"
- Kurose, J. F., & Ross, K. W. (2020). "Computer Networking" (8th ed.)
- RFC 2328: "OSPF Version 2" (Open Shortest Path First)
- GeeksforGeeks: Dijkstra's Algorithm tutorials
- Oracle Java Documentation: Swing GUI framework

---

## Appendix: Sample Output

### Compute-All Mode
```
=== Compute-All Mode ===

Source A:
B: Path: A>B Cost: 5
C: Path: A>C Cost: 3
D: Path: A>C>D Cost: 4
E: Path: A>C>D>E Cost: 7
F: Path: A>B>F Cost: 7
```

### Single-Step Mode
```
=== Single-Step Mode ===
Press ENTER to execute each step...

Found C: Path: A>C Cost: 3 [press any key to continue]
Found D: Path: A>C>D Cost: 4 [press any key to continue]
Found B: Path: A>B Cost: 5 [press any key to continue]
Found E: Path: A>C>D>E Cost: 7 [press any key to continue]
Found F: Path: A>B>F Cost: 7 [press any key to continue]

=== Summary ===
[complete summary table]
```
