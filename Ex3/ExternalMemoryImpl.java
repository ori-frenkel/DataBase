package Join;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Scanner;

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

	@Override
	public void sort(String in, String out, String tmpPath) {

		// TODO: Implement
		// STEP1 - reading line to buffer less than 50mb (buffer size) sorting them and write to disk
		// the number of line that would be ~50 mb
		FileInputStream inputStream = null;

		int buffer_line = 4; // the number of lines in buffer
		int buffer_size = buffer_line * line_size_in_bytes(in); // the buffer it bytes
		File tmp_sorted_blocks = null;
		try {
			tmp_sorted_blocks = File.createTempFile("TempFileMaine", ".txt", new File(tmpPath));
//			tmp_sorted_blocks.deleteOnExit();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader br = new BufferedReader(new FileReader(in), buffer_size);
			String line;
			int count = 0;
			ArrayList<String> line_in_buffer = new ArrayList<String>();
			BufferedWriter bw = new BufferedWriter(new FileWriter(tmp_sorted_blocks, true));
			while((line = br.readLine()) != null){
				line_in_buffer.add(line);
				count++;
				if (count == buffer_line){
					// sorting and writing to tmp file.
					Collections.sort(line_in_buffer); // sorting the lines
					// writing then to tmp file.
					for(String _line : line_in_buffer){
						bw.write(_line + "\n"); // TODO : remember the last line in file is empty (and shouldn't
													// TODO  be in the output file empty line at the end)
						System.out.println("Writing : \n" +  _line);
					}
					line_in_buffer.clear();
					count = 0;
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
		} catch (IOException e) {
			e.printStackTrace();
		}

//		try (BufferedReader br = new BufferedReader(new FileReader(in), buffer_size)) {
//			String line;
//			while ((line = br.readLine()) != null) {
//				// process the line.
//				System.out.println(line);
//				String[] split = line.split("\\s+");
//				Line tmp = new Line(split[0], split[1], split[2]);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
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