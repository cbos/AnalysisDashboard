package analysis;

import java.util.List;

import jsonhandling.JobParser;
import jsonhandling.JsonReader;
import jsonhandling.ViewParser;
import model.EntityHelper;
import model.jenkins.JenkinsServer;
import model.jenkins.Job;

import org.codehaus.jackson.JsonNode;

public class ViewAnalyzer
{
	private final JenkinsServer m_jenkinsServer;

	private final String m_name;

	private final JsonReader m_jsonReader;

	public ViewAnalyzer(final JenkinsServer jenkinsServer, final String name, final JsonReader jsonReader)
	{
		m_jenkinsServer = jenkinsServer;
		m_name = name;
		m_jsonReader = jsonReader;
	}

	public void analyze()
	{
		String url = String.format("%s/view/%s/api/json", m_jenkinsServer.getUrl(), m_name);

		JsonNode viewNode = m_jsonReader.getJSonResult(url);
		ViewParser view = new ViewParser(viewNode);

		List<JobParser> jobs = view.getJobs();
		System.out.println(jobs.size());

		for (JobParser jobParser : jobs)
		{
			String jobName = jobParser.getName();
			Job job = getOrCreateJob(jobName);

			new JobAnalyzer(job, jobParser, m_jsonReader).analyze();
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
		EntityHelper.persist(newJob);
		return newJob;
	}
}
