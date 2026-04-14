# COMP4322 LSR Project - Quick Reference Guide

## One-Minute Overview

**What**: Java implementation of Link State Routing protocol
**Where**: /workspaces/COMP4322/
**How**: Dijkstra's algorithm for shortest path computation
**Modes**: CLI (command-line) and GUI (graphical interface)

---

## Quick Commands

### Compile
```bash
cd src
javac *.java
```

### Run GUI (Easiest)
```bash
cd src
java -cp . LSRComputeGUI
```

### Run CLI - Quick Mode
```bash
cd src
java -cp . LSRCompute ../data/routes.lsa A CA
```

### Run CLI - Step-by-Step
```bash
cd src
java -cp . LSRCompute ../data/routes.lsa A SS
```

### Run All Tests
```bash
bash test.sh
```

---

## File Organization

```
COMP4322/
├── src/                          # Source code
│   ├── Graph.java               # Network topology
│   ├── DijkstraAlgorithm.java   # Algorithm implementation
│   ├── LSAFileParser.java       # File I/O
│   ├── LSRCompute.java          # CLI interface
│   └── LSRComputeGUI.java       # GUI interface
│
├── data/                         # Sample network files
│   ├── routes.lsa               # Example 6-node network
│   ├── test_simple.lsa          # Simple 3-node network
│   └── test_complex.lsa         # Complex 8-node network
│
├── README.md                     # Quick start guide
├── PROJECT_REPORT.md            # Full project report
├── DESIGN_DOCUMENT.md           # UML diagrams & architecture
├── IMPLEMENTATION_SUMMARY.md    # Completion status
├── QUICK_REFERENCE.md           # This file
├── build.sh                      # Build script
└── test.sh                       # Test suite script
```

---

## LSA File Format Reference

### Basic Format
```
NodeName: Neighbor1:cost Neighbor2:cost ...
```

### Example
```
A: B:5 C:3 D:5
B: A:5 C:4 E:3 F:2
C: A:3 B:4 D:1 E:6
D: A:5 C:1 E:3
E: B:3 C:6 D:3 F:5
F: B:2 E:5
```

### Rules
- One line per node
- Node name and neighbors separated by colon
- Neighbors separated by spaces
- Each neighbor format: `NodeName:cost`
- Cost must be positive integer
- Bidirectional costs are symmetric
- Comments start with `#`
- Empty lines are ignored

---

## CLI Usage Reference

### Syntax
```
java LSRCompute <filename.lsa> <source_node> [MODE]
```

### Parameters
- `<filename.lsa>` - Path to network file (required)
- `<source_node>` - Starting node A-Z (required)
- `[MODE]` - Optional: SS (step-by-step) or CA (compute-all), default is CA

### Examples
```bash
# Compute-all from node A (default)
java LSRCompute routes.lsa A

# Compute-all explicitly
java LSRCompute routes.lsa A CA

# Single-step from node E
java LSRCompute routes.lsa E SS

# Test different networks
java LSRCompute test_simple.lsa C CA
java LSRCompute test_complex.lsa H CA
```

### Output Format
```
Source A:
B: Path: A>B Cost: 5
C: Path: A>C Cost: 3
D: Path: A>C>D Cost: 4
E: Path: A>C>D>E Cost: 7
F: Path: A>B>F Cost: 7
```

---

## GUI Usage Reference

### Starting
```bash
java -cp . LSRComputeGUI
```

### Steps
1. Click **"Load File"** - Select an .lsa file
2. Choose **"Source Node"** from dropdown
3. Click **"Single Step"** or **"Compute All"**
4. For Single Step: Click **"Next Step"** button to progress
5. View results in right panel

### Dynamic Topology
- **Add Node**: Enter node name, click "Add"
- **Remove Node**: Enter node name, click "Remove"
- **Add Link**: Enter from node, to node, cost, click "Add"
- **Break Link**: Enter link like "A-B", click "Break"

---

## Expected Output Examples

### Example 1: Compute-All Mode
```
=== Compute-All Mode ===

Source A:
B: Path: A>B Cost: 5
C: Path: A>C Cost: 3
D: Path: A>C>D Cost: 4
E: Path: A>C>D>E Cost: 7
F: Path: A>B>F Cost: 7
```

### Example 2: Single-Step Mode
```
=== Single-Step Mode ===
Press ENTER to execute each step...

Found C: Path: A>C Cost: 3 [press any key to continue]
Found D: Path: A>C>D Cost: 4 [press any key to continue]
Found B: Path: A>B Cost: 5 [press any key to continue]
Found E: Path: A>C>D>E Cost: 7 [press any key to continue]
Found F: Path: A>B>F Cost: 7 [press any key to continue]

=== Summary ===
Source A:
B: Path: A>B Cost: 5
C: Path: A>C Cost: 3
D: Path: A>C>D Cost: 4
E: Path: A>C>D>E Cost: 7
F: Path: A>B>F Cost: 7
```

---

## Troubleshooting

### Issue: "Compilation Error"
**Solution**: Ensure all .java files are in src/ directory and Java is installed
```bash
cd src
javac *.java
```

### Issue: "File not found"
**Solution**: Provide correct path to .lsa file
```bash
# Correct (from src directory)
java -cp . LSRCompute ../data/routes.lsa A CA

# Or with full path
java -cp . LSRCompute /path/to/routes.lsa A CA
```

### Issue: "Source node not found"
**Solution**: Check node name exists in file and matches case
```bash
# Node 'Z' doesn't exist - use 'A' through 'F'
java -cp . LSRCompute routes.lsa A CA ✓
java -cp . LSRCompute routes.lsa Z CA ✗
```

### Issue: "GUI doesn't display"
**Solution**: Ensure Java Swing is available (might need X11 forwarding if remote)
```bash
# Test if display works
java -cp . LSRComputeGUI &
```

---

## Testing Checklist

- [ ] Compile without errors: `javac *.java`
- [ ] CLI CA mode works: `java -cp . LSRCompute ../data/routes.lsa A CA`
- [ ] CLI SS mode works: `java -cp . LSRCompute ../data/routes.lsa A SS`
- [ ] Different source nodes: `java -cp . LSRCompute ../data/routes.lsa E CA`
- [ ] Error handling: `java -cp . LSRCompute ../data/routes.lsa Z CA` (should error)
- [ ] GUI launches: `java -cp . LSRComputeGUI` (should open window)
- [ ] GUI file loading: Click Load File, select routes.lsa
- [ ] All test networks: `bash test.sh` (should pass 4 tests)

---

## Algorithm Complexity

| Operation | Complexity | Notes |
|-----------|-----------|-------|
| File Parsing | O(V + E) | Linear in nodes and edges |
| Graph Building | O(V + E) | Adjacency list construction |
| Dijkstra's Algorithm | O(V²) | Simple array-based |
| Path Reconstruction | O(V) | Trace back through prev[] |
| **Total** | **O(V² + E)** | Dominated by Dijkstra |

Where V = number of nodes, E = number of edges

---

## Key Concepts Reference

### Link State Routing
- Each router knows complete network topology
- All routers run same algorithm independently
- Produces identical shortest path trees

### Dijkstra's Algorithm
1. Start from source (distance = 0)
2. Mark source as visited
3. For each unvisited neighbor, compute tentative distance
4. Select unvisited neighbor with smallest distance
5. Repeat until all reachable nodes visited
6. Reconstruct paths by tracing back through prev pointers

### Path Representation
- Format: `Source>Node1>Node2>Destination`
- Example: `A>C>D>E` means path is A → C → D → E
- Cost is sum of edge weights along path

---

## Common Tasks

### Create New Test Network
1. Create file: `data/mynetwork.lsa`
2. Add topology:
```
H: I:10 J:20
I: H:10 J:5
J: I:5 H:20
```
3. Test it:
```bash
cd src
java -cp . LSRCompute ../data/mynetwork.lsa H CA
```

### Modify Network (in GUI)
1. Launch GNU: `java -cp . LSRComputeGUI`
2. Load existing file
3. Add new node in "Add Node" field
4. Add links in "Add Link" field
5. Changes apply immediately

### Run Demonstration
```bash
# Terminal demo (shows output)
bash test.sh

# Interactive demo (shows step-by-step)
cd src
java -cp . LSRCompute ../data/routes.lsa A SS
# Press ENTER after each step

# GUI demo (shows interface)
cd src
java -cp . LSRComputeGUI
```

---

## Command Cheat Sheet

```bash
# Compilation
cd COMP4322/src && javac *.java

# GUI Application
java -cp . LSRComputeGUI

# CLI Compute-All
java -cp . LSRCompute ../data/routes.lsa A CA

# CLI Single-Step  
java -cp . LSRCompute ../data/routes.lsa A SS

# From different source
java -cp . LSRCompute ../data/routes.lsa B CA
java -cp . LSRCompute ../data/routes.lsa C CA

# Different test files
java -cp . LSRCompute ../data/test_simple.lsa A CA
java -cp . LSRCompute ../data/test_complex.lsa H CA

# Run all tests
cd .. && bash test.sh

# Check compilation
cd src && javac *.java && echo "Success"
```

---

## Documentation Files Reference

| File | Purpose |
|------|---------|
| README.md | Quick start and usage |
| PROJECT_REPORT.md | Comprehensive report |
| DESIGN_DOCUMENT.md | UML diagrams |
| IMPLEMENTATION_SUMMARY.md | Completion checklist |
| QUICK_REFERENCE.md | This reference (quick lookup) |

---

## Contact & Help

For specific Java version requirements:
- Requires: Java 8 or higher
- Compiler: javac
- Runtime: java

To verify Java installation:
```bash
java -version
javac -version
```

---

*Last Updated: April 14, 2026*
*Ready for Submission: YES ✓*
