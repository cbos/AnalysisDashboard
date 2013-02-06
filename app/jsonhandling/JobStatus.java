package jsonhandling;

public enum JobStatus
{
	STABLE("blue"), UNSTABLE("yellow"), DISABLED("disabled"), FAILED("red"), ABORTED("aborted"), NEW("grey");

	private String jenkinsStatus;

	JobStatus(final String jenkinsStatus)
	{
		this.jenkinsStatus = jenkinsStatus;
	}

	public String asText()
	{
		return jenkinsStatus;
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