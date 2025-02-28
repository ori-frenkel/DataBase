package Join;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ExternalMemoryImpl extends IExternalMemory {

	private int line_size_in_bytes(String file){
		int size = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			size = br.readLine().length() * 2; // A character in Java occupies 2 bytes.
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

	private String getLine(int blockNumber, int lineNumber, int lineInFullBlock ,String allLinesPath){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(allLinesPath), 1024);
			String line = null;
			int currentLineNumber = 0;
			int wantedLine = blockNumber*lineInFullBlock + lineNumber;
			while((line = br.readLine()) != null){
				currentLineNumber++;
				if(currentLineNumber == wantedLine)
				{
					return line;
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return "a";
	}

	@Override
	public void sort(String in, String out, String tmpPath) {

		// TODO: Implement
		// STEP1 - reading line to buffer less than 50mb (buffer size) sorting them and write to disk
		// the number of line that would be ~50 mb
		FileInputStream inputStream = null;

		int lineInBlocks = 2; // the number of lines in buffer/block (at this stage same)
		int buffer_size = lineInBlocks * line_size_in_bytes(in); // the buffer it bytes
		File tmp_sorted_blocks = null;
		try {
			tmp_sorted_blocks = File.createTempFile("TempFileMaine", ".txt", new File(tmpPath));
			tmp_sorted_blocks.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int totalNumberOfLines = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(in), buffer_size);
			String line;
			ArrayList<String> line_in_buffer = new ArrayList<String>();
			assert tmp_sorted_blocks != null;
			BufferedWriter bw = new BufferedWriter(new FileWriter(tmp_sorted_blocks, true));
			while((line = br.readLine()) != null){
				line_in_buffer.add(line);
				totalNumberOfLines++;
				if (totalNumberOfLines % lineInBlocks == 0){ // if got to end of block
					// sorting and writing to tmp file.
					Collections.sort(line_in_buffer); // sorting the lines
					// writing then to tmp file.
					for(String _line : line_in_buffer){
						bw.write(_line + "\n"); // TODO : remember the last line in file is empty (and shouldn't
						// TODO  be in the output file empty line at the end)
						System.out.println("Writing : \n" +  _line);
					}
					line_in_buffer.clear();
				}
			}
			// in case the last buffer contain less than full buffer length
			if (line_in_buffer.size() > 0) {
				Collections.sort(line_in_buffer); // sorting the lines
				// writing then to tmp file.
				for(String _line : line_in_buffer){
					bw.write(_line + "\n"); // TODO : remember the last line in file is empty (and shouldn't
					// TODO  be in the output file empty line at the end)
					System.out.println("Writing : \n" +  _line);
				}
				line_in_buffer.clear();
			}
			bw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int max_size_of_line = 52 * 2 + 1;
		// STEP 2 :
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(out, true));
			int numberOfBlocks = (int) Math.ceil((double)totalNumberOfLines / lineInBlocks);
			System.out.println("Number of blocks : " + numberOfBlocks);
//			ArrayList<String> line_in_buffer = new ArrayList<String>();
			BufferedReader[] blocksArr = new BufferedReader[numberOfBlocks];
			int[] currentLineInBlock = new int[numberOfBlocks];
			ArrayList<String> mainMemory = new ArrayList<String>();
			for(int i = 0; i < numberOfBlocks; i++)
			{
				assert tmp_sorted_blocks != null;
				// buffer contain only the first line of block
				blocksArr[i] = new BufferedReader(new FileReader(tmp_sorted_blocks), max_size_of_line);
				// making the buffer to point to the start of the  block
				for(int j = 0; j < i * lineInBlocks; j++)
				{
					blocksArr[i].readLine();
				}
				currentLineInBlock[i] = 1;
				mainMemory.add(blocksArr[i].readLine());


			}
			// Block number where the min string is
			int minIndex = mainMemory.indexOf(Collections.min(mainMemory));
			bw.write(mainMemory.get(minIndex)); // writing the min line in main memory to output
			mainMemory.set(minIndex, blocksArr[minIndex].readLine()); // moving the written line in memory
																	  // to the next line
			currentLineInBlock[minIndex]++;


		}
		catch (IOException e) {
			e.printStackTrace();
		}







//		int numberOfBlocks = (int) Math.ceil((double)totalNumberOfLines / lineInBuffer);
//		System.out.println("Number of blocks : " + numberOfBlocks);
//		int[] blocksArray = new int[numberOfBlocks]; // array that contain the line in each block we are currently
//		Arrays.fill(blocksArray, 1); // all blocks start from line 2
//		int numberOfBlocksFinishedToSort = 0;
//		ArrayList<String> line_in_buffer = new ArrayList<String>();
//		for(int blockNum = 0; blockNum < numberOfBlocks; blockNum++)
//		{
//			assert tmp_sorted_blocks != null;
//			line_in_buffer.add(getLine(blockNum, 1, lineInBuffer, tmp_sorted_blocks.getAbsolutePath()));
//			System.out.println( " Added to buff " + line_in_buffer.get(line_in_buffer.size() - 1));
//		}
//		while(numberOfBlocksFinishedToSort != totalNumberOfLines)
//		{
//			for(int i = 0; i < numberOfBlocks; i++)
//			{
//
//			}
//		}

	}

	@Override
	protected void join(String in1, String in2, String out, String tmpPath) {

		// TODO Auto-generated method stub

	}

	@Override
	protected void select(String in, String out, String substrSelect, String tmpPath) {

		// TODO Auto-generated method stub

	}

	@Override
	public void joinAndSelectEfficiently(String in1, String in2, String out,
										 String substrSelect, String tmpPath) {

		// TODO Auto-generated method stub

	}

}

class Line{
	private String idx;
	private String seq1, seq2;
	Line(String idx, String seq1, String seq2)
	{
		this.idx = idx;
		this.seq1 = seq1;
		this.seq2 = seq2;
	}

	public String getIdx(){
		return this.idx;
	}

	public String getSeq1() {
		return seq1;
	}

	public String getSeq2() {
		return seq2;
	}
}