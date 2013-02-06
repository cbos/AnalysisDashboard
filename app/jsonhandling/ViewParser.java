package jsonhandling;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;

public class ViewParser extends BaseParser
{
	public ViewParser(final JsonNode node)
	{
		super(node);
	}

	public List<JobParser> getJobs()
	{
		ArrayList<JobParser> jobs = new ArrayList<>();
		for (JsonNode jobNode : path("jobs"))
		{
			jobs.add(new JobParser(jobNode));
		}
		return jobs;
	}
}
