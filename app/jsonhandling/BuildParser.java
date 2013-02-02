package jsonhandling;

import org.codehaus.jackson.JsonNode;

public class BuildParser extends RunParser
{
	public BuildParser(final JsonNode node)
	{
		super(node);
	}

	@Override
	public BuildStatus getStatus()
	{
		return BuildStatus.fromString(path("result").asText());
	}

	public boolean hasRuns()
	{
		return !path("runs").isMissingNode();
	}

	@Override
	public TestReportParser loadTestReport(final JsonReader reader)
	{
		if (hasRuns())
		{
			throw new IllegalStateException("Get the testReport by querying the runs");
		}
		if (!hasTestResults())
		{
			throw new IllegalStateException("There is not testReport available");
		}
		return new TestReportParser(reader.getJSonResult(getUrl() + "testReport"));
	}
}
