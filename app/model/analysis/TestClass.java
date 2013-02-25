package model.analysis;

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

@Entity(name = "testclass")
public class TestClass extends EntityBase
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 250)
	@Required
	private String className;

	@OneToMany(targetEntity = TestMethod.class, fetch = FetchType.EAGER, mappedBy = "testClass", cascade = CascadeType.ALL)
	private Set<TestMethod> testMethods;

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

	public String getClassName()
	{
		return className;
	}

	public void setClassName(final String className)
	{
		this.className = className;
	}

	public Set<TestMethod> getTestMethods()
	{
		return testMethods;
	}
}
