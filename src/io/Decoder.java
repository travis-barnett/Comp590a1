package io;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Decoder  {
	

	public static void main (String[] args) throws InsufficientBitsLeftException, IOException{
		String input_file_name = "data/recompressed.dat";
		String output_file_name = "/Users/Travis/Documents/College/Freshman Year/COMP 401/Eclipse/HuffmanTree/data/reuncompressed.txt";
		
		
		InputStream fis = new FileInputStream("/Users/Travis/Documents/College/Freshman Year/COMP 401/Eclipse/HuffmanTree/data/recompressed.dat");
		InputStreamBitSource inputstream = new InputStreamBitSource(fis);
		
		int[] code_length_array = new int[256];
		List<HuffmanNode> list = new ArrayList<HuffmanNode>();
		for (int i=0; i<256; i++){
			int codeLength = inputstream.next(8);
			list.add(new HuffmanNode(codeLength, i));
			code_length_array[i] = codeLength;
		}
		
		list.sort(Comparator.comparing(HuffmanNode::getCodeLength).thenComparing(Comparator.comparing(HuffmanNode::getChar)));
		
		
		HuffmanTree tree = new HuffmanTree(list);

		
		int num_symbols = inputstream.next(32);
		System.out.println(num_symbols);
		
		try {

			// Open up output file.
			
			FileOutputStream fos = new FileOutputStream(output_file_name);
			OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);

			int[] symbol_count = new int[256];
			for (int i=0; i<num_symbols; i++) {
				int symbol = tree.decode(inputstream);
				symbol_count[symbol]++;
				fos.write(symbol);
				//System.out.println(symbol);
			}
			
			//calculate entropy
			double entropy = 0;
			for (int i=0; i<symbol_count.length; i++){
				entropy = entropy + ((double)symbol_count[i]*(double)code_length_array[i]);
			}
			entropy = entropy/num_symbols;
			System.out.println(entropy);

			// Flush output and close files.
			
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
