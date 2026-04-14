#!/bin/bash
# Test script for LSR implementation

cd src

echo "=========================================="
echo "Compiling LSR Project..."
echo "=========================================="
javac *.java

if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo ""
echo "=========================================="
echo "Test 1: Example network (6 nodes) from project spec"
echo "=========================================="
echo "Source A, Compute-All:"
java -cp . LSRCompute ../data/routes.lsa A CA

echo ""
echo "Source E, Compute-All:"
java -cp . LSRCompute ../data/routes.lsa E CA

echo ""
echo "=========================================="
echo "Test 2: Simple 3-node network"
echo "=========================================="
echo "Source A, Compute-All:"
java -cp . LSRCompute ../data/test_simple.lsa A CA

echo ""
echo "=========================================="
echo "Test 3: Complex 8-node network"
echo "=========================================="
echo "Source H, Compute-All:"
java -cp . LSRCompute ../data/test_complex.lsa H CA

echo ""
echo "=========================================="
echo "Test 4: Error handling - Invalid source node"
echo "=========================================="
java -cp . LSRCompute ../data/routes.lsa Z CA

echo ""
echo "=========================================="
echo "All tests completed!"
echo "=========================================="
