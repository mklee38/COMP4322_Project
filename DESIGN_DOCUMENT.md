# UML Class Diagram - COMP4322 LSR Project

## Class Relationships

```
┌─────────────────────────────────────────────────────────────────┐
│                    LSRCompute (main)                             │
│  ─────────────────────────────────────────────────────────────  │
│  - main(String[] args): void                                    │
│  - executeSingleStepMode(DijkstraAlgorithm): void              │
│  - executeComputeAllMode(DijkstraAlgorithm): void              │
│  - printUsage(): void                                            │
└─────────────────────────────────────────────────────────────────┘
                            │ uses
                            ▼
        ┌──────────────────────────────────────┐
        │     LSAFileParser (utility)           │
        ├──────────────────────────────────────┤
        │ + parseFile(String): Graph           │
        │ + writeFile(String, Graph): void     │
        │ - parseLine(String, Graph): void     │
        │ + validateSourceNode(Graph, String)  │
        └──────────────────────────────────────┘
                   │ reads/writes
                   ▼
        ┌──────────────────────────────────┐
        │         .lsa File                 │
        │  (Network Topology Description)  │
        └──────────────────────────────────┘


        ┌──────────────────────────────────┐
        │     Graph (data structure)       │
        ├──────────────────────────────────┤
        │ - adjacencyList: Map<String,     │
        │      Map<String, Integer>>       │
        │ - nodes: Set<String>             │
        ├──────────────────────────────────┤
        │ + addNode(String): void          │
        │ + addEdge(String, String, int)   │
        │ + removeNode(String): void       │
        │ + removeEdge(String, String)     │
        │ + getNeighbors(String):          │
        │      Map<String, Integer>        │
        │ + getNodes(): Set<String>        │
        │ + getEdgeWeight(): int           │
        │ + containsNode(String): boolean  │
        │ + getNodeCount(): int            │
        │ + clear(): void                  │
        └──────────────────────────────────┘
                   △
                   │ uses
                   │
        ┌──────────────────────────────────┐
        │  DijkstraAlgorithm               │
        ├──────────────────────────────────┤
        │ - graph: Graph                   │
        │ - sourceNode: String             │
        │ - distances: Map<String, Int>    │
        │ - previousNodes: Map<String>     │
        │ - visited: Set<String>           │
        │ - visitOrder: List<String>       │
        ├──────────────────────────────────┤
        │ + executeSingleStep(): List      │
        │ + executeComputeAll(): void      │
        │ + getNextStep(): String          │
        │ + getDistance(String): int       │
        │ + getPath(String): String        │
        │ + getDistances(): Map            │
        │ + getPaths(): Map                │
        │ + getPreviousNodes(): Map        │
        │ + getVisitOrder(): List          │
        │ + getSourceNode(): String        │
        │ + isComplete(): boolean          │
        │ + getResultsAsString(): String   │
        │ - getMinDistanceUnvisited(): Str │
        └──────────────────────────────────┘
                   △
                   │ uses
                   └─────────────────────────┐
                                   │         │
        ┌──────────────────────────┴──┐   ┌──┴─────────────────────┐
        │   LSRCompute (CLI)           │   │  LSRComputeGUI (GUI)   │
        ├──────────────────────────────┤   ├────────────────────────┤
        │ + main(String[]): void       │   │ - graph: Graph         │
        │ - executeSingleStepMode()    │   │ - dijkstra: Dijkstra   │
        │ - executeComputeAllMode()    │   │ - topologyArea: JTA    │
        │ - printUsage()               │   │ - resultsArea: JTA     │
        │                              │   │ - statusArea: JTA      │
        │ Input: File + Node + Mode    │   │ - Controls: JButton    │
        │ Output: Console              │   │ - sourceNodeCombo: JCB │
        └──────────────────────────────┘   ├────────────────────────┤
                                           │ - initializeGUI()      │
                                           │ - loadFile()           │
                                           │ - startSingleStep()    │
                                           │ - nextStep()           │
                                           │ - computeAll()         │
                                           │ - reset()              │
                                           │ - addNode()            │
                                           │ - removeNode()         │
                                           │ - addLink()            │
                                           │ - breakLink()          │
                                           │ - updateDisplay()      │
                                           │                        │
                                           │ Input: GUI Dialogs     │
                                           │ Output: GUI Display    │
                                           └────────────────────────┘
```

## Data Flow Diagram

```
                    ┌──────────────┐
                    │ LSA File     │
                    │ (routes.lsa) │
                    └────────┬─────┘
                             │
                             ▼
                  ┌──────────────────────┐
                  │  LSAFileParser       │
                  │  - parseFile()       │
                  └──────────┬───────────┘
                             │
                             ▼
                        ┌─────────┐
                        │ Graph   │
                        └────┬────┘
                             │
              ┌──────────────┴──────────────┐
              │                             │
              ▼                             ▼
      ┌────────────────┐        ┌──────────────────────┐
      │ CLI: LSRCompute│        │ GUI: LSRComputeGUI   │
      │ - arg parsing  │        │ - File chooser       │
      │ - mode select  │        │ - Interactive select │
      │ - execution    │        │ - Step control      │
      └────────┬───────┘        └──────────┬───────────┘
               │                            │
               └────────────┬───────────────┘
                            │
                            ▼
                   ┌──────────────────────┐
                   │ DijkstraAlgorithm    │
                   │ Single-Step/All Mode │
                   └──────────┬───────────┘
                              │
                    ┌─────────┴─────────┐
                    │                   │
                    ▼                   ▼
            ┌───────────────┐  ┌─────────────────┐
            │ SS Mode:      │  │ CA Mode:        │
            │ Step-by-step  │  │ Compute all     │
            │ + pause after │  │ Direct result   │
            │ each node     │  │                 │
            └───────────────┘  └─────────────────┘
                    │                   │
                    └─────────┬─────────┘
                              │
                              ▼
                    ┌──────────────────┐
                    │ Results Display  │
                    │ - Paths found    │
                    │ - Costs computed │
                    │ - Summary table  │
                    └──────────────────┘
```

