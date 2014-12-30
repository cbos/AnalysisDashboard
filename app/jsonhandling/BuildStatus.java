package jsonhandling;

public enum BuildStatus
{
	STABLE("SUCCESS"), UNSTABLE("UNSTABLE"), FAILED("FAILURE"), ABORTED("ABORTED"), NULL("NULL");

	private String jenkinsStatus;

	BuildStatus(final String jenkinsStatus)
	{
		this.jenkinsStatus = jenkinsStatus;
	}

	public String asText()
	{
		return jenkinsStatus;
	}

	public static BuildStatus fromString(final String text)
	{
		for (BuildStatus status : BuildStatus.values())
		{
			if (text.equalsIgnoreCase(status.jenkinsStatus))
			{
				return status;
			}
		}
		throw new IllegalStateException(String.format("'%s' is not a valid build status", text));
	}
}