package model.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Query;

import model.EntityBase;
import model.user.User;
import play.data.validation.Constraints.Required;
import utils.EMHelper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity(name = "task")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = ComputerTask.class, name = "computertask"),
							 @Type(value = JobTask.class, name = "jobtask"),
							 @Type(value = Task.class, name = "task") })
@JsonIgnoreProperties(ignoreUnknown = true)
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
		this.summary = truncate(summary, 250);
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

	public static List<Task> getTodaysList()
	{
		String genericQueryPart = "from task t where t.done=0 or t.updateDate > :today";

		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);

		final Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 0, 0, 0);
		final Date today = now.getTime();

		dataQuery.setParameter("today", today);

		return dataQuery.getResultList();
	}
}
