import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import model.EntityHelper;
import model.jenkins.JenkinsServer;

import org.junit.Test;

import play.db.jpa.JPA;
import analysis.JsonReaderImpl;
import analysis.ViewAnalyzer;

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
						JenkinsServer buildmasterNl = EntityHelper.getEntityById(JenkinsServer.class, 1L);

						String viewName = "UIUnit%20Core";
						new ViewAnalyzer(buildmasterNl, viewName, new JsonReaderImpl()).analyze();

						//System.out.println(promise.get().asJson().toString());
					}
				});
			}
		});
	}
}
