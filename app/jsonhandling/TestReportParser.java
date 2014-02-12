package jsonhandling;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class TestReportParser extends BaseParser
{
	public TestReportParser(final JsonNode node)
	{
		super(node);
	}

	public long getFailedTestCount()
	{
		return path("failCount").asLong();
	}

	public List<TestCase> getAllTestCases()
	{
		List<TestCase> testcases = new ArrayList<>();

		JsonNode suites = path("suites");
		for (JsonNode suite : suites)
		{
			JsonNode testcaseNodes = suite.path("cases");
			for (JsonNode testcaseNode : testcaseNodes)
			{
				testcases.add(new TestCase(testcaseNode));
			}
		}
		return testcases;
	}

	public List<TestCase> getFailingTestCases()
	{
		List<TestCase> failingTestCases = new ArrayList<>();
		List<TestCase> allTestCases = getAllTestCases();
		for (TestCase testCase : allTestCases)
		{
			if (testCase.isFailed())
			{
				failingTestCases.add(testCase);
			}
		}
		return failingTestCases;
	}

	public static class TestCase extends BaseParser
	{
		//		{
		//      "age" : 1,
		//      "className" : "com.cordys.cws.uiunit.preparation.StartupCuspTestCase",
		//      "duration" : 44.204,
		//      "errorDetails" : "Error(s) left open, which should not happen. Check the console output for more details about the open errors.",
		//      "errorStackTrace" : "... java.lang.Thread.run(Thread.java:722)\n",
		//      "failedSince" : 1885,
		//      "name" : "cleanupWorkspace",
		//      "skipped" : false,
		//      "status" : "REGRESSION",
		//      "stderr" : "",
		//      "stdout" : "... Browser will stop\n"
		//    }

		public TestCase(final JsonNode node)
		{
			super(node);
		}

		public long getAge()
		{
			return path("age").asLong();
		}

		public String getClassName()
		{
			return path("className").asText();
		}

		public String getErrorDetails()
		{
			return path("errorDetails").asText();
		}

		public String getErrorStackTrace()
		{
			return path("errorStackTrace").asText();
		}

		public String getMethodName()
		{
			return path("name").asText();
		}

		public boolean isSkipped()
		{
			return path("skipped").asBoolean();
		}

		public boolean isFailed()
		{
			String status = path("status").asText();
			return "REGRESSION".equals(status) || "FAILED".equals(status);
		}
	}
}
