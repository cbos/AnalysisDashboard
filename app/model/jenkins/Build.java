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
import model.analysis.TestFailure;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import play.data.validation.Constraints.Required;

@Entity(name = "build")
@JsonIgnoreProperties(ignoreUnknown = true)
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

	private String displayName;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = Job.class)
	@JoinColumn(name = "job_id", nullable = false, updatable = true, insertable = true)
	private Job job;

	@ManyToOne(optional = true, fetch = FetchType.EAGER, targetEntity = Build.class)
	@JoinColumn(name = "parentBuild_id", nullable = true, updatable = true, insertable = true)
	private Build parentBuild;

	@OneToMany(targetEntity = Build.class, fetch = FetchType.LAZY, mappedBy = "parentBuild", cascade = CascadeType.ALL)
	private Set<Build> childBuilds;

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

	public String getFailedTestCount()
	{
		BuildStatus parsedStatus = getStatus();
		if (parsedStatus == BuildStatus.FAILED || parsedStatus == BuildStatus.UNSTABLE)
		{
			long testFailCount = 0l;
			if (childBuilds.size() > 0)
			{
				for (Build childBuild : childBuilds)
				{
					testFailCount += childBuild._getFailedTestCount();
				}
			}
			else
			{
				testFailCount = _getFailedTestCount();
			}
			if (testFailCount > 0)
			{
				return Long.toString(testFailCount);
			}
		}
		return "";
	}

	private long _getFailedTestCount()
	{
		long testFailCount = 0l;
		for (Failure failure : failures)
		{
			if (failure instanceof TestFailure)
			{
				testFailCount++;
			}
		}
		return testFailCount;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(final String displayName)
	{
		this.displayName = displayName;
	}

	@JsonIgnore
	public Build getParentBuild()
	{
		return parentBuild;
	}

	public void setParentBuild(final Build parentBuild)
	{
		this.parentBuild = parentBuild;
	}

	public Set<Build> getChildBuilds()
	{
		return childBuilds;
	}
}
