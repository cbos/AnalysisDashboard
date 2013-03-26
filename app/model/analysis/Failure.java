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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import model.EntityBase;
import model.jenkins.Build;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@Entity(name = "failure")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = TestFailure.class, name = "testfailure"), @Type(value = Failure.class, name = "failure") })
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

	@Lob
	private String analysis;

	private boolean issueIdentified;

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

	public String getAnalysis()
	{
		return analysis;
	}

	public void setAnalysis(final String analysis)
	{
		this.analysis = analysis;
	}

	public boolean isIssueIdentified()
	{
		return issueIdentified;
	}

	public void setIssueIdentified(final boolean issueIdentified)
	{
		this.issueIdentified = issueIdentified;
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
}
