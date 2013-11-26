package model.analysis;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;

import model.EntityBase;

import org.codehaus.jackson.annotate.JsonIgnore;

import play.data.validation.Constraints.Required;
import utils.EMHelper;

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

	public Long getRandomFailureCount()
	{
		String genericQueryPart = "select count(f) from testfailure f where f.testMethod = :testMethod and f.randomFailure=1";

		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		dataQuery.setParameter("testMethod", this);

		return (Long) dataQuery.getSingleResult();
	}

	public Long getLastRandomOccurrence()
	{
		String genericQueryPart = "select f from testfailure f where f.testMethod = :testMethod and f.randomFailure=1 order by f.id DESC";

		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		dataQuery.setParameter("testMethod", this);
		dataQuery.setFirstResult(0);
		dataQuery.setMaxResults(1);

		List<TestFailure> results = dataQuery.getResultList();
		if (results.isEmpty())
		{
			return 0L;
		}
		return results.get(0).getBuild().getTimestamp();
	}

	public static List<TestMethod> getRandomFailures(int pageNumber, int pageSize)
	{
		String genericQueryPart = "select DISTINCT t from testmethod t, testfailure f where f.testMethod = t and f.randomFailure=1 order by f.id DESC";

		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		dataQuery.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize);

		return dataQuery.getResultList();
	}

	public static Long getTotalRandomFailures()
	{
		return (Long) EMHelper.em()
													.createQuery("select count(DISTINCT t) from testmethod t, testfailure f where f.testMethod = t and f.randomFailure=1 order by f.id DESC")
													.getSingleResult();
	}

	@JsonIgnore
	public List<TestFailure> getTestFailures(int pageNumber, int pageSize)
	{
		String genericQueryPart = "select f from testfailure f where f.testMethod = :testMethod order by f.id DESC";

		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		dataQuery.setParameter("testMethod", this);
		dataQuery.setFirstResult((pageNumber - 1) * pageSize).setMaxResults(pageSize);
		return dataQuery.getResultList();
	}

	public Long getTestFailuresCount()
	{
		String genericQueryPart = "select count(f) from testfailure f where f.testMethod = :testMethod";
		Query dataQuery = EMHelper.em().createQuery(genericQueryPart);
		dataQuery.setParameter("testMethod", this);
		return (Long) dataQuery.getSingleResult();
	}
}
