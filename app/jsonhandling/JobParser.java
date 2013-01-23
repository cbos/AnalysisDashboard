package jsonhandling;

import org.codehaus.jackson.JsonNode;

public class JobParser extends BaseParser
{
	public enum JobStatus
	{
		STABLE("blue"), UNSTABLE("yellow"), DISABLED("disabled"), FAILED("red");

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

}
