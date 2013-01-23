package jsonhandling.viewparser;

import java.io.IOException;

import jsonhandling.JobParser;
import jsonhandling.ParserUtil;
import jsonhandling.ViewParser;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;

public class ViewParserTest
{
	@Test
	public void testView() throws IOException
	{
		ViewParser vp = parseFile("view.json");
		for (JsonNode jobNode : vp.getJobs())
		{
			JobParser job = new JobParser(jobNode);
			System.out.println(job.getName());
			System.out.println(job.getUrl());
			System.out.println(job.getStatus().toString());
		}
	}

	@Test
	public void testViewOneDepth() throws IOException
	{
		ViewParser vp = parseFile("viewDepthOne.json");
		for (JsonNode jobNode : vp.getJobs())
		{
			JobParser job = new JobParser(jobNode);
			System.out.println(job.getName());
			System.out.println(job.getUrl());
			System.out.println(job.getStatus().toString());
		}
	}

	private ViewParser parseFile(final String fileName) throws IOException
	{
		return new ViewParser(ParserUtil.parseJsonFile(this, fileName));
	}
}
