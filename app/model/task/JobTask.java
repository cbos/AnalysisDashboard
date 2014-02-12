package model.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import model.EntityHelper;
import model.jenkins.Job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Entity(name = "jobtask")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobTask extends Task
{
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "jobtask_job", joinColumns = { @JoinColumn(name = "jobtask_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "job_id", referencedColumnName = "id") })
	private List<Job> jobs;

	@Transient
	private JsonNode m_jsonNodeForUpdate;

	@JsonIgnore
	public List<Job> _getJobs()
	{
		return jobs;
	}

	public void _setJobs(final List<Job> jobs)
	{
		this.jobs = jobs;
	}

	public JsonNode getRelatedJobs()
	{
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
		if (jobs != null)
		{
			for (Job job : jobs)
			{
				ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
				objectNode.put("id", job.getId());
				objectNode.put("name", job.getName());
				arrayNode.add(objectNode);
			}
		}
		return arrayNode;
	}

	public void updateRelatedJobs()
	{
		if (m_jsonNodeForUpdate != null)
		{
			if (jobs == null)
			{
				jobs = new ArrayList<>();
			}

			List<Job> untouchedJobs = new ArrayList<>(jobs);
			Iterator<JsonNode> elements = m_jsonNodeForUpdate.elements();
			while (elements.hasNext())
			{
				JsonNode jobElement = elements.next();
				long jobId = jobElement.path("id").asLong();
				Job job = getJob(jobId, jobs);
				if (job == null)
				{
					jobs.add(EntityHelper.getEntityById(Job.class, jobId));
				}
				else
				{
					untouchedJobs.remove(job);
				}
			}

			if (!untouchedJobs.isEmpty())
			{
				jobs.removeAll(untouchedJobs);
			}
		}
	}

	private Job getJob(final long id, final List<Job> jobs)
	{
		for (Job job : jobs)
		{
			if (job.getId() == id)
			{
				return job;
			}
		}
		return null;
	}

	public void setRelatedJobs(final JsonNode node)
	{
		m_jsonNodeForUpdate = node;
	}
}
