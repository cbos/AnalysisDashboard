package model.jenkins;

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

import model.EntityBase;
import play.data.validation.Constraints.Required;

@Entity(name = "job")
public class Job extends EntityBase
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	@Required
	private String name;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = JenkinsServer.class)
	@JoinColumn(name = "jenkinsServer_id", nullable = false, updatable = true, insertable = true)
	private JenkinsServer jenkinsServer;

	@OneToMany(targetEntity = Build.class, fetch = FetchType.LAZY, mappedBy = "job", cascade = CascadeType.ALL)
	private Set<Build> builds;

	@Required
	private Long lastBuildNumber;

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
}
