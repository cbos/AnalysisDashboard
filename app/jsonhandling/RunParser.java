package jsonhandling;

import com.fasterxml.jackson.databind.JsonNode;

public class RunParser extends BaseParser
{

	protected RunParser(final JsonNode node)
	{
		super(node);
	}

	public BuildStatus getStatus()
	{
		return BuildStatus.fromString(path("result").asText());
	}

	public String getDescription()
	{
		JsonNode descriptionNode = path("description");
		if (descriptionNode.isNull())
		{
			return null;
		}
		return descriptionNode.asText();
	}

	public Long getBuildNumber()
	{
		return path("number").asLong();
	}

	public String getFullDisplayName()
	{
		JsonNode displayNameNode = path("fullDisplayName");
		if (displayNameNode.isNull())
		{
			return null;
		}
		return displayNameNode.asText();
	}

	public long getTimestamp()
	{
		return path("timestamp").asLong();
	}

	public long getEstimatedDuration()
	{
		return path("estimatedDuration").asLong();
	}

	public String getUrl()
	{
		return path("url").asText();
	}

	public boolean hasTestResults()
	{
		return (getTestActionNode() != null);
	}

	private JsonNode getTestActionNode()
	{
		JsonNode actions = path("actions");
		for (JsonNode jsonNode : actions)
		{
			JsonNode urlName = jsonNode.path("urlName");
			if (!urlName.isMissingNode())
			{
				if ("testReport".equals(urlName.asText()))
				{
					return jsonNode;
				}
			}
		}
		return null;
	}

	public long getFailedTestCount()
	{
		JsonNode testActionNode = getTestActionNode();
		if (null == testActionNode)
		{
			return 0L;
		}
		return testActionNode.path("failCount").asLong();
	}

	public TestReportParser loadTestReport(final JsonReader reader)
	{
		if (!hasTestResults())
		{
			throw new IllegalStateException("There is not testReport available");
		}
		// Below JSONOutputFilter is added because 'stdout' in 'suites[cases[stdout]]]' can blow up returned JSON response above 1GB but it is never used
		String JSONOutputFilter = "/api/json?pretty=true&tree=duration,empty,failCount,passCount,skipCount,suites[cases[age,className,duration,errorDetails,errorStackTrace,failedSince,name,skipped,status],duration,id,name,timestamp]";
		return new TestReportParser(reader.getJSonResult(getUrl() + "testReport" + JSONOutputFilter));
	}
}
