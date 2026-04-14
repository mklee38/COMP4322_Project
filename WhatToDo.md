Here's a clear, step-by-step action plan for your **COMP4322 Term Project** (Link State Routing). The deadline is **April 20, 2026, 23:00**, so you have about 6 days left — start immediately and prioritize the **mandatory** parts first.

### 1. Understand What You Need to Deliver (Mandatory)
Your Java program must:
- Read an `.lsa` file (e.g., `routes.lsa`) that describes the network topology.
  - Example format (undirected graph, symmetric costs):
    ```
    A: B:5 C:3 D:5
    B: A:5 C:4 E:3 F:2
    C: A:3 B:4 D:1 E:6
    D: A:5 C:1 E:3
    E: B:3 C:6 D:3 F:5
    F: B:2 E:5
    ```
- Build the graph from this file (nodes + weighted edges).
- Run **Dijkstra’s algorithm** from a given source node (e.g., "A").
- Support two modes:
  - **SS (Single-Step)**: Show each step of Dijkstra (e.g., "Found F: Path: A>B>F Cost: 7"), pause for user input, then continue until all nodes are visited. At the end, show a summary table of all shortest paths.
  - **CA (Compute-All)**: Run Dijkstra fully and directly output only the summary table.
- Command-line support (at minimum):
  ```
  java LSRCompute routes.lsa A          // defaults to CA?
  java LSRCompute routes.lsa A SS
  java LSRCompute routes.lsa A CA
  ```

**Bonus for higher marks**:
- Nice GUI (highly recommended — it gives "user-friendly interfaces" points in marking).
- Optional dynamic topology (add/remove nodes/links via the program itself).

You also need:
- A **project report** (summary of LSR algorithm + your design/implementation + roles + UML if possible).
- Source code package.
- 10-minute demonstration (compile + run smoothly, show features, handle Q&A).

### 2. Immediate Next Steps (Do Today/Tomorrow)
1. **Form your group** (1–3 people) if not done. Decide roles (e.g., one on parsing + graph, one on Dijkstra + steps, one on GUI/report).

2. **Parse the LSA file**:
   - Read the file line by line.
   - Use a `Map<String, Map<String, Integer>>` (adjacency list) or a custom `Graph` class.
   - Nodes are strings (A, B, C...); edges are bidirectional with the given cost.

3. **Implement Dijkstra’s algorithm** with path reconstruction:
   - You need distances + previous node (to rebuild paths like "A>C>D>E").
   - For **single-step mode**, you must expose each iteration:
     - Find the unvisited node with smallest tentative distance.
     - "Relax" its neighbors (update distances if better path found).
     - Print the "Found X: Path: ... Cost: ..." message.
     - Pause (e.g., `Scanner.nextLine()` or GUI button).

   A good way is to modify the standard Dijkstra loop to yield one node at a time in SS mode.

4. **Handle input/output**:
   - Parse command-line args properly.
   - For SS: interactive pauses.
   - For CA: just the final table.

### 3. Recommended Implementation Structure (Java)
- `Graph.java` — stores nodes, adjacency list, methods to add edges, get neighbors.
- `LSRCompute.java` (main class) — reads file, builds graph, runs Dijkstra in SS or CA mode.
- Optional: `Dijkstra.java` — separate class for the algorithm (with step-by-step support).
- For GUI (strongly advised): Use **Swing** (JFrame, JTextArea for output, JButton for "Next Step", JTable for summary). Or JavaFX if you're comfortable.

You can model nodes as Strings (easier for this project).

### 4. Quick Tips & Resources
- **Dijkstra with paths**: Keep a `prev` map (String to String) to reconstruct the path by backtracking from destination to source.
- **Single-step visualization**: In the loop, after selecting the next node `u`, print the message with the current best path to `u`, then wait.
- There are existing GitHub repos for similar COMP4322 projects (search "Link-State-Routing COMP4322" or "zgtcktom/Link-State-Routing"). **Study them for ideas/structure, but do NOT copy code** — understand and rewrite in your own style (plagiarism detection is common).
- Standard Java Dijkstra examples (GeeksforGeeks, Baeldung) are excellent starting points — adapt them for strings + step-by-step + path printing.

### 5. Timeline Suggestion (6 Days Left)
- **Day 1–2**: File parsing + Graph class + basic Dijkstra (CA mode working).
- **Day 3**: Add single-step mode + path reconstruction + exact output format.
- **Day 4**: Command-line interface + testing with the sample topology (and your own test cases).
- **Day 5**: GUI (if doing) + polish + bug fixing.
- **Day 6**: Write report (LSR summary + design + roles), prepare demo slides, test everything.

### 6. Report & Demo Tips (Big Marks)
- **LSR summary (10%)**: Explain flooding + LSDB + Dijkstra. Use the figure from the project description. Add a simple diagram.
- **Design & Implementation (20%)**: Architecture (classes diagram), why you chose certain data structures, screenshots of GUI/output, test cases.
- **Code quality (20%)**: Clean naming, comments, no hardcoding.
- **Demo (50%)**: Make it smooth — show both SS and CA, explain steps, show GUI if you have it, mention any extra features.

### 7. Start Coding Now
Create a new Java project in IntelliJ/Eclipse/NetBeans.

First, write a simple file reader that prints the parsed graph.

Then implement Dijkstra for CA mode.

Test with the exact example in the project description (nodes A–F).

If you're stuck on any specific part (e.g., parsing, single-step logic, GUI), reply with details and I can give code snippets or pseudocode.

You’ve got this — focus on getting the core (parse → Dijkstra → correct output in both modes) working first. That alone should get you a solid base mark.

Let me know what part you want to tackle first or if you need help with a specific class! Good luck! 🚀