#!/bin/bash
# Build script for COMP4322 LSR Project

echo "Compiling LSR Project..."
cd src
javac *.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run the program:"
    echo "  CLI mode (Compute-All):   java -cp . LSRCompute ../data/routes.lsa A CA"
    echo "  CLI mode (Single-Step):   java -cp . LSRCompute ../data/routes.lsa A SS"
    echo "  GUI mode:                 java -cp . LSRComputeGUI"
else
    echo "Compilation failed!"
    exit 1
fi
