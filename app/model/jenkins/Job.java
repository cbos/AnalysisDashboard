package model.jenkins;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;

import jsonhandling.JobStatus;
import model.EntityBase;
import model.task.JobTask;
import play.data.validation.Constraints.Required;
import utils.EMHelper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "job")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job extends EntityBase
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 75)
	@Required
	private String name;

	@Column(length = 250)
	private String description;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = JenkinsServer.class)
	@JoinColumn(name = "jenkinsServer_id", nullable = false, updatable = true, insertable = true)
	private JenkinsServer jenkinsServer;

	@OneToMany(targetEntity = Build.class, fetch = FetchType.LAZY, mappedBy = "job", cascade = CascadeType.ALL)
	private Set<Build> builds;

	@Required
	private Long lastBuildNumber;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Build.class, optional = true)
	@JoinColumn(name = "lastBuild_id", nullable = true, updatable = true, insertable = true)
	private Build lastBuild;

	private boolean isBuilding;

	private double eta;

	private String status;

	private boolean watch;

	private String type;

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

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	@JsonIgnore
	public JenkinsServer getJenkinsServer()
	{
		return jenkinsServer;
	}

	public void setJenkinsServer(final JenkinsServer jenkinsServer)
	{
		this.jenkinsServer = jenkinsServer;
	}

	public Long getLastBuildNumber()
	{
		return lastBuildNumber == null ? -1L : lastBuildNumber;
	}

	public void setLastBuildNumber(final Long lastBuildNumber)
	{
		this.lastBuildNumber = lastBuildNumber;
	}

	public boolean isBuilding()
	{
		return isBuilding;
	}

	public void setBuilding(final boolean isBuilding)
	{
		this.isBuilding = isBuilding;
	}

	public void setStatus(final JobStatus jobStatus)
	{
		status = jobStatus.asText();
	}

	public JobStatus getStatus()
	{
		return JobStatus.fromString(status);
	}

	public String getUrl()
	{
		return String.format("%s/job/%s", jenkinsServer.getUrl(), getName());
	}

	public boolean isWatch()
	{
		return watch;
	}

	public void setWatch(final boolean watch)
	{
		this.watch = watch;
	}

	public String getType()
	{
		return type == null ? "" : type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(final String description)
	{
		this.description = truncate(description, 249);
	}

	public Build getLastBuild()
	{
		return lastBuild;
	}

	public void setLastBuild(final Build lastBuild)
	{
		this.lastBuild = lastBuild;
	}

	public double getEta()
	{
		return eta;
	}

	public void setEta(final double eta)
	{
		this.eta = eta;
	}

	@Override
	public void delete()
	{
		for (JobTask task : getRelatedJobTaskEntries())
		{
			if (task._getJobs().size() == 1)
			{
				task.delete();
			}
			else
			{
				task._getJobs().remove(this);
			}
		}
		super.delete();
	}

	private List<JobTask> getRelatedJobTaskEntries()
	{
		String genericQueryPart = "select t from jobtask t join t.jobs j where j = :job";

		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		dataQuery.setParameter("job", this);
		return dataQuery.getResultList();
	}

	@JsonIgnore
	public List<Build> getBuilds(Date startDate, Date endDate)
	{
		String genericQueryPart = "select b from build b where b.job = :job and b.timestamp >= :startDate and b.timestamp < :endDate order by b.timestamp";

		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		dataQuery.setParameter("job", this);
		dataQuery.setParameter("startDate", startDate.getTime());
		dataQuery.setParameter("endDate", endDate.getTime());

		return dataQuery.getResultList();
	}

	public static List<Job> getUnstableJobs()
	{
		String genericQueryPart = "from job j where j.status<>'blue' and j.watch=true";
		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		return dataQuery.getResultList();
	}
}
