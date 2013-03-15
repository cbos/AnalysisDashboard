package controllers.data;

import model.jenkins.Job;

public class JobController extends EntityController<Job>
{
	public JobController()
	{
		super(Job.class);
	}

	@Override
	protected void validateEntity(final Long id, final Job job)
	{
		Job jobFromDB = getEntityById(id);

		job.setJenkinsServer(jobFromDB.getJenkinsServer());
		job.setLastBuild(jobFromDB.getLastBuild());
	}
}
