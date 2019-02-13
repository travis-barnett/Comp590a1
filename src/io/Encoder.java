package io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Encoder {

	public static void main(String[] args) throws InsufficientBitsLeftException, IOException {
		// TODO Auto-generated method stub

		String input_file_name = "data/uncompressed.txt";
		String output_file_name = "data/recompressed.dat";

		FileInputStream fis = new FileInputStream(input_file_name);
		InputStreamBitSource inputstream = new InputStreamBitSource(fis);

		int[] symbol_counts = new int[256];
		int num_symbols = 0;
		
		// Read in each symbol (i.e. byte) of input file and 
		// update appropriate count value in symbol_counts
		// Should end up with total number of symbols 
		// (i.e., length of file) as num_symbols
		
		while(true){
			try{
				int symbol = inputstream.next(8);
				symbol_counts[symbol]++;
				num_symbols++;
				continue;
			}catch(InsufficientBitsLeftException e){
				System.out.println("end of file");
				System.out.println("Num symbols = "+ num_symbols);
				break;
			}
		}
		
		//calculate entropy of ASCII English text file
		double[] symbol_prob = new double[256];
		for (int i=0; i<symbol_counts.length; i++){
			symbol_prob[i] = ((double)symbol_counts[i]/(double)num_symbols);
			//System.out.println(symbol_prob[i]+ " symbol count = "+ symbol_counts[i]);
		}
		double entropy=0.0;
		for (int i=0; i<symbol_prob.length; i++){
			if (symbol_prob[i]!=0.0)
				entropy = entropy + (symbol_prob[i])*((Math.log(1/symbol_prob[i])/Math.log(2.0)));	
		}
		System.out.println("entropy = "+ entropy);

		// Close input file
		fis.close();

		// Create array of symbol values
		
		int[] symbols = new int[256];
		for (int i=0; i<256; i++) {
			symbols[i] = i;
		}
		
		// Create encoder using symbols and their associated counts from file.
			
		HuffmanEncoder encoder = new HuffmanEncoder(symbols, symbol_counts);
		
		
		// Open output stream.
		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);

		// Write out code lengths for each symbol as 8 bit value to output file.
		for (int i=0; i<256; i++){
			bit_sink.write(encoder.getCode(i).length(), 8);
		}
		
		// Write out total number of symbols as 32 bit value.
		bit_sink.write(num_symbols, 32);

		// Reopen input file.
		fis = new FileInputStream(input_file_name);
		InputStreamBitSource is = new InputStreamBitSource(fis);
		// Go through input file, read each symbol (i.e. byte),
		// look up code using encoder.getCode() and write code
        // out to output file.
		num_symbols=0;
		while(true){
			try{
				int symbol = is.next(8);
				encoder.encode(symbol, bit_sink);
				num_symbols++;
				continue;
			}catch(InsufficientBitsLeftException e){
				System.out.println("end of file");
				System.out.println("Num symbols = "+ num_symbols);
				break;
			}
		}
		

		// Pad output to next word.
		bit_sink.padToWord();

		// Close files.
		fis.close();
		fos.close();
		
	}

}
