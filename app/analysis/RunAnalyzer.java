package analysis;

import jsonhandling.BuildParser;
import jsonhandling.JsonReader;
import model.jenkins.Job;

public class RunAnalyzer
{

	private final Job m_job;

	private final BuildParser m_buildParser;

	private final JsonReader m_jsonReader;

	public RunAnalyzer(final Job job, final BuildParser buildParser, final JsonReader jsonReader)
	{
		m_job = job;
		m_buildParser = buildParser;
		m_jsonReader = jsonReader;
	}

	public void analyze()
	{

	}

}
