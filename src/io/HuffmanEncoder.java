package io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import io.OutputStreamBitSink;

public class HuffmanEncoder {
		
	private Map<Integer, String> _code_map;
	
	public HuffmanEncoder(int[] symbols, int[] symbol_counts) {
		assert symbols.length == symbol_counts.length;
		
		// Given symbols and their associated counts, create initial
		// Huffman tree. Use that tree to get code lengths associated
		// with each symbol. Create canonical tree using code lengths.
		// Use canonical tree to form codes as strings of 0 and 1
		// characters that are inserted into _code_map.

		// Start with an empty list of nodes
		
		List<HuffmanNode> node_list = new ArrayList<HuffmanNode>();
		
		// Create a leaf node for each symbol, encapsulating the
		// frequency count information into each leaf.
		for (int i=0; i<symbols.length; i++){
			node_list.add(new HuffmanNode(symbol_counts[i], symbols[i]));
		}

		// Sort the leaf nodes
		node_list.sort(Comparator.comparing(HuffmanNode::getCodeLength).thenComparing(Comparator.comparing(HuffmanNode::height)));

		
		PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
	
	        //create queue based on frequencies in source file
	        for (int i=0; i<node_list.size(); i++){
	            q.add(node_list.get(i));
	        }
	        
	        //create a root node
	        HuffmanNode root = null;
	        
	        while (q.size() > 1){
	            //remove first two nodes in queue
	            HuffmanNode x = q.peek();
	            q.poll();
	            HuffmanNode y = q.peek();
	            q.poll();
	
	            //create new node that is sum of their frequencies
	            HuffmanNode z = new HuffmanNode(x.codeLength + y.codeLength, -1);
	        
	            //add first extracted node as left child, second as right child
	            z.left = x;
	            z.right = y;
	
	            //mark as root and add back to queue
	            root = z;
	            q.add(z);
	        }
	        
	        // Create a temporary empty mapping between symbol values and their code strings
			//Map<Integer, String> cmap = new HashMap<Integer, String>();

			// Start at root and walk down to each leaf, forming code string along the
			// way (0 means left, 1 means right). Insert mapping between symbol value and
			// code string into cmap when each leaf is reached.
			
	        System.out.println("root height = "+ root.height());
	        
	        StringBuilder str = new StringBuilder();
	        Map<Integer, String> cmap = new HashMap<Integer, String>();
	        root.getCodeString(str, cmap);
	        
	        //System.out.print(cmap);
	        
		
		// Create empty list of SymbolWithCodeLength objects
		List<HuffmanNode> list = new ArrayList<HuffmanNode>();

		// For each symbol value, find code string in cmap and create new SymbolWithCodeLength
		// object as appropriate (i.e., using the length of the code string you found in cmap).
		
		for(Integer key : cmap.keySet()){
			int value = key;
			String code_string = cmap.get(key);
			int code_length = code_string.length();
			System.out.println("key: "+ value+ " code_length: " +code_length);
			HuffmanNode sym = new HuffmanNode(code_length, value);
			list.add(sym);
		}
		
		// Sort sym_with_lenght
		list.sort(null);
		
		
		HuffmanTree tree = new HuffmanTree(list);
		// Now construct the canonical tree as you did in HuffmanDecodeTree constructor
		
		
		// Create code map that encoder will use for encoding
		
		_code_map = new HashMap<Integer, String>();
		StringBuilder string = new StringBuilder();
		_code_map = tree.getRoot().getCodeString(string, _code_map);
		
		// Walk down canonical tree forming code strings as you did before and
		// insert into map.		
	}
	

	public String getCode(int symbol) {
		return _code_map.get(symbol);
	}

	public void encode(int symbol, OutputStreamBitSink bit_sink) throws IOException {
		bit_sink.write(_code_map.get(symbol));
	}
	
	
	
	

}
