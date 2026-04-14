import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

/**
 * LSRComputeGUI - Graphical User Interface for Link State Routing computation
 * Provides interactive visualization of Dijkstra's algorithm execution
 */
public class LSRComputeGUI extends JFrame {
    private Graph graph;
    private DijkstraAlgorithm dijkstra;
    private JTextArea topologyArea;
    private JTextArea resultsArea;
    private JTextArea statusArea;
    private JComboBox<String> sourceNodeCombo;
    private JButton singleStepBtn;
    private JButton nextStepBtn;
    private JButton computeAllBtn;
    private JButton loadFileBtn;
    private JButton resetBtn;
    private JLabel currentStepLabel;
    private JProgressBar progressBar;
    private boolean isExecuting = false;
    private JTextField nodeToAddField;
    private JTextField nodeToRemoveField;
    private JTextField linkFromField;
    private JTextField linkToField;
    private JTextField linkCostField;
    private JTextField linkBreakField;

    public LSRComputeGUI() {
        setTitle("Link State Routing (LSR) Computation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        graph = new Graph();
        initializeGUI();
    }

    private void initializeGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Top: Control Panel
        mainPanel.add(createControlPanel(), BorderLayout.NORTH);
        
        // Center: Content area with 3 panels
        JSplitPane topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        topSplit.setLeftComponent(createTopologyPanel());
        topSplit.setRightComponent(createResultsPanel());
        topSplit.setDividerLocation(400);

        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setTopComponent(topSplit);
        mainSplit.setBottomComponent(createDynamicTopologyPanel());
        mainSplit.setDividerLocation(500);

        mainPanel.add(mainSplit, BorderLayout.CENTER);
        
        // Bottom: Status bar
        mainPanel.add(createStatusBar(), BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));

        loadFileBtn = new JButton("Load File");
        loadFileBtn.addActionListener(e -> loadFile());
        panel.add(loadFileBtn);

        panel.add(new JLabel("Source Node:"));
        sourceNodeCombo = new JComboBox<>();
        sourceNodeCombo.addItem("A");
        panel.add(sourceNodeCombo);

        singleStepBtn = new JButton("Single Step");
        singleStepBtn.addActionListener(e -> startSingleStep());
        singleStepBtn.setEnabled(false);
        panel.add(singleStepBtn);

        nextStepBtn = new JButton("Next Step");
        nextStepBtn.addActionListener(e -> nextStep());
        nextStepBtn.setEnabled(false);
        panel.add(nextStepBtn);

        computeAllBtn = new JButton("Compute All");
        computeAllBtn.addActionListener(e -> computeAll());
        computeAllBtn.setEnabled(false);
        panel.add(computeAllBtn);

        resetBtn = new JButton("Reset");
        resetBtn.addActionListener(e -> reset());
        panel.add(resetBtn);

