package controllers.data;

import model.jenkins.Computer;

public class ComputerController extends EntityController<Computer>
{
	public ComputerController()
	{
		super(Computer.class);
	}

	@Override
	protected void validateEntity(final Long id, final Computer entity)
	{
		Computer computerFromDB = getEntityById(id);

		entity.setJenkinsServer(computerFromDB.getJenkinsServer());
	}
}
