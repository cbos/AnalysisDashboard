package controllers.data;

import model.jenkins.JenkinsServer;
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

		new JenkinsServerAnalyzer(jenkinsServer, new JsonReaderImpl()).analyzeComputers();
		return ok();
	}

}
