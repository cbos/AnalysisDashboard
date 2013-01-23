package jsonhandling.viewparser;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

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
			assertThat(job.getName(), notNullValue());
			assertThat(job.getUrl(), notNullValue());
			assertThat(job.getStatus(), notNullValue());
			assertThat(job.hasBuildInformationAvailable(), equalTo(false));
		}
	}

	@Test
	public void testAnotherView() throws IOException
	{
		ViewParser vp = parseFile("anotherview.json");
		for (JsonNode jobNode : vp.getJobs())
		{
			JobParser job = new JobParser(jobNode);
			if ("AbortedJob".equals(job.getName()))
			{
				assertThat(job.getStatus(), equalTo(JobParser.JobStatus.ABORTED));
			}

			if ("NewJob".equals(job.getName()))
			{
				assertThat(job.getStatus(), equalTo(JobParser.JobStatus.NEW));
			}

			assertThat(job.getName(), notNullValue());
			assertThat(job.getUrl(), notNullValue());
			assertThat(job.getStatus(), notNullValue());
			assertThat(job.hasBuildInformationAvailable(), equalTo(false));
		}
	}

	@Test
	public void testViewOneDepth() throws IOException
	{
		ViewParser vp = parseFile("viewDepthOne.json");
		for (JsonNode jobNode : vp.getJobs())
		{
			JobParser job = new JobParser(jobNode);
			assertThat(job.getName(), notNullValue());
			assertThat(job.getUrl(), notNullValue());
			assertThat(job.getStatus(), notNullValue());
			assertThat(job.hasBuildInformationAvailable(), equalTo(true));
		}
	}

	private ViewParser parseFile(final String fileName) throws IOException
	{
		return new ViewParser(ParserUtil.parseJsonFile(this, fileName));
	}
}