## Sequence Diagram: Single-Step Mode

```
User          LSRCompute      DijkstraAlgo       Graph
  │                │                 │             │
  │─ Enter args──>│                 │             │
  │                └─ parseFile()───────────────>│
  │                │<──── Graph ────────────────│
  │                │                 │             │
  │                ├─ validate source │             │
  │                │                 │             │
  │                └─ new Dijkstra───────────────>│
  │                │<─ Algorithm ────────────────│
  │                │                 │             │
  │─ [MAIN LOOP]──>│                 │             │
  │                │                 │             │
  │                └─ getNextStep()  >│             │
  │                │<─ nodeFound ────│             │
  │                │                 │             │
  │─ Press ENTER──>│ (wait for input) │             │
  │                │                 │             │
  │                └─ getNextStep()  >│             │
  │                │<─ nodeFound ────│             │
  │                │                 │             │
  │  [Repeat MAIN LOOP until complete]           │
  │                │                 │             │
  │                └─ getResults()   >│             │
  │                │<─ paths&costs ──│             │
  │                │                 │             │
  │<─ results─────│                 │             │
```

## Class Relationships Summary

| Class | Purpose | Dependencies |
|-------|---------|---|
| **LSRCompute** | CLI main program | Graph, DijkstraAlgorithm, LSAFileParser |
| **LSRComputeGUI** | GUI application | Graph, DijkstraAlgorithm, LSAFileParser |
| **Graph** | Network topology storage | None |
| **DijkstraAlgorithm** | Shortest path computation | Graph |
| **LSAFileParser** | File I/O utility | Graph |

## Design Patterns Used

1. **Strategy Pattern**: Two execution strategies (SS vs CA modes)
2. **Factory Pattern**: Graph and Algorithm creation
3. **Observer Pattern**: GUI updates based on algorithm progress
4. **MVC Pattern**: GUI separates Model (Graph, Algorithm) from View and Controller

## Package Structure

```
Default Package (no packages used for simplicity)
├── Graph.java
├── DijkstraAlgorithm.java
├── LSAFileParser.java
├── LSRCompute.java
└── LSRComputeGUI.java
```

## Method Call Flow: Complete Example

```
User: java LSRCompute routes.lsa A CA

1. LSRCompute.main()
   ├── parseArguments() → filename="routes.lsa", source="A", mode="CA"
   ├── LSAFileParser.parseFile("routes.lsa") → Graph g
   ├── validateSourceNode(g, "A") → true
   ├── new DijkstraAlgorithm(g, "A") → Dijkstra d
   ├── executeComputeAllMode(d)
   │  └── d.executeComputeAll()
   │     └── d.executeSingleStep()
   │        ├── getMinDistanceUnvisitedNode() → "C" (minimum distance)
   │        ├── mark "C" as visited
   │        ├── relaxEdges("C") → update distances to neighbors
   │        ├── repeat for "D", "B", "E", "F"
   │        └── return visitOrder = [C, D, B, E, F]
   │
   └── System.out.println(d.getResultsAsString())
      ├── d.getResultsAsString() → StringBuilder
      │  ├── for each node: d.getPath(node)
      │  │  └── reconstruct path from previous nodes
      │  └── format "B: Path: A>B Cost: 5"
      └── Output to console
```

## Interface Visualization

### GUI Layout

```
┌─────────────────────────────────────────────────────────────┐
│  File  Edit  View  Help                              [_][□][X]│
├─────────────────────────────────────────────────────────────┤
│ Controls:                                                     │
│ [Load File] Source: [   A   ▼] [Single Step][Next] [Compute] [Reset] │
├──────────────────────────┬──────────────────────────────────┤
│ Topology:                │ Results:                          │
│                          │                                   │
│ A: B:5 C:3 D:5           │ Found C: Path: A>C Cost: 3        │
│ B: A:5 C:4 E:3 F:2       │ Found D: Path: A>C>D Cost: 4      │
│ C: A:3 B:4 D:1 E:6       │ Found B: Path: A>B Cost: 5        │
│ D: A:5 C:1 E:3           │ Found E: Path: A>C>D>E Cost: 7    │
│ E: B:3 C:6 D:3 F:5       │ Found F: Path: A>B>F Cost: 7      │
│ F: B:2 E:5               │                                   │
│                          │ Summary:                          │
│                          │ Source A:                         │
│                          │ B: Path: A>B Cost: 5              │
├──────────────────────────┴──────────────────────────────────┤
│ Dynamic Topology:                                            │
│ Add Node: [H  ][Add] Remove: [D][Remove] Link: [A][B][5][Add]│
│ Break Link: [C-D][Break]                                    │
├──────────────────────────────────────────────────────────────┤
│ Status: Computation complete        Progress: [████████] 100%│
└──────────────────────────────────────────────────────────────┘
```
