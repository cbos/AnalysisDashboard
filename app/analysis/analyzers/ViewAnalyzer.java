package analysis.analyzers;

import java.util.List;
import java.util.Set;

import jsonhandling.JobParser;
import jsonhandling.JsonReader;
import jsonhandling.ViewParser;
import model.EntityHelper;
import model.jenkins.JenkinsServer;
import model.jenkins.Job;

import com.fasterxml.jackson.databind.JsonNode;

import play.Logger;

public class ViewAnalyzer
{
	private final JenkinsServer m_jenkinsServer;

	private final String m_name;

	private final JsonReader m_jsonReader;

	private final Set<String> m_jobsAnalyzed;

	public ViewAnalyzer(final JenkinsServer jenkinsServer,
											final String name,
											final JsonReader jsonReader,
											final Set<String> jobsAnalyzed)
	{
		m_jenkinsServer = jenkinsServer;
		m_name = name;
		m_jsonReader = jsonReader;
		m_jobsAnalyzed = jobsAnalyzed;
	}

	public void analyze()
	{
		Logger.debug("Analyzing view " + m_name + " on " + m_jenkinsServer.getName());

		String url = String.format("%s/view/%s/api/json?depth=1", m_jenkinsServer.getUrl(), m_name);

		JsonNode viewNode = m_jsonReader.getJSonResult(url);
		ViewParser view = new ViewParser(viewNode);

		List<JobParser> jobs = view.getJobs();

		for (JobParser jobParser : jobs)
		{
			String jobName = jobParser.getName();
			//a job can exist multiple times on a view and on multiple views in one analysis round
			//it should be checked (and created) only once
			if (m_jobsAnalyzed.add(jobName))
			{
				Job job = getOrCreateJob(jobName);

				new JobAnalyzer(job, jobParser, m_jsonReader).analyze();
			}
		}
	}

	private Job getOrCreateJob(final String jobName)
	{
		for (Job job : m_jenkinsServer.getJobs())
		{
			if (job.getName().equals(jobName))
			{
				return job;
			}
		}

		Job newJob = new Job();
		newJob.setJenkinsServer(m_jenkinsServer);
		newJob.setName(jobName);
		newJob.setLastBuildNumber(0L);
		newJob.setLastBuild(null);
		newJob.setWatch(true);
		EntityHelper.persist(newJob);
		return newJob;
	}
}