        return panel;
    }

    private JPanel createTopologyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Network Topology"));

        topologyArea = new JTextArea();
        topologyArea.setEditable(false);
        topologyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(topologyArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Results"));

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEtchedBorder());

        currentStepLabel = new JLabel("Status: Ready");
        panel.add(currentStepLabel);

        panel.add(new JSeparator(SwingConstants.VERTICAL));

        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(200, 20));
        panel.add(progressBar);

        statusArea = new JTextArea(3, 50);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 10));

        return panel;
    }

    private JPanel createDynamicTopologyPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createTitledBorder("Dynamic Topology Management"));

        // Add Node Panel
        JPanel addNodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addNodePanel.add(new JLabel("Add Node:"));
        nodeToAddField = new JTextField(5);
        addNodePanel.add(nodeToAddField);
        JButton addNodeBtn = new JButton("Add");
        addNodeBtn.addActionListener(e -> addNode());
        addNodePanel.add(addNodeBtn);

        // Remove Node Panel
        JPanel removeNodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        removeNodePanel.add(new JLabel("Remove Node:"));
        nodeToRemoveField = new JTextField(5);
        removeNodePanel.add(nodeToRemoveField);
        JButton removeNodeBtn = new JButton("Remove");
        removeNodeBtn.addActionListener(e -> removeNode());
        removeNodePanel.add(removeNodeBtn);

        // Manage Links Panel
        JPanel linksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linksPanel.add(new JLabel("Add Link:"));
        linkFromField = new JTextField(3);
        linksPanel.add(linkFromField);
        linksPanel.add(new JLabel("to"));
        linkToField = new JTextField(3);
        linksPanel.add(linkToField);
        linksPanel.add(new JLabel("Cost:"));
        linkCostField = new JTextField(3);
        linksPanel.add(linkCostField);
        JButton addLinkBtn = new JButton("Add");
        addLinkBtn.addActionListener(e -> addLink());
        linksPanel.add(addLinkBtn);

        linksPanel.add(new JSeparator(SwingConstants.VERTICAL));
        linksPanel.add(new JLabel("Break Link:"));
        linkBreakField = new JTextField(5);
        linksPanel.add(linkBreakField);
        JButton breakLinkBtn = new JButton("Break");
        breakLinkBtn.addActionListener(e -> breakLink());
        linksPanel.add(breakLinkBtn);

        panel.add(addNodePanel);
        panel.add(removeNodePanel);
        panel.add(linksPanel);

        return panel;
    }

    private void loadFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                graph = LSAFileParser.parseFile(chooser.getSelectedFile().getAbsolutePath());
                updateTopologyDisplay();
                updateSourceNodeCombo();
                singleStepBtn.setEnabled(true);
                computeAllBtn.setEnabled(true);
                currentStepLabel.setText("Status: File loaded successfully");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
            }
        }
    }

    private void updateTopologyDisplay() {
        topologyArea.setText(graph.toString());
    }

    private void updateSourceNodeCombo() {
        sourceNodeCombo.removeAllItems();
        for (String node : new TreeSet<>(graph.getNodes())) {
            sourceNodeCombo.addItem(node);
        }
    }

    private void startSingleStep() {
        if (sourceNodeCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please load a file first");
            return;
        }

        String source = (String) sourceNodeCombo.getSelectedItem();
        dijkstra = new DijkstraAlgorithm(graph, source);
        isExecuting = true;

        singleStepBtn.setEnabled(false);
        computeAllBtn.setEnabled(false);
        nextStepBtn.setEnabled(true);
        loadFileBtn.setEnabled(false);
        sourceNodeCombo.setEnabled(false);

        resultsArea.setText("Starting single-step execution from node " + source + "...\n");
        currentStepLabel.setText("Status: Single-Step mode - Click 'Next Step' to continue");
        progressBar.setValue(0);
        progressBar.setMaximum(graph.getNodeCount());
    }

    private void nextStep() {
        if (dijkstra == null || !isExecuting) {
            return;
        }

        String nextNode = dijkstra.getNextStep();
        if (nextNode == null) {
            isExecuting = false;
            nextStepBtn.setEnabled(false);
            singleStepBtn.setEnabled(true);
            computeAllBtn.setEnabled(true);
            loadFileBtn.setEnabled(true);
            sourceNodeCombo.setEnabled(true);

            // Show summary
            resultsArea.append("\n\n=== COMPUTATION COMPLETE ===\n");
            resultsArea.append(dijkstra.getResultsAsString());
            currentStepLabel.setText("Status: Single-Step complete");
            progressBar.setValue(progressBar.getMaximum());
        } else {
            String path = dijkstra.getPath(nextNode);
            int cost = dijkstra.getDistance(nextNode);
            resultsArea.append("Found " + nextNode + ": Path: " + path + " Cost: " + cost + "\n");
            
            int visited = dijkstra.getVisitOrder().size();
            progressBar.setValue(visited);
            currentStepLabel.setText("Status: Processing node " + nextNode + 
                                   " (" + visited + "/" + graph.getNodeCount() + ")");
        }
    }

    private void computeAll() {
        if (sourceNodeCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please load a file first");
            return;
        }

        String source = (String) sourceNodeCombo.getSelectedItem();
        dijkstra = new DijkstraAlgorithm(graph, source);
        dijkstra.executeComputeAll();

        resultsArea.setText(dijkstra.getResultsAsString());
        progressBar.setValue(progressBar.getMaximum());
        currentStepLabel.setText("Status: Computation complete");
        isExecuting = false;
    }

    private void reset() {
        dijkstra = null;
        isExecuting = false;
        resultsArea.setText("");
        singleStepBtn.setEnabled(graph.getNodeCount() > 0);
        computeAllBtn.setEnabled(graph.getNodeCount() > 0);
        nextStepBtn.setEnabled(false);
        loadFileBtn.setEnabled(true);
        sourceNodeCombo.setEnabled(true);
        progressBar.setValue(0);
        currentStepLabel.setText("Status: Reset");
    }

    private void addNode() {
        String node = nodeToAddField.getText().trim().toUpperCase();
        if (node.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a node name");
            return;
        }
        graph.addNode(node);
        updateTopologyDisplay();
        updateSourceNodeCombo();
        nodeToAddField.setText("");
        currentStepLabel.setText("Status: Node " + node + " added");
    }

    private void removeNode() {
        String node = nodeToRemoveField.getText().trim().toUpperCase();
        if (node.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a node name");
            return;
        }
        if (!graph.containsNode(node)) {
            JOptionPane.showMessageDialog(this, "Node not found");
            return;
        }
        graph.removeNode(node);
        updateTopologyDisplay();
        updateSourceNodeCombo();
        reset();
        nodeToRemoveField.setText("");
        currentStepLabel.setText("Status: Node " + node + " removed");
    }

    private void addLink() {
        String from = linkFromField.getText().trim().toUpperCase();
        String to = linkToField.getText().trim().toUpperCase();
        String costStr = linkCostField.getText().trim();

        if (from.isEmpty() || to.isEmpty() || costStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        try {
            int cost = Integer.parseInt(costStr);
            graph.addEdge(from, to, cost);
            updateTopologyDisplay();
            updateSourceNodeCombo();
            reset();
            linkFromField.setText("");
            linkToField.setText("");
            linkCostField.setText("");
            currentStepLabel.setText("Status: Link " + from + "-" + to + " added");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid cost value");
        }
    }

    private void breakLink() {
        String linkStr = linkBreakField.getText().trim().toUpperCase();
        if (linkStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter link (e.g., A-B)");
            return;
        }

        String[] parts = linkStr.split("-");
        if (parts.length != 2) {
            JOptionPane.showMessageDialog(this, "Invalid format. Use: A-B");
            return;
        }

        String from = parts[0].trim();
        String to = parts[1].trim();

        graph.removeEdge(from, to);
        updateTopologyDisplay();
        reset();
        linkBreakField.setText("");
        currentStepLabel.setText("Status: Link " + from + "-" + to + " removed");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LSRComputeGUI());
    }
}
