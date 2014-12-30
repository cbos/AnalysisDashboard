package jsonhandling;

import com.fasterxml.jackson.databind.JsonNode;

public class JobParser extends BaseParser
{
	public JobParser(final JsonNode node)
	{
		super(node);
	}

	public String getName()
	{
		return path("name").asText();
	}

	public String getDescription()
	{
		return path("description").asText();
	}

	public String getUrl()
	{
		return path("url").asText();
	}

	public boolean isBuilding()
	{
		return path("color").asText().endsWith("_anime");
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

	public BuildParser loadLastBuild(final JsonReader reader)
	{
		return new BuildParser(reader.getJSonResult(path("lastBuild", "url").asText()));
	}
}
