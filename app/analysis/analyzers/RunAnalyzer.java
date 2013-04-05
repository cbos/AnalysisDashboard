package analysis.analyzers;

import java.util.List;
import java.util.Set;

import jsonhandling.BuildParser;
import jsonhandling.JsonReader;
import jsonhandling.RunParser;
import jsonhandling.TestReportParser;
import jsonhandling.TestReportParser.TestCase;
import model.EntityHelper;
import model.analysis.Failure;
import model.analysis.TestClass;
import model.analysis.TestFailure;
import model.analysis.TestMethod;
import model.jenkins.Build;
import model.jenkins.Job;

public class RunAnalyzer
{

	private final Job m_job;

	private final RunParser m_buildParser;

	private final JsonReader m_jsonReader;

	public RunAnalyzer(final Job job, final RunParser buildParser, final JsonReader jsonReader)
	{
		m_job = job;
		m_buildParser = buildParser;
		m_jsonReader = jsonReader;
	}

	public Build analyze()
	{
		Build build = new Build();
		build.setBuildNumber(m_buildParser.getBuildNumber());

		build.setStatus(m_buildParser.getStatus());
		build.setTimestamp(m_buildParser.getTimestamp());
		build.setJob(m_job);
		build.setUrl(m_buildParser.getUrl());
		setDisplayName(build);
		EntityHelper.persist(build);

		switch (m_buildParser.getStatus())
		{
		case FAILED:
		case UNSTABLE:
			analyzeDetails(build);
			break;
		case ABORTED:
		case STABLE:
		default:
			break;
		}
		return build;
	}

	private void setDisplayName(final Build build)
	{
		String fullDisplayName = m_buildParser.getFullDisplayName();

		//rootbuilds have a name like this: cws-wip-uiunit #1885
		if (fullDisplayName.indexOf('#') > 0)
		{
			fullDisplayName = fullDisplayName.split("#")[0].trim();
		}

		//Runs have names like this "cws-wip-uiunit » 64,Safari,oraclejdk-1.7.3 64,Safari,oraclejdk-1.7.3"
		if (fullDisplayName.indexOf('»') > 0)
		{
			fullDisplayName = fullDisplayName.split("»")[1].trim();
		}
		build.setDisplayName(fullDisplayName);
	}

	private void analyzeDetails(final Build build)
	{
		if (m_buildParser instanceof BuildParser && ((BuildParser) m_buildParser).hasRuns())
		{
			//There are runs, so these contain the details of the failure
			//Do nothing here
		}
		else
		{
			if (m_buildParser.hasTestResults())
			{
				TestReportParser testReportParser = m_buildParser.loadTestReport(m_jsonReader);
				List<TestCase> failingTestCases = testReportParser.getFailingTestCases();
				for (TestCase testCase : failingTestCases)
				{
					TestClass testClass = getOrCreateTestClass(testCase.getClassName());
					TestMethod testMethod = getOrCreateMethod(testClass, testCase.getMethodName());

					TestFailure testFailure = new TestFailure();
					testFailure.setTestMethod(testMethod);
					testFailure.setAge(testCase.getAge());
					testFailure.setErrorDetails(testCase.getErrorDetails());
					testFailure.setBuild(build);
					EntityHelper.persist(testFailure);
				}
			}
			else
				if (m_buildParser.getDescription() != null && !"".equals(m_buildParser.getDescription()))
				{
					Failure failure = new Failure();
					failure.setSummary(m_buildParser.getDescription());
					failure.setBuild(build);
					EntityHelper.persist(failure);
				}
				else
				{
					Failure failure = new Failure();
					failure.setSummary("Unknown reason of failure");
					failure.setBuild(build);
					EntityHelper.persist(failure);
				}
		}
	}

	private TestClass getOrCreateTestClass(final String className)
	{
		for (TestClass testClass : EntityHelper.getAll(TestClass.class))
		{
			if (testClass.getClassName().equals(className))
			{
				return testClass;
			}
		}

		TestClass testClass = new TestClass();
		testClass.setClassName(className);
		return EntityHelper.persist(testClass);
	}

	private TestMethod getOrCreateMethod(final TestClass testClass, final String methodName)
	{
		Set<TestMethod> testMethods = testClass.getTestMethods();
		if (testMethods != null)
		{
			for (TestMethod method : testMethods)
			{
				if (method.getMethodName().equals(methodName))
				{
					return method;
				}
			}
		}

		TestMethod method = new TestMethod();
		method.setTestClass(testClass);
		method.setMethodName(methodName);
		return EntityHelper.persist(method);
	}
}
