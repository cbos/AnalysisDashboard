import java.util.concurrent.TimeUnit;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;
import play.libs.Akka;
import play.libs.F.Callback0;
import scala.concurrent.duration.Duration;
import analysis.executor.AnalysisExecutor;

public class Global extends GlobalSettings
{

	@Override
	public void onStart(final Application app)
	{
		Logger.info("Application has started");

		Akka.system().scheduler().schedule(Duration.create(5, TimeUnit.SECONDS), //Initial delay
																			 Duration.create(2, TimeUnit.MINUTES), //Frequency
																			 new Runnable()
																			 {
																				 @Override
																				 public void run()
																				 {
																					 try
																					 {
																						 JPA.withTransaction(new Callback0()
																						 {
																							 @Override
																							 public void invoke() throws Throwable
																							 {
																								 AnalysisExecutor.getInstance().executeAnalysis();
																							 }
																						 });
																						 AnalysisExecutor.getInstance().markSuccessful();
																					 }
																					 catch (Exception e)
																					 {
																						 AnalysisExecutor.getInstance().markFailed();
																						 Logger.error("Error during scheduled task", e);
																					 }
																				 }
																			 },
																			 Akka.system().dispatcher());
	}

	@Override
	public void onStop(final Application app)
	{
		Logger.info("Application shutdown...");
	}

}