package controllers.data;

import model.analysis.Failure;
import model.analysis.TestFailure;

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
}
