package jsonhandling.jobparser;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import jsonhandling.BuildParser;
import jsonhandling.JobParser;
import jsonhandling.JsonReader;
import jsonhandling.ParserUtil;
import jsonhandling.TestReportParser;

import org.codehaus.jackson.JsonNode;
import org.hamcrest.core.IsNull;
import org.junit.Test;

public class JobParserTest
{
	JsonReaderMock jsonreader = new JsonReaderMock();

	@Test
	public void abortedJobTest() throws IOException
	{
		//small info is the same as the info from a view (0-depth)
		JobParser job = new JobParser(ParserUtil.parseJsonFile(this, "abortedjob-small.json"));
		assertThat(job.hasBuildInformationAvailable(), equalTo(false));

		jsonreader.setNextResult("abortedjob.json");
		job.loadBuildInformation(jsonreader);

		assertThat(job.hasBuildInformationAvailable(), equalTo(true));
		assertThat(job.getLastCompletedBuildNumber(), equalTo(2L));

		jsonreader.setNextResult("abortedjob-2.json");
		BuildParser build = job.loadLastCompletedBuild(jsonreader);
		assertThat(build.getStatus(), equalTo(BuildParser.BuildStatus.ABORTED));
		assertThat(build.getDescription(), IsNull.nullValue());
		assertThat(build.getTimestamp(), equalTo(1358969670031L));
		assertThat(build.getUrl(), equalTo("http://bos-laptop:8080/job/AbortedJob/2/"));
		assertThat(build.hasTestResults(), equalTo(false));
	}

	@Test(expected = IllegalStateException.class)
	public void abortedJobTestWithoutBuildInfo() throws IOException
	{
		//small info is the same as the info from a view (0-depth)
		JsonNode smalljobDetails = ParserUtil.parseJsonFile(this, "abortedjob-small.json");
		JobParser job = new JobParser(smalljobDetails);
		assertThat(job.hasBuildInformationAvailable(), equalTo(false));
		job.getLastCompletedBuildNumber();
	}

	@Test
	public void installJobTest() throws IOException
	{
		//small info is the same as the info from a view (0-depth)
		JobParser job = new JobParser(ParserUtil.parseJsonFile(this, "install-crd04.json"));
		assertThat(job.hasBuildInformationAvailable(), equalTo(true));
		assertThat(job.getLastCompletedBuildNumber(), equalTo(932L));

		jsonreader.setNextResult("install-crd04-932.json");
		BuildParser build = job.loadLastCompletedBuild(jsonreader);
		assertThat(build.getStatus(), equalTo(BuildParser.BuildStatus.FAILED));
		assertThat(build.getDescription(), equalTo("ERROR: Directory E:/cordys/bop4/defaultInst is locked."));
		assertThat(build.hasTestResults(), equalTo(false));
	}

	@Test
	public void loadtestJobTest() throws IOException
	{
		//small info is the same as the info from a view (0-depth)
		JobParser job = new JobParser(ParserUtil.parseJsonFile(this, "loadtest.json"));
		assertThat(job.hasBuildInformationAvailable(), equalTo(true));
		assertThat(job.getLastCompletedBuildNumber(), equalTo(1091L));

		jsonreader.setNextResult("loadtest-1091.json");
		BuildParser build = job.loadLastCompletedBuild(jsonreader);

		assertThat(build.getStatus(), equalTo(BuildParser.BuildStatus.UNSTABLE));
		assertThat(build.getDescription(), equalTo("Loadtest on #1424"));
		assertThat(build.hasTestResults(), equalTo(true));
		assertThat(build.getFailedTestCount(), equalTo(2L));

		jsonreader.setNextResult("loadtest-1091-testreport.json");
		TestReportParser testReport = build.loadTestReport(jsonreader);

		assertThat(testReport.getFailedTestCount(), equalTo(2L));
		assertThat(testReport.getAllTestCases().size(), equalTo(39));
		assertThat(testReport.getFailingTestCases().size(), equalTo(2));
	}

	private class JsonReaderMock implements JsonReader
	{
		private JsonNode nextResult;

		public void setNextResult(final String fileName) throws IOException
		{
			nextResult = ParserUtil.parseJsonFile(this, fileName);
		}

		@Override
		public JsonNode getJSonResult(final String url)
		{
			JsonNode resultToReturn = nextResult;
			nextResult = null; //reset result
			return resultToReturn;
		}
	}
}
