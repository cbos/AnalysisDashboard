package analysis.analyzers;

import java.util.List;
import java.util.Set;

import jsonhandling.BuildParser;
import jsonhandling.JsonReader;
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

	private final BuildParser m_buildParser;

	private final JsonReader m_jsonReader;

	public RunAnalyzer(final Job job, final BuildParser buildParser, final JsonReader jsonReader)
	{
		m_job = job;
		m_buildParser = buildParser;
		m_jsonReader = jsonReader;
	}

	public void analyze()
	{
		Build build = new Build();
		build.setBuildNumber(m_buildParser.getBuildNumber());

		build.setStatus(m_buildParser.getStatus());
		build.setTimestamp(m_buildParser.getTimestamp());
		build.setJob(m_job);
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

		m_job.setLastBuildNumber(build.getBuildNumber());
		m_job.setLastBuild(build);
	}

	private void analyzeDetails(final Build build)
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
