package io;

import java.util.PriorityQueue;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HuffmanNode implements Comparable<HuffmanNode>{
	int codeLength;
    int c;
    HuffmanNode left;
    HuffmanNode right;
	
	public HuffmanNode(int codeLength, int c){
		this.codeLength = codeLength;
		this.c = c;
	}
	
	public int getCodeLength(){
		return codeLength;
	}
	public int getChar(){
		return c;
	}
	
	public boolean insert(HuffmanNode node){
		//check left side and then right side
		//create internal node
		//call insert recursivley --> left.insert(symbol, length-1)
		int length = node.getCodeLength();
		
		if (length == 1){
			if (this.left==null){
				this.left = node;
				return true;
			}else{
				this.right = node;
				return true;
			}
		}
		
		if (this.left==null){
			HuffmanNode internal = new HuffmanNode(0,-1);
			this.left = internal;
			node.codeLength--;
			return internal.insert(node);
		}else if (!this.left.isFull()){
			node.codeLength--;
			return this.left.insert(node);
		}
		if (this.right==null){
			HuffmanNode internal = new HuffmanNode(0,-1);
			this.right = internal;
			node.codeLength--;
			return this.right.insert(node);
		}else if (!this.right.isFull()){
			node.codeLength--;
			return this.right.insert(node);
		}
		return false;
		
	}
	
	public boolean isFull(){
		if (this.isLeaf()){
			return true;
		}
		if (this.left==null || this.right==null){
			return false;
		}else {
			return this.left.isFull() && this.right.isFull();
		}
	}
	
	public boolean isLeaf(){
		if (this.getChar()==-1){
			return false;
		}else{
			return true;
		}
	}
	
	public int height(){
		int left=0;
		int right=0;
		if (this==null){
			return -1;
		}
		
		left = (this.left==null)? -1: this.left.height();
		right = (this.right==null)? -1: this.right.height();
		
		return Math.max(left, right) +1;
	}
	
	public Map<Integer, String> getCodeString(StringBuilder str, Map<Integer, String> cmap){	
		
		if (this.isLeaf()){
			cmap.put(this.c, str.toString());
		}else{
			StringBuilder strcopy = new StringBuilder();
			strcopy.append(str);
			this.left.getCodeString(str.append(0), cmap);
			this.right.getCodeString(strcopy.append(1), cmap);
		}
		
		return cmap;
	}
	
	public int compareTo(HuffmanNode other) {
		if (getCodeLength() != other.getCodeLength()) {
			return getCodeLength() - other.getCodeLength();
		} else {
			return height() - other.height();
		}
	}

}
class HuffmanTree {
	
	private HuffmanNode root;
	
	public HuffmanTree(List<HuffmanNode> list){
		
		//create root node
		root = new HuffmanNode(0,-1);
		
		for (int i=0; i<list.size(); i++){
			root.insert(list.get(i));
		}
		
		root.isFull();
		System.out.println("Tree is full..." + root.isFull());
		
	}
	
	public HuffmanNode getRoot(){
		return root;
	}
	
	public int decode(InputStreamBitSource bit_source) throws IOException, InsufficientBitsLeftException {
			
			// Start at the root
			HuffmanNode n = root;
			
			while (!n.isLeaf()) {
				int bit = bit_source.next(1);
				//System.out.print(bit);
				if (bit==0){
					n = n.left;
				}else{
					n = n.right;
				}
			}
			
			// Return symbol at leaf
			int symbol = n.getChar();
			//System.out.println((char)symbol);
			n = root;
			return symbol;
		}
}








//class HuffmanTree {
//
//    public HuffmanTree(int[] array){
//        PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
//    
//    
//
//        //create queue based on frequencies in source file
//        for (int i=0; i<256; i++){
//            HuffmanNode hn = new HuffmanNode();
//
//            hn.c = (char)i;
//            hn.freq = array[i];
//
//            hn.left = null;
//            hn.right = null;
//
//            q.add(hn);
//        }
//        
//        //create a root node
//        HuffmanNode root = null;
//
//        while (q.size() > 1){
//            //remove first two nodes in queue
//            HuffmanNode x = q.peek();
//            q.poll();
//            HuffmanNode y = q.peek();
//            q.poll();
//
//            //create new node that is sum of their frequencies
//            HuffmanNode z = new HuffmanNode();
//            z.freq = x.freq + y.freq;
//            z.c = '-';
//
//            //add first extracted node as left child, second as right child
//            z.left = x;
//            z.right = y;
//
//            //mark as root and add back to queue
//            root = z;
//            q.add(z);
//        }
//    }
//
//}