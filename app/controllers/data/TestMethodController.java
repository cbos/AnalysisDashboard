package controllers.data;

import java.util.List;

import model.EntityHelper;
import model.analysis.TestFailure;
import model.analysis.TestMethod;
import model.jenkins.Build;
import play.db.jpa.Transactional;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TestMethodController extends EntityController<TestMethod>
{
	public TestMethodController()
	{
		super(TestMethod.class);
	}

	@Override
	protected void validateEntity(final Long id, final TestMethod testMethod)
	{
		TestMethod testMethodFromDB = getEntityById(id);

		testMethod.setTestClass(testMethodFromDB.getTestClass());
	}

	@Transactional(readOnly = true)
	public Result forFailure(final Long failureId)
	{
		TestFailure testFailure = EntityHelper.getEntityById(TestFailure.class, failureId);

		return ok(testMethodToJson(testFailure.getTestMethod()));
	}

	@Transactional(readOnly = true)
	public Result getTestFailures(final Long testMethodID, final Integer id)
	{
		int pageSize = 20;

		TestMethod testMethod = getEntityById(testMethodID);

		Long total = testMethod.getTestFailuresCount();

		ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
		objectNode.put("pageSize", pageSize);
		objectNode.put("totalFailures", total);
		objectNode.put("totalPages", Double.valueOf(Math.ceil(total / (double) pageSize)).intValue());
		objectNode.put("failures", prepareAllFailures(testMethod.getTestFailures(Math.max(1, id), pageSize)));
		return ok(objectNode);
	}

	private JsonNode prepareAllFailures(List<TestFailure> testMethodFailures)
	{
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

		for (TestFailure testFailure : testMethodFailures)
		{
			ObjectNode testMethodJson = (ObjectNode) toJson(testFailure);

			testMethodJson.put("timestamp", testFailure.getBuild().getTimestamp());
			testMethodJson.put("jobName", testFailure.getBuild().getJob().getName());
			testMethodJson.put("runName", getRunName(testFailure));
			arrayNode.add(testMethodJson);
		}
		return arrayNode;
	}

	private String getRunName(TestFailure failure)
	{
		Build build = failure.getBuild();
		if (build.getParentBuild() == null)
		{
			return "";
		}
		return build.getDisplayName();
	}

	@Transactional(readOnly = true)
	public Result getRandomList(final Integer id)
	{
		int pageSize = 25;

		Long total = TestMethod.getTotalRandomFailures();

		ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
		objectNode.put("pageSize", pageSize);
		objectNode.put("totalRandomFailures", total);
		objectNode.put("totalPages", Double.valueOf(Math.ceil(total / (double) pageSize)).intValue());
		objectNode.put("failures", prepareRandomFailures(TestMethod.getRandomFailures(Math.max(1, id), pageSize)));

		return ok(objectNode);
	}

	private JsonNode prepareRandomFailures(List<TestMethod> testmethods)
	{
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

		for (TestMethod testMethod : testmethods)
		{
			ObjectNode testMethodJson = testMethodToJson(testMethod);
			arrayNode.add(testMethodJson);
		}
		return arrayNode;
	}

	private ObjectNode testMethodToJson(TestMethod testMethod)
	{
		ObjectNode testMethodJson = (ObjectNode) toJson(testMethod);
		testMethodJson.put("className", testMethod.getTestClass().getClassName());
		return testMethodJson;
	}
}
