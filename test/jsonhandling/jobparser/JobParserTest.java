package jsonhandling.jobparser;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import jsonhandling.BuildParser;
import jsonhandling.JobParser;
import jsonhandling.JsonReader;
import jsonhandling.ParserUtil;
import jsonhandling.RunParser;
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
		assertThat(job.isMatrixJob(), equalTo(false));

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
		assertThat(build.hasRuns(), equalTo(false));
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
		assertThat(job.isMatrixJob(), equalTo(false));

		jsonreader.setNextResult("install-crd04-932.json");
		BuildParser build = job.loadLastCompletedBuild(jsonreader);
		assertThat(build.getStatus(), equalTo(BuildParser.BuildStatus.FAILED));
		assertThat(build.getDescription(), equalTo("ERROR: Directory E:/cordys/bop4/defaultInst is locked."));
		assertThat(build.hasTestResults(), equalTo(false));
		assertThat(build.hasRuns(), equalTo(false));
	}

	@Test
	public void loadtestJobTest() throws IOException
	{
		//small info is the same as the info from a view (0-depth)
		JobParser job = new JobParser(ParserUtil.parseJsonFile(this, "loadtest.json"));
		assertThat(job.hasBuildInformationAvailable(), equalTo(true));
		assertThat(job.isMatrixJob(), equalTo(false));
		assertThat(job.getLastCompletedBuildNumber(), equalTo(1091L));

		jsonreader.setNextResult("loadtest-1091.json");
		BuildParser build = job.loadLastCompletedBuild(jsonreader);

		assertThat(build.getStatus(), equalTo(BuildParser.BuildStatus.UNSTABLE));
		assertThat(build.getDescription(), equalTo("Loadtest on #1424"));
		assertThat(build.hasTestResults(), equalTo(true));
		assertThat(build.getFailedTestCount(), equalTo(2L));
		assertThat(build.hasRuns(), equalTo(false));

		jsonreader.setNextResult("loadtest-1091-testreport.json");
		TestReportParser testReport = build.loadTestReport(jsonreader);

		assertThat(testReport.getFailedTestCount(), equalTo(2L));
		assertThat(testReport.getAllTestCases().size(), equalTo(39));
		assertThat(testReport.getFailingTestCases().size(), equalTo(2));
	}

	@Test
	public void cwswipuiunitTest() throws IOException
	{
		JobParser job = new JobParser(ParserUtil.parseJsonFile(this, "cws-wip-uiunit.json"));
		assertThat(job.isMatrixJob(), equalTo(true));
		assertThat(job.hasBuildInformationAvailable(), equalTo(true));
		assertThat(job.getLastCompletedBuildNumber(), equalTo(1885L));

		jsonreader.setNextResult("cws-wip-uiunit-1885.json");
		BuildParser build = job.loadLastCompletedBuild(jsonreader);

		assertThat(build.getStatus(), equalTo(BuildParser.BuildStatus.UNSTABLE));
		assertThat(build.getDescription(), IsNull.nullValue());
		assertThat(build.hasTestResults(), equalTo(true));
		assertThat(build.getFailedTestCount(), equalTo(8L));
		assertThat(build.hasRuns(), equalTo(true));

		jsonreader.setNextResult("cws-wip-uiunit-1885-chrome.json");
		jsonreader.setNextResult("cws-wip-uiunit-1885-firefox.json");
		jsonreader.setNextResult("cws-wip-uiunit-1885-safari.json");
		List<RunParser> runs = build.getRuns(jsonreader);

		RunParser runChrome = runs.get(0);
		assertThat(runChrome.getStatus(), equalTo(BuildParser.BuildStatus.UNSTABLE));
		assertThat(runChrome.getFullDisplayName(),
							 equalTo("cws-wip-uiunit » 64,Chrome,oraclejdk-1.7.3 64,Chrome,oraclejdk-1.7.3"));
		assertThat(runChrome.hasTestResults(), equalTo(true));
		assertThat(runChrome.getFailedTestCount(), equalTo(2L));

		jsonreader.setNextResult("cws-wip-uiunit-1885-chrome-testreport.json");
		TestReportParser testReport = runChrome.loadTestReport(jsonreader);

		assertThat(testReport.getFailedTestCount(), equalTo(2L));
		assertThat(testReport.getAllTestCases().size(), equalTo(133));
		assertThat(testReport.getFailingTestCases().size(), equalTo(2));

		RunParser runFirefox = runs.get(1);
		assertThat(runFirefox.getStatus(), equalTo(BuildParser.BuildStatus.STABLE));
		assertThat(runFirefox.getFullDisplayName(),
							 equalTo("cws-wip-uiunit » 64,Firefox,oraclejdk-1.7.3 64,Firefox,oraclejdk-1.7.3"));
		assertThat(runFirefox.hasTestResults(), equalTo(true));
		assertThat(runFirefox.getFailedTestCount(), equalTo(0L));

		jsonreader.setNextResult("cws-wip-uiunit-1885-firefox-testreport.json");
		testReport = runFirefox.loadTestReport(jsonreader);

		assertThat(testReport.getFailedTestCount(), equalTo(0L));
		assertThat(testReport.getAllTestCases().size(), equalTo(133));
		assertThat(testReport.getFailingTestCases().size(), equalTo(0));

		RunParser runSafari = runs.get(2);
		assertThat(runSafari.getStatus(), equalTo(BuildParser.BuildStatus.UNSTABLE));
		assertThat(runSafari.getFullDisplayName(),
							 equalTo("cws-wip-uiunit » 64,Safari,oraclejdk-1.7.3 64,Safari,oraclejdk-1.7.3"));
		assertThat(runSafari.hasTestResults(), equalTo(true));
		assertThat(runSafari.getFailedTestCount(), equalTo(6L));

		jsonreader.setNextResult("cws-wip-uiunit-1885-safari-testreport.json");
		testReport = runSafari.loadTestReport(jsonreader);

		assertThat(testReport.getFailedTestCount(), equalTo(6L));
		assertThat(testReport.getAllTestCases().size(), equalTo(133));
		assertThat(testReport.getFailingTestCases().size(), equalTo(6));
	}

	private class JsonReaderMock implements JsonReader
	{
		private final Queue<JsonNode> nextResults = new LinkedList<>();

		public void setNextResult(final String fileName) throws IOException
		{
			nextResults.add(ParserUtil.parseJsonFile(this, fileName));
		}

		@Override
		public JsonNode getJSonResult(final String url)
		{
			return nextResults.poll();
		}
	}
}
