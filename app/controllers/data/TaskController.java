package controllers.data;

import java.util.Date;

import model.task.JobTask;
import model.task.Task;
import play.db.jpa.Transactional;
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
			updateJobTask(entity, null);
			sendUpdateEmail(entity);
		}
		else
		{
			Task taskFromDB = getEntityById(id);
			updateJobTask(entity, taskFromDB);
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

	private void updateJobTask(final Task entityToUpdate, final Task taskFromDB)
	{
		if (entityToUpdate instanceof JobTask)
		{
			updateJobTaskRelatedJobs((JobTask) entityToUpdate, (JobTask) taskFromDB);
		}
	}

	private void updateJobTaskRelatedJobs(final JobTask entityToUpdate, final JobTask taskFromDB)
	{
		if (taskFromDB != null)
		{
			entityToUpdate._setJobs(taskFromDB._getJobs());
		}
		entityToUpdate.updateRelatedJobs();
	}

	private void sendUpdateEmail(final Task task)
	{
		if (task.getAssignee() != null)
		{
			new TaskUpdateMailer(task, "http://" + request().host()).sendMail();
		}
	}

	@Transactional(readOnly = true)
	public Result getTodaysList()
	{
		return ok(toJson(Task.getTodaysList()));
	}
}