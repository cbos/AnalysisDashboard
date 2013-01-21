package model.jenkins;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import model.EntityBase;
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
	
	@Override
	protected void setId(Long id)
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

	public void setBuildNumber(Long buildNumber)
	{
		this.buildNumber = buildNumber;
	}

	public Job getJob()
	{
		return job;
	}

	public void setJob(Job job)
	{
		this.job = job;
	}
	
	public Long getTimestamp()
	{
		return timestamp;
	}
	
	public void setTimestamp(Long timestamp)
	{
		this.timestamp = timestamp;
	}
}
