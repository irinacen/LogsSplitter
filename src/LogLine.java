import static java.util.Arrays.asList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
	
	public static SimpleDateFormat usedTimeFormat;
	
	private String lineContent;
	
	public LogLine(String line)
	{
		//TODO: Check usedTimeFormat in given line.
		
		this.setLineContent(line);
	
	}
		
	@Override
	public int compareTo(final LogLine logLine)
	{
		// TODO: Order by date. 
		//this.lineContent.split(regex)
		
		return 1;
	}

	public String getLineContent()
	{
		return lineContent;
	}

	public void setLineContent(String lineContent)
	{
		this.lineContent = lineContent;
	}

}
