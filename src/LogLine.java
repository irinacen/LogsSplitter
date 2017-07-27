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
	
	public static final String DEFAULT_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss,SSS";	
	
	private static SimpleDateFormat usedTimeFormat;
	private Date lineTimestamp;
	private String lineContent;
	
	public LogLine(String line) throws Exception
	{
		// Check timestamp format and save it if it is ok.
		String[] dateStr = line.split(" ");
		try
		{
			this.setLineTimestamp(usedTimeFormat.parse(dateStr[0] + " " + dateStr[1]));
			this.setLineContent(line);
		}
		catch(Exception e)
		{
			throw new Exception("Invalid time format in log line: '" + line + "'. Current pattern in use is '" + LogLine.getUsedTimeFormatPattern() + "'.", e);
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

	public static SimpleDateFormat getUsedTimeFormat()
	{
		return usedTimeFormat;
	}
	
	public static String getUsedTimeFormatPattern()
	{
		return usedTimeFormat.toPattern();
	}

	public static void setUsedTimeFormat(SimpleDateFormat usedTimeFormat)
	{
		LogLine.usedTimeFormat = usedTimeFormat;
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
