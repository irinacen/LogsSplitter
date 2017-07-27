
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Main class that reads all the log files of a given directory and reorganizes it in new files separating it 
 * according to the thread that has written each trace and ordering the traces of each output file chronologically.
 * 
 * @author irina.cendrero
 *
 */
public class LogsSplitter
{

	private static String inputFolder;
	private static String outputFolder;
	
	public static void main(String[] args) throws Exception
	{
		LogsSplitter logsSplitter =  new LogsSplitter();
		
		if(logsSplitter.checkInputParameters(args) == true)
		{
			ArrayList<File> filesToBeProcessed = new ArrayList<File>();
			logsSplitter.getInputFilesNames(inputFolder, filesToBeProcessed);
			if(filesToBeProcessed.size() > 0)
			{					
				LinkedHashMap<String, TreeSet<LogLine>> processedLogFiles = new LinkedHashMap<String, TreeSet<LogLine>>();
				logsSplitter.processLogFiles(filesToBeProcessed, processedLogFiles);
				logsSplitter.saveOutputFiles(outputFolder, processedLogFiles);
			}
			else
			{
				System.out.println("ERROR: No files to be processed found in '" + inputFolder + "'.");
			}
		}
		else
		{
			System.err.println("ERROR: Incorrect arguments! Relaunch application giving the correct parameters.");
			logsSplitter.showUsage();
		}
	}
	
	/**
	 * Check if the received args are valid and if so, stored them in variables.
	 * 
	 * @param args
	 * 
	 * @return true if the number and content of input is right, false otherwise.
	 */
	private boolean checkInputParameters(final String[] args)
	{	
		if(args.length < 2)
		{
			return false;
		}
		else 
		{
			inputFolder = args[0];
			outputFolder = args[1];
			
			if (inputFolder.equals(outputFolder))
			{
				System.err.println("ERROR: input and output folders can not be the same.");
				return false;
			}
			return true;
		}
	}

	/**
	 * Shows the application usage indicating the input parameters.
	 */
	private void showUsage()
	{
		String usage = new String("Usage:");
		usage += "\n\n LogsSplitter <inputFolder> <outputFolder>";
		usage += "\n inputFolder \t - Folder containing input log files. All files in folder will be processed, so the folder should contain only the log files. Spaces in folder name are not allowed.";
		usage += "\n outputFolder \t - Folder where the resulting log files will be saved. Spaces in folder name are not allowed.";
		
		System.out.println(usage);		
	}
	
	/**
	 * Recursive method to get and store the file names inside the given input folder.
	 * 
	 * @param folderName Folder containing the input files.
	 * @param filesToBeProcessed List to store the files to be processed.
	 */
	private void getInputFilesNames(final String folderName, ArrayList<File> filesToBeProcessed)
	{
		File folder = new File(folderName);
		System.out.println("Getting files from: " + folderName);
		
		for (final File fileEntry : folder.listFiles())
		{
	        if (fileEntry.isDirectory())
	        {
	        	getInputFilesNames(fileEntry.getAbsolutePath(), filesToBeProcessed);
	        } 
	        else
	        {	        	
	        	filesToBeProcessed.add(new File(fileEntry.getAbsolutePath()));
	        }
	    }
	}
	
	/**
	 * Process all log files sortering it by thread in an ordered and no key duplicated hashmap. 
	 * The value of hashmap is a set of LogLine, whcih will be ordered automatically immplementing the comparable interface.
	 * 
	 * @param filesToBeProcessed List of files to be processed.
	 * @param processedLogFiles HashMap where the result will be saved.
	 * 
	 * @throws Exception
	 */
	private void processLogFiles(final ArrayList<File> filesToBeProcessed, LinkedHashMap<String, TreeSet<LogLine>> processedLogFiles) throws Exception
	{
		System.out.println("Starting to process '" + filesToBeProcessed.size() + "' files in '" + inputFolder + "'.");
		for (File file : filesToBeProcessed)
		{
			System.out.println("Processing file '" + file.getName() + "' ...");
			
			BufferedReader br = new BufferedReader(new FileReader(file));			 
			String line = null;
			try
			{
				while ((line = br.readLine()) != null) 
				{
					String threadName;
					try
					{
						threadName = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
					}
					catch (Exception e)
					{
						throw new Exception("Thread name not found in line: '" + line + "'.", e);
					}
					
					if(processedLogFiles.containsKey(threadName))
					{
						processedLogFiles.get(threadName).add(new LogLine(line));
					}
					else
					{
						TreeSet<LogLine> lines = new TreeSet<LogLine>();
						lines.add(new LogLine(line));
						processedLogFiles.put(threadName, lines);
					}
				}
				System.out.println("Done!");
			}
			finally
			{
				br.close();
			}
		}
	}


	/**
	 * Save the processed (separated by thread and ordered by date) files by thread in the folder specified as outputFolder.
	 * 
	 * @param outputFolder Folder where save the resulting files.
	 * @param processedLogFiles HashMap with the processed files.
	 * 
	 * @throws IOException
	 */
	private void saveOutputFiles(final String outputFolder, final LinkedHashMap<String, TreeSet<LogLine>> processedLogFiles) throws IOException
	{
		for (Map.Entry<String, TreeSet<LogLine>> entry : processedLogFiles.entrySet()) {
			
		    String fileName = entry.getKey();
		    TreeSet<LogLine> fileLines = entry.getValue();
		    
		    File fileToBeWrited = new File(outputFolder + File.separator + fileName +".log");
			FileOutputStream fos = new FileOutputStream(fileToBeWrited);
		 
			System.out.println("Saving file '" + fileToBeWrited.getAbsolutePath() + "' ...");
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));			
			try
			{
				for (LogLine logLine : fileLines)
				{
					bw.write(logLine.getLineContent());
					bw.newLine();
				}
			}
			finally
			{
				bw.close();
			}
		}
	}
}
