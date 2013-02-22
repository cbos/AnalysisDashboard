package model.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import model.EntityBase;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.data.validation.Constraints.Required;

@Entity(name = "testmethod")
public class TestMethod extends EntityBase
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 250)
	@Required
	private String methodName;

	@ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = TestClass.class)
	@JoinColumn(name = "testClass_id", nullable = false, updatable = true, insertable = true)
	private TestClass testClass;

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

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(final String methodName)
	{
		this.methodName = methodName;
	}

	@JsonIgnore
	public TestClass getTestClass()
	{
		return testClass;
	}

	public void setTestClass(final TestClass testClass)
	{
		this.testClass = testClass;
	}
}
