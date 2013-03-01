package controllers.data;

import java.util.Date;

import model.task.Task;

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
		}
		entity.setUpdateDate(now);
	}

}
