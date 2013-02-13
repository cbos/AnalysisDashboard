package controllers.data;

import model.jenkins.JenkinsServer;
import play.Logger;
import play.db.jpa.Transactional;
import play.mvc.Result;
import analysis.JenkinsServerAnalyzer;
import analysis.JsonReaderImpl;

public class JenkinsServerController extends EntityController<JenkinsServer>
{
	public JenkinsServerController()
	{
		super(JenkinsServer.class);
	}

	@Transactional()
	public Result analyzeServer(final Long id)
	{
		JenkinsServer jenkinsServer = getEntityById(id);

		JenkinsServerAnalyzer jenkinsServerAnalyzer = new JenkinsServerAnalyzer(jenkinsServer, new JsonReaderImpl());
		Logger.of(JenkinsServerController.class).info("Analyzing computers");
		jenkinsServerAnalyzer.analyzeComputers();
		Logger.of(JenkinsServerController.class).info("Analyzing views");
		jenkinsServerAnalyzer.analyzeViews();
		Logger.of(JenkinsServerController.class).info("Done with the analysis");

		Logger.of(JenkinsServerController.class).info("Ready");
		return ok();
	}

	/*public Result analyzeServer(final Long id)
	{
		{
			Promise<Boolean> promise = play.libs.Akka.future(new Callable<Boolean>()
			{
				@Override
				@Transactional()
				public Boolean call()
				{
					try
					{
						return JPA.withTransaction(new F.Function0<Boolean>()
						{
							@Override
							public Boolean apply()
							{
								JenkinsServer jenkinsServer = getEntityById(id);

								JenkinsServerAnalyzer jenkinsServerAnalyzer = new JenkinsServerAnalyzer(jenkinsServer,
																																												new JsonReaderImpl());
								Logger.of(JenkinsServerController.class).info("Analyzing computers");
								jenkinsServerAnalyzer.analyzeComputers();
								Logger.of(JenkinsServerController.class).info("Analyzing views");
								jenkinsServerAnalyzer.analyzeViews();
								Logger.of(JenkinsServerController.class).info("Done with the analysis");
								return true;
							}
						});
					}
					catch (Throwable e)
					{
						Logger.of(JenkinsServerController.class).error("Error during analysis", e);
						throw new RuntimeException(e);
					}
				}
			});
			return async(promise.map(new Function<Boolean, Result>()
			{
				@Override
				public Result apply(final Boolean done)
				{
					Logger.of(JenkinsServerController.class).info("Ready");
					return ok();
				}
			}));
		}
	}*/
}
