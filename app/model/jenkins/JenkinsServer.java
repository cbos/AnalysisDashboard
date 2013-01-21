package model.jenkins;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import model.EntityBase;
import play.data.validation.Constraints.Required;

@Entity(name = "jenkinsserver")
public class JenkinsServer extends EntityBase
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50)
	@Required
	private String name;
	
	@Column(length = 250)
	@Required
	private String url;
	
	@OneToMany(targetEntity = Job.class, fetch = FetchType.LAZY, mappedBy = "jenkinsServer", cascade = CascadeType.ALL)
	private Set<Job> jobs;
	
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	public Set<Job> getJobs()
	{
		return jobs;
	}
}
