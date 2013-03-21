package analysis.analyzers;

import java.util.List;

import jsonhandling.BuildParser;
import jsonhandling.JobParser;
import jsonhandling.JsonReader;
import jsonhandling.RunParser;
import model.EntityHelper;
import model.jenkins.Build;
import model.jenkins.Job;

public class JobAnalyzer
{
	private final Job m_job;

	private final JobParser m_jobParser;

	private final JsonReader m_jsonReader;

	public JobAnalyzer(final Job job, final JobParser jobParser, final JsonReader jsonReader)
	{
		m_job = job;
		m_jobParser = jobParser;
		m_jsonReader = jsonReader;
	}

	public void analyze()
	{
		if (!m_jobParser.hasBuildInformationAvailable())
		{
			m_jobParser.loadBuildInformation(m_jsonReader);
		}

		m_job.setBuilding(m_jobParser.isBuilding());
		m_job.setStatus(m_jobParser.getStatus());
		m_job.setDescription(m_jobParser.getDescription());

		switch (m_jobParser.getStatus())
		{
		case NEW:
			return;
		case ABORTED:
		case DISABLED:
		case FAILED:
		case STABLE:
		case UNSTABLE:
			long lastCompletedBuildNumber = m_jobParser.getLastCompletedBuildNumber();
			if (!m_job.getLastBuildNumber().equals(Long.valueOf(lastCompletedBuildNumber)))
			{
				analyzeDetails();
			}
			break;
		default:
			break;
		}

		EntityHelper.persist(m_job);
	}

	private void analyzeDetails()
	{
		BuildParser buildParser = m_jobParser.loadLastCompletedBuild(m_jsonReader);
		Build rootBuild = new RunAnalyzer(m_job, buildParser, m_jsonReader).analyze();
		if (buildParser.hasRuns())
		{
			List<RunParser> runs = buildParser.getRuns(m_jsonReader);
			for (RunParser runParser : runs)
			{
				Build childBuild = new RunAnalyzer(m_job, runParser, m_jsonReader).analyze();
				childBuild.setParentBuild(rootBuild);
			}
		}

		m_job.setLastBuildNumber(rootBuild.getBuildNumber());
		m_job.setLastBuild(rootBuild);
	}
}
