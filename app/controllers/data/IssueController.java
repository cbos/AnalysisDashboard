package controllers.data;

import java.util.Date;

import model.issue.Issue;

public class IssueController extends EntityController<Issue>
{
	public IssueController()
	{
		super(Issue.class);
	}

	@Override
	protected void validateEntity(final Long id, final Issue entity)
	{
		Date now = new Date();
		if (id == null)
		{
			entity.setCreationDate(now);
		}
		entity.setUpdateDate(now);
	}
}