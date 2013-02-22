package model.jenkins;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import jsonhandling.BuildStatus;
import model.EntityBase;
import model.analysis.Failure;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.data.validation.Constraints.Required;

@Entity(name = "build")
public class Build extends EntityBase
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Required
	private Long buildNumber;

	@Required
	private String status;

	@Required
	private Long timestamp;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = Job.class)
	@JoinColumn(name = "job_id", nullable = false, updatable = true, insertable = true)
	private Job job;

	@OneToMany(targetEntity = Failure.class, fetch = FetchType.LAZY, mappedBy = "build", cascade = CascadeType.ALL)
	private Set<Failure> failures;

	@Override
	protected void setId(final Long id)
	{
		this.id = id;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public Long getBuildNumber()
	{
		return buildNumber;
	}

	public void setBuildNumber(final Long buildNumber)
	{
		this.buildNumber = buildNumber;
	}

	@JsonIgnore
	public Job getJob()
	{
		return job;
	}

	public void setJob(final Job job)
	{
		this.job = job;
	}

	public Long getTimestamp()
	{
		return timestamp;
	}

	public void setTimestamp(final Long timestamp)
	{
		this.timestamp = timestamp;
	}

	public void setStatus(final BuildStatus buildStatus)
	{
		status = buildStatus.asText();
	}

	public BuildStatus getStatus()
	{
		return BuildStatus.fromString(status);
	}

	public Set<Failure> getFailures()
	{
		return failures;
	}
}
