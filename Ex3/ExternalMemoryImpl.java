package Join;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ExternalMemoryImpl extends IExternalMemory {

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

	private String minFinder(ArrayList<String> currArr)
	{
		return Collections.min(currArr, (s1, s2) -> {
			if(s1 != null && s2 != null)
			{
				return s1.compareTo(s2);
			}
			else if(s1 != null)
			{
				// case where s1 != null and s2 = null
				return -1;
			}
			else
			{
				return 1;
			}
		});
	}

	@Override
	public void sort(String in, String out, String tmpPath) {



		// TODO: Implement Part A
		// STEP1 - reading line to buffer less than 50mb (buffer size) sorting them and write to disk
		// the number of line that would be ~50 mb
		ArrayList<BufferedReader> blocks = new ArrayList<>();
		final int sizeOfOneLine = 52;
		int lineInBuffer = 2; // the number of lines in buffer/block (at this stage same)
		int buffer_size = lineInBuffer * sizeOfOneLine; // the buffer it bytes
		int fullBlock = 4096; // 4kb
		int lineInOneBlock = fullBlock / sizeOfOneLine;
		// there are 78 lines in full block of 4kb  (4kb / 52)
		ArrayList<String> oneBlockOutPut = new ArrayList<>(lineInOneBlock);
		int totalNumberOfLines = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(in), buffer_size);
			String line;
			ArrayList<String> line_in_buffer = new ArrayList<String>();
			while((line = br.readLine()) != null){
				line_in_buffer.add(line);
				totalNumberOfLines++;
				if (totalNumberOfLines % lineInBuffer == 0){ // if got to end of block
					// sorting and writing to tmp file.
					Collections.sort(line_in_buffer); // sorting the lines
					// writing then to tmp file.
					File tmp_sorted_blocks = File.createTempFile("TempFileMaineA", ".txt", new File(tmpPath));
					tmp_sorted_blocks.deleteOnExit();
					BufferedWriter bw = new BufferedWriter(new FileWriter(tmp_sorted_blocks, true));
					for(String _line : line_in_buffer){
						bw.write(_line + "\n");
						System.out.println("Writing : \n" +  _line);
					}
					bw.close();
					blocks.add(new BufferedReader(new FileReader(tmp_sorted_blocks), fullBlock));
					line_in_buffer.clear();
				}
			}

			// in case the last buffer contain less than full buffer length
			if (line_in_buffer.size() > 0)
			{
				File tmp_sorted_blocks = File.createTempFile("TempFileMaine", ".txt", new File(tmpPath));
				tmp_sorted_blocks.deleteOnExit();
				BufferedWriter bw = new BufferedWriter(new FileWriter(tmp_sorted_blocks, true));
				Collections.sort(line_in_buffer); // sorting the lines
				// writing then to tmp file.

				for(String _line : line_in_buffer){
					bw.write(_line + "\n");
					System.out.println("Writing : \n" +  _line);
				}
				line_in_buffer.clear();
				bw.close();
				blocks.add(new BufferedReader(new FileReader(tmp_sorted_blocks), fullBlock));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final int max_size_of_line = 52 * 2 + 1;
		// STEP 2 :

		try
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(out, true));
			int numberOfBuffer = blocks.size();
			int finishedBuffer = 0;
			ArrayList<String> currArr = new ArrayList<String>(numberOfBuffer);
			for(int i = 0; i < numberOfBuffer; i++)
			{
				currArr.add(blocks.get(i).readLine());
			}
			int minIndex = currArr.indexOf(minFinder(currArr));
			oneBlockOutPut.add(currArr.get(minIndex));
			currArr.set(minIndex, blocks.get(minIndex).readLine());
			if(currArr.get(minIndex) == null)
			{
				finishedBuffer++;
			}
			while(numberOfBuffer != finishedBuffer)
			{
				minIndex = currArr.indexOf(minFinder(currArr));
				oneBlockOutPut.add(currArr.get(minIndex));
				// if output block is full, flush it to disk and clear it.
				if(oneBlockOutPut.size() == lineInOneBlock)
				{
					for(int j = 0; j < lineInBuffer; ++j)
					{
						bw.write(oneBlockOutPut.get(j) + "\n");
						System.out.println("Wrote : "+ oneBlockOutPut.get(j));
					}
					oneBlockOutPut.clear();
				}
				currArr.set(minIndex, blocks.get(minIndex).readLine());
				if(currArr.get(minIndex) == null)
				{
					++finishedBuffer;
				}

			}
			// flushing out in not full block been written
			for(int j = 0; j < oneBlockOutPut.size(); ++j)
			{
				bw.write(oneBlockOutPut.get(j) + "\n");
				System.out.println("Wrote : "+ oneBlockOutPut.get(j));
			}
			// freeing up everything.
			bw.close();
			for(int i = 0; i < numberOfBuffer; i++)
			{
				blocks.get(i).close();
			}
			blocks.clear();
			currArr.clear();
			oneBlockOutPut.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	@Override
	protected void join(String in1, String in2, String out, String tmpPath)
	{
		// TODO implement Part B
		try
		{
			final int blockSize = 4096; // 4kb
			final int sizeOfOneLine = 52;
			final int lineInOneBlock = blockSize / sizeOfOneLine;
			ArrayList<String> outPutBlock = new ArrayList<>(lineInOneBlock);
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));
			BufferedReader tr = new BufferedReader(new FileReader(in1), blockSize);
			BufferedReader ts = new BufferedReader(new FileReader(in2), blockSize);
			BufferedReader gs = new BufferedReader(new FileReader(in2), blockSize);
			String lineTr, lineGs, lineTs = "";
			lineTr = tr.readLine();
			lineGs = gs.readLine();
			while(lineTr != null && lineGs != null)
			{
				// while tr != EOF and lineTr.idx < lineGs.idx
				while (lineTr != null && (lineTr.split("\\s")[0].compareTo(lineGs.split("\\s")[0]) < 0))
				{
					lineTr = tr.readLine();
				}
				// while gs != EOF and lineTr.idx > lineGs.idx
				while (lineTr != null && (lineTr.split("\\s")[0].compareTo(lineGs.split("\\s")[0]) > 0))
				{
					lineGs = gs.readLine();
				}
				while(!lineTs.equals(lineGs))
				{
					lineTs = ts.readLine();
				}

				while(!lineTs.equals(lineGs))
				{
					lineTs = ts.readLine();
				}
				ts.mark(1);
				String curr = lineTs;
				while(lineTr != null && lineTr.split("\\s")[0].equals(lineGs.split("\\s")[0]))
				{ //1
					//TS = GS
					lineTs = curr;
					ts.reset();
					// while ts != eof and lineTs.idx == lineTr.idx
					while(lineTs != null && lineTs.split("\\s")[0].compareTo(lineGs.split("\\s")[0]) == 0)
					{
						// join the two lines
						outPutBlock.add(lineTr + lineTs.substring(lineTs.indexOf(" ")));
						lineTs = ts.readLine();

						// if output block is full, flush it to disk and clear it.
						if(outPutBlock.size() == lineInOneBlock)
						{
							for(int j = 0; j < lineInOneBlock; ++j)
							{
								bw.write(outPutBlock.get(j) + "\n");
								System.out.println("New Wrote New: "+ outPutBlock.get(j));
							}
							outPutBlock.clear();
						}
					}
					lineTr = tr.readLine();
				} //1
				// GS = TS
				while(!lineGs.equals(lineTs))
				{
					lineGs = gs.readLine();
					if(lineGs == null)
					{
						break;
					}
				}
			}

			if(outPutBlock.size() != 0)
			{
				for(int j = 0; j < outPutBlock.size(); j++)
				{
					bw.write(outPutBlock.get(j) + "\n");
					System.out.println("New Wrote New: "+ outPutBlock.get(j));
				}
				outPutBlock.clear();
			}
			outPutBlock.clear();
			bw.close();
			tr.close();
			ts.close();
			gs.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}



	@Override
	protected void select(String in, String out, String substrSelect, String tmpPath) {
		// TODO : implement part C
		try
		{
			final int blockSize = 4096; // 4kb
			final int sizeOfOneLine = 52;
			final int lineInOneBlock = blockSize / sizeOfOneLine;
			ArrayList<String> outPutBlock = new ArrayList<>(lineInOneBlock);
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));
			BufferedReader br = new BufferedReader(new FileReader(in), blockSize);
			String line;
			while((line = br.readLine()) != null)
			{
				if(line.split("\\s")[0].contains(substrSelect))
				{
					outPutBlock.add(line);
					// if output block is full, flush it to disk and clear it.
					if(outPutBlock.size() == lineInOneBlock)
					{
						for(int j = 0; j < lineInOneBlock; ++j)
						{
							bw.write(outPutBlock.get(j) + "\n");
							System.out.println("New Wrote New: "+ outPutBlock.get(j));
						}
						outPutBlock.clear();
					}
				}
			}

			if(outPutBlock.size() != 0)
			{
				for(int j = 0; j < outPutBlock.size(); j++)
				{
					bw.write(outPutBlock.get(j) + "\n");
					System.out.println("New Wrote New: "+ outPutBlock.get(j));
				}
				outPutBlock.clear();
			}
			outPutBlock.clear();
			br.close();
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void joinAndSelectEfficiently(String in1, String in2, String out,
										 String substrSelect, String tmpPath) {

		// TODO implement part D
		try
		{
			final int blockSize = 4096; // 4kb
			final int sizeOfOneLine = 52;
			final int lineInOneBlock = blockSize / sizeOfOneLine;
			ArrayList<String> outPutBlock = new ArrayList<>(lineInOneBlock);
			BufferedWriter bw = new BufferedWriter(new FileWriter(out));
			BufferedReader tr = new BufferedReader(new FileReader(in1), blockSize);
			BufferedReader ts = new BufferedReader(new FileReader(in2), blockSize);
			BufferedReader gs = new BufferedReader(new FileReader(in2), blockSize);
			String lineTr, lineGs, lineTs = "";
			lineTr = tr.readLine();
			lineGs = gs.readLine();
			while(lineTr != null && lineGs != null)
			{
				// while tr != EOF and lineTr.idx < lineGs.idx
				while (lineTr != null &&
						(lineTr.split("\\s")[0].compareTo(lineGs.split("\\s")[0]) < 0))
				{
					lineTr = tr.readLine();
				}
				// if the line isn't substring of the select, move to the next line.
				while(lineTr != null && !lineTr.split("\\s")[0].contains(substrSelect))
				{
					lineTr = tr.readLine();
				}

				// while gs != EOF and lineTr.idx > lineGs.idx
				while (lineTr != null && (lineTr.split("\\s")[0].compareTo(lineGs.split("\\s")[0]) > 0))
				{
					lineGs = gs.readLine();
				}
				while(!lineTs.equals(lineGs))
				{
					lineTs = ts.readLine();
				}

				while(!lineTs.equals(lineGs))
				{
					lineTs = ts.readLine();
				}
				ts.mark(1);
				String curr = lineTs;
				while(lineTr != null && lineTr.split("\\s")[0].equals(lineGs.split("\\s")[0]))
				{ //1
					//TS = GS
					lineTs = curr;
					ts.reset();
					// while ts != eof and lineTs.idx == lineTr.idx
					while(lineTs != null && lineTs.split("\\s")[0].compareTo(lineGs.split("\\s")[0]) == 0)
					{
						// join the two lines
						outPutBlock.add(lineTr + lineTs.substring(lineTs.indexOf(" ")));
						lineTs = ts.readLine();

						// if output block is full, flush it to disk and clear it.
						if(outPutBlock.size() == lineInOneBlock)
						{
							for(int j = 0; j < lineInOneBlock; ++j)
							{
								bw.write(outPutBlock.get(j) + "\n");
								System.out.println("New Wrote New: "+ outPutBlock.get(j));
							}
							outPutBlock.clear();
						}
					}
					do {
						lineTr = tr.readLine();
					}
					while(!lineTr.split("\\s")[0].contains(substrSelect));

				} //1
				// GS = TS
				while(!lineGs.equals(lineTs))
				{
					lineGs = gs.readLine();
					if(lineGs == null)
					{
						break;
					}
				}
			}

			if(outPutBlock.size() != 0)
			{
				for(int j = 0; j < outPutBlock.size(); j++)
				{
					bw.write(outPutBlock.get(j) + "\n");
					System.out.println("New Wrote New: "+ outPutBlock.get(j));
				}
				outPutBlock.clear();
			}
			outPutBlock.clear();
			bw.close();
			tr.close();
			ts.close();
			gs.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
