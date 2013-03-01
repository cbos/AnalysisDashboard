package model.task;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import model.EntityBase;
import model.user.User;
import play.data.validation.Constraints.Required;

@Entity(name = "task")
public class Task extends EntityBase
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 250)
	private String summary;

	@Lob
	private String details;

	@Required
	private Date creationDate;

	@Required
	private Date updateDate;

	@ManyToOne(optional = true, fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "assignee_id", nullable = true, updatable = true, insertable = true)
	private User assignee;

	private boolean done;

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

	public User getAssignee()
	{
		return assignee;
	}

	public void setAssignee(final User assignee)
	{
		this.assignee = assignee;
	}

	public boolean isDone()
	{
		return done;
	}

	public void setDone(final boolean done)
	{
		this.done = done;
	}
}
