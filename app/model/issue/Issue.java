package model.issue;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import model.EntityBase;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import play.data.validation.Constraints.Required;

@Entity(name = "issue")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue extends EntityBase
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 250)
	private String summary;

	@Lob
	private String details;

	@Column(length = 50)
	private String jira_id;

	@Required
	private Date creationDate;

	@Required
	private Date updateDate;

	private String type;

	private boolean solved;

	//	@ManyToMany
	//	@JoinTable(name = "issueToFailure", joinColumns = @JoinColumn(name = "issue_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "failure_id", referencedColumnName = "id"))
	//	private Set<Failure> failures;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	protected void setId(final Long id)
	{
		this.id = id;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(final String summary)
	{
		this.summary = summary;
	}

	public String getDetails()
	{
		return details;
	}

	public void setDetails(final String details)
	{
		this.details = details;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(final Date updateDate)
	{
		this.updateDate = updateDate;
	}

	public String getType()
	{
		return type;
	}

	public void setType(final String type)
	{
		this.type = type;
	}

	public boolean isSolved()
	{
		return solved;
	}

	public void setSolved(final boolean solved)
	{
		this.solved = solved;
	}

	public String getJira_id()
	{
		return jira_id;
	}

	public void setJira_id(final String jira_id)
	{
		this.jira_id = jira_id;
	}
}
