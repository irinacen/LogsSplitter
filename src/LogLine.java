
import static java.util.Arrays.asList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class representing each trace line from logs.
 * 
 * @author irina.cendrero
 *
 */
public class LogLine implements Comparable<LogLine>
{
	public static ArrayList<String> VALID_TIME_PATTERNS = new ArrayList<String>(
			asList("dd-MM-yyyy HH:mm:ss,SSS", "dd-MM HH:mm:ss,SSS", "dd-MM-yyyy HH:mm:ss", "dd-MM HH:mm:ss"));

	private Date lineTimestamp;
	private String lineContent;
	
	/**
	 * Creates a new LogLine containing the text received as line. 
	 * The line should contains something like:
	 * 24-07-2017 21:45:50 INFO [Thread9] Blah blah blah SomeText: '25' hello(0).  
	 * @param line
	 * @throws Exception
	 */
	public LogLine(String line) throws Exception
	{
		// Check timestamp of received line against valid patterns.
		SimpleDateFormat timeFormat; 
		boolean validPattern = false;
		
		// This does not give us good performance, but in this case we prefer usability.
		for (String pattern : VALID_TIME_PATTERNS)
		{
			String[] dateStr = line.split(" ");
			try
			{
				timeFormat = new SimpleDateFormat(pattern);
				this.setLineTimestamp(timeFormat.parse(dateStr[0] + " " + dateStr[1]));				
				this.setLineContent(line);
				validPattern = true;
			}
			catch(Exception e) {} // Nothing to doing here.
		}
		
		if(!validPattern)
		{
			throw new Exception("Invalid time format in log line: '" + line + "'. Valid patterns are: " + VALID_TIME_PATTERNS + ".");
		}
	}
		
	@Override
	public int compareTo(final LogLine logLine)
	{
		// We want order logLines by time chronologically, so we can simply call to compareTo method of Date.
		return this.getLineTimestamp().compareTo(logLine.getLineTimestamp());
	}

	public String getLineContent()
	{
		return lineContent;
	}

	public void setLineContent(String lineContent)
	{
		this.lineContent = lineContent;
	}

	public Date getLineTimestamp()
	{
		return lineTimestamp;
	}

	public void setLineTimestamp(Date timestamp)
	{
		this.lineTimestamp = timestamp;
	}
}
