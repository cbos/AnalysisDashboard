import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import model.EntityHelper;
import model.jenkins.JenkinsServer;

import org.junit.Test;

import play.db.jpa.JPA;

public class JSONTest
{

	@Test
	public void findById()
	{
		running(fakeApplication(), new Runnable()
		{
			@Override
			public void run()
			{
				JPA.withTransaction(new play.libs.F.Callback0()
				{
					@Override
					public void invoke()
					{
						JenkinsServer buildmasterNL = new JenkinsServer();
						buildmasterNL.setName("buildmaster-nl");
						buildmasterNL.setUrl("http://buildmaster-nl/jenkins");
						buildmasterNL.setViewsToAnalyze("CWS Cluster%20and%20Upgrade Auto%20Ugrades Auto%20(Un)Install Loadtests");
						buildmasterNL.setLabelsToAnalyze("CWS RnDNL");
						EntityHelper.persist(buildmasterNL);

						JenkinsServer buildmasterHyd = new JenkinsServer();
						buildmasterHyd.setName("buildmaster-hyd");
						buildmasterHyd.setUrl("http://buildmaster-hyd/jenkins");
						buildmasterHyd.setViewsToAnalyze("CWS");
						buildmasterHyd.setLabelsToAnalyze("CWS CWS4.2");
						EntityHelper.persist(buildmasterHyd);
					}
				});
			}
		});
	}
}
