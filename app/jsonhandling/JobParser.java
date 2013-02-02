package jsonhandling;

import org.codehaus.jackson.JsonNode;

public class JobParser extends BaseParser
{
	public enum JobStatus
	{
		STABLE("blue"), UNSTABLE("yellow"), DISABLED("disabled"), FAILED("red"), ABORTED("aborted"), NEW("grey");

		private String jenkinsStatus;

		JobStatus(final String jenkinsStatus)
		{
			this.jenkinsStatus = jenkinsStatus;
		}

		public static JobStatus fromString(String text)
		{
			if (text != null)
			{
				text = text.replace("_anime", "");
				for (JobStatus status : JobStatus.values())
				{
					if (text.equalsIgnoreCase(status.jenkinsStatus))
					{
						return status;
					}
				}
			}
			throw new IllegalStateException(String.format("'%s' is not a valid job status", text));
		}
	}

	public JobParser(final JsonNode node)
	{
		super(node);
	}

	public String getName()
	{
		return path("name").asText();
	}

	public String getUrl()
	{
		return path("url").asText();
	}

	public JobStatus getStatus()
	{
		return JobStatus.fromString(path("color").asText());
	}

	public boolean hasBuildInformationAvailable()
	{
		return !path("lastCompletedBuild").isMissingNode();
	}

	public boolean isMatrixJob()
	{
		return !path("activeConfigurations").isMissingNode();
	}

	public void loadBuildInformation(final JsonReader reader)
	{
		replaceNode(reader.getJSonResult(getUrl()));
	}

	public long getLastCompletedBuildNumber()
	{
		checkHasCompletedBuild();
		return path("lastCompletedBuild", "number").asLong();
	}

	private void checkHasCompletedBuild()
	{
		if (!hasBuildInformationAvailable())
		{
			throw new IllegalStateException("No build information available");
		}
		if (JobStatus.NEW == getStatus())
		{
			throw new IllegalStateException("Job is NEW, so there is no complete build");
		}
	}

	public BuildParser loadLastCompletedBuild(final JsonReader reader)
	{
		checkHasCompletedBuild();
		return new BuildParser(reader.getJSonResult(path("lastCompletedBuild", "url").asText()));
	}
}
