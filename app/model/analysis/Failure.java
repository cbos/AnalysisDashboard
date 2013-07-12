package model.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import model.EntityBase;
import model.issue.Issue;
import model.jenkins.Build;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@Entity(name = "failure")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = TestFailure.class, name = "testfailure"), @Type(value = Failure.class, name = "failure") })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Failure extends EntityBase
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 250)
	private String summary;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = Build.class)
	@JoinColumn(name = "build_id", nullable = false, updatable = true, insertable = true)
	private Build build;

	@ManyToOne(optional = true, fetch = FetchType.EAGER, targetEntity = Issue.class)
	@JoinColumn(name = "issue_id", nullable = true, updatable = true, insertable = true)
	private Issue issue;

	private boolean randomFailure;

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

	public boolean isRandomFailure()
	{
		return randomFailure;
	}

	public void setRandomFailure(final boolean randomFailure)
	{
		this.randomFailure = randomFailure;
	}

	@JsonIgnore
	public Build getBuild()
	{
		return build;
	}

	public void setBuild(final Build build)
	{
		this.build = build;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(final String summary)
	{
		this.summary = truncate(summary, 250);
	}

	public Issue getIssue()
	{
		return issue;
	}

	public void setIssue(final Issue issue)
	{
		this.issue = issue;
	}
}
