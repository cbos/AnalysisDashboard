package controllers.data;

import model.jenkins.JenkinsServer;
import play.db.jpa.Transactional;
import play.mvc.Result;

public class JenkinsServerController extends EntityController
{

	@Transactional(readOnly = true)
	public static Result jenkinsServer(final Long id)
	{
		return entity(JenkinsServer.class, id);
	}
}
