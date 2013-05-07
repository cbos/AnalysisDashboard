package controllers.data;

import java.util.Date;

import model.issue.Issue;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

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

	@Transactional(readOnly = true)
	public Result getTodaysList()
	{
		return ok(Json.toJson(Issue.getTodaysList()));
	}
}