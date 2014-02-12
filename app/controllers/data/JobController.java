package controllers.data;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.jenkins.Build;
import model.jenkins.Job;
import play.db.jpa.Transactional;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

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
		return ok(toJson(Job.getUnstableJobs()));
	}

	@Transactional(readOnly = true)
	public Result getHistory(final Long id)
	{
		Job jobFromDB = getEntityById(id);

		final Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 0, 0, 0);
		now.add(Calendar.DAY_OF_YEAR, 1);
		Date tomorrow = now.getTime();
		now.add(Calendar.DAY_OF_YEAR, -1);
		now.add(Calendar.MONTH, -12);

		Date dayToHandle = now.getTime();
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
		List<Build> builds = jobFromDB.getBuilds(dayToHandle, tomorrow);

		Iterator<Build> buildsIterator = builds.iterator();
		Build build = null;
		if (buildsIterator.hasNext())
		{
			build = buildsIterator.next();
		}

		while (dayToHandle.before(tomorrow))
		{
			now.add(Calendar.DAY_OF_YEAR, 1);
			Date nextDay = now.getTime();
			ArrayNode arrayNodePerDay = arrayNode.addArray();
			arrayNodePerDay.add(String.format("%tY/%tm/%td", dayToHandle, dayToHandle, dayToHandle));

			DayStats stats = new DayStats();
			boolean buildInDateRange = true;
			while ((build != null) && buildInDateRange)
			{
				Date dateOfBuild = new Date(build.getTimestamp());
				if (dateOfBuild.after(dayToHandle) && dateOfBuild.before(nextDay))
				{
					handleBuild(build, stats);

					if (buildsIterator.hasNext())
					{
						build = buildsIterator.next();
					}
					else
					{
						build = null;
					}
				}
				else
				{
					buildInDateRange = false;
				}
			}
			arrayNodePerDay.add(toJson(stats));
			dayToHandle = nextDay;
		}
		return ok(arrayNode);
	}

	private void handleBuild(Build build, DayStats stats)
	{
		switch (build.getStatus())
		{
		case ABORTED:
			stats.aborted++;
			break;
		case FAILED:
			stats.failed++;
			break;
		case UNSTABLE:
			stats.unstable++;
			break;
		case STABLE:
			stats.stable++;
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unused")
	private class DayStats
	{
		public int failed = 0;

		public int aborted = 0;

		public int stable = 0;

		public int unstable = 0;
	}
}
