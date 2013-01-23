package jsonhandling;

import org.codehaus.jackson.JsonNode;

public class BuildParser extends BaseParser
{
	public enum BuildStatus
	{
		STABLE("SUCCESS"), UNSTABLE("UNSTABLE"), FAILED("FAILURE"), ABORTED("ABORTED");

		private String jenkinsStatus;

		BuildStatus(final String jenkinsStatus)
		{
			this.jenkinsStatus = jenkinsStatus;
		}

		public static BuildStatus fromString(final String text)
		{
			if (text != null)
			{
				for (BuildStatus status : BuildStatus.values())
				{
					if (text.equalsIgnoreCase(status.jenkinsStatus))
					{
						return status;
					}
				}
			}
			throw new IllegalStateException(String.format("'%s' is not a valid build status", text));
		}
	}

	public BuildParser(final JsonNode node)
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

	public long getTimestamp()
	{
		return path("timestamp").asLong();
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
		return new TestReportParser(reader.getJSonResult(getUrl() + "testReport"));
	}
}
