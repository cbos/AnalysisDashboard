package controllers.data;

import model.jenkins.Job;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

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

	@Transactional(readOnly = true)
	public Result getUnstableJobs()
	{
		return ok(Json.toJson(Job.getUnstableJobs()));
	}
}
