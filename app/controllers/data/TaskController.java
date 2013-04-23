package controllers.data;

import java.util.Date;

import model.task.Task;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import utils.email.TaskUpdateMailer;

public class TaskController extends EntityController<Task>
{
	public TaskController()
	{
		super(Task.class);
	}

	@Override
	protected void validateEntity(final Long id, final Task entity)
	{
		Date now = new Date();
		if (id == null)
		{
			entity.setCreationDate(now);
			sendUpdateEmail(entity);
		}
		else
		{
			Task taskFromDB = getEntityById(id);
			if (entity.getAssignee() != null
					&& (taskFromDB.getAssignee() == null || !entity.getAssignee()
																												 .getId()
																												 .equals(taskFromDB.getAssignee().getId())))
			{
				sendUpdateEmail(entity);
			}
		}
		entity.setUpdateDate(now);
	}

	private void sendUpdateEmail(final Task task)
	{
		if (task.getAssignee() != null)
		{
			new TaskUpdateMailer(task, "http:" + request().host()).sendMail();
		}
	}

	@Transactional(readOnly = true)
	public Result getTodaysList()
	{
		return ok(Json.toJson(Task.getTodaysList()));
	}
}