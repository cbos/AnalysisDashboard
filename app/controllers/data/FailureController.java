package controllers.data;

import model.analysis.Failure;
import model.analysis.TestFailure;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

import play.db.jpa.Transactional;
import play.mvc.Result;

public class FailureController extends EntityController<Failure>
{
	public FailureController()
	{
		super(Failure.class);
	}

	@Override
	protected void validateEntity(final Long id, final Failure failure)
	{

		Failure failureFromDB = getEntityById(id);

		failure.setBuild(failureFromDB.getBuild());

		if (failure instanceof TestFailure)
		{
			((TestFailure) failure).setTestMethod(((TestFailure) failureFromDB).getTestMethod());
		}
	}
	
	@Transactional(readOnly = true)
	public Result getRandomList(final Integer id)
	{
		int pageSize = 25;
		
		Long total = Failure.getTotalRandomFailures();
		
		ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
		objectNode.put("pageSize", pageSize);
		objectNode.put("totalRandomFailures", total);
		objectNode.put("totalPages", Double.valueOf(Math.ceil(total / (double)pageSize)).intValue());
		objectNode.put("failures", toJson(Failure.getRandomFailures(Math.max(1, id), pageSize)));
		
		return ok(objectNode);
	}
}
