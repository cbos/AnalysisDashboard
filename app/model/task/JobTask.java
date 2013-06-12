package model.task;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import play.data.validation.Constraints.Required;

@Entity(name = "jobtask")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobTask extends Task
{
	@Required
	private String relatedJobs;

	public String getRelatedJobs()
	{
		return relatedJobs;
	}

	public void setRelatedJobs(final String relatedJobs)
	{
		this.relatedJobs = relatedJobs;
	}
}
