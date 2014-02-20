package model.analysis;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "testfailure")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestFailure extends Failure
{

	@ManyToOne(optional = true, fetch = FetchType.EAGER, targetEntity = TestMethod.class)
	@JoinColumn(name = "testMethod_id", nullable = true, updatable = true, insertable = true)
	private TestMethod testMethod = null;

	private long age;

	@Lob
	private String errorDetails;

	@JsonIgnore
	public TestMethod getTestMethod()
	{
		return testMethod;
	}

	public void setTestMethod(final TestMethod testMethod)
	{
		this.testMethod = testMethod;
	}

	public String getTestMethodName()
	{
		String methodName = testMethod.getMethodName();
		TestClass testClass = testMethod.getTestClass();
		String className = testClass.getClassName();
		return String.format("%s.%s", className, methodName);
	}

	public String getUrl()
	{
		String methodName = getTestMethodNameForURL();

		TestClass testClass = testMethod.getTestClass();
		String fullClassName = testClass.getClassName();

		String namespace = "(root)"; //default for Jenkins, when there is no namespace
		String className = fullClassName;

		int lastIndexOf = fullClassName.lastIndexOf('.');
		if (lastIndexOf > 0)
		{
			namespace = fullClassName.substring(0, lastIndexOf);
			className = fullClassName.substring(lastIndexOf + 1);
		}
		return String.format("%s/testReport/junit/%s/%s/%s", getBuild().getUrl(), namespace, className, methodName);
	}

	private String getTestMethodNameForURL()
	{
		char[] specialCharacters = { ' ', '.', '[', ']', ',', '=' };

		String methodName = testMethod.getMethodName();
		if (methodName != null)
		{
			for (char c : specialCharacters)
			{
				methodName = methodName.replace(c, '_');
			}
		}
		return methodName;
	}

	public long getAge()
	{
		return age;
	}

	public void setAge(final long age)
	{
		this.age = age;
	}

	public String getErrorDetails()
	{
		return errorDetails;
	}

	public void setErrorDetails(final String errorDetails)
	{
		this.errorDetails = errorDetails;
	}
}
