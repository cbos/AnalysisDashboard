package analysis.executor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import model.EntityHelper;
import model.jenkins.JenkinsServer;
import play.Logger;
import analysis.JsonReaderImpl;
import analysis.analyzers.JenkinsServerAnalyzer;
import analysis.listener.AnalysisListener;

public class AnalysisExecutor
{
	private static AnalysisExecutor singletonExecutor = new AnalysisExecutor();

	private final Map<AnalysisListener, AnalysisListener> listeners = new ConcurrentHashMap<>();

	private Date lastExecutionDate = null;

	private boolean lastSuccessful = true;

	private AnalysisExecutor()
	{

	}

	public static AnalysisExecutor getInstance()
	{
		return singletonExecutor;
	}

	public Date lastExecution()
	{
		return lastExecutionDate;
	}

	public boolean isSuccessful()
	{
		return lastSuccessful;
	}

	public void markSuccessful()
	{
		if (!lastSuccessful)
		{
			lastSuccessful = true;
			notifyListenerOnEnd();
		}
	}

	public void markFailed()
	{
		lastSuccessful = false;
		notifyListenerOnEnd();
	}

	public void executeAnalysis()
	{
		lastExecutionDate = new Date();

		notifyListenerOnStart();

		try
		{
			List<JenkinsServer> all = EntityHelper.getAll(JenkinsServer.class);
			for (JenkinsServer jenkinsServer : all)
			{
				analyzeServer(jenkinsServer);
			}
		}
		finally
		{
			notifyListenerOnEnd();
		}
	}

	private void notifyListenerOnEnd()
	{
		for (AnalysisListener listener : listeners.keySet())
		{
			listener.endAnalysis();
		}
	}

	private void notifyListenerOnStart()
	{
		for (AnalysisListener listener : listeners.keySet())
		{
			listener.startAnalysis();
		}
	}

	public synchronized void analyzeServer(final JenkinsServer jenkinsServer)
	{
		JenkinsServerAnalyzer jenkinsServerAnalyzer = new JenkinsServerAnalyzer(jenkinsServer, new JsonReaderImpl());
		Logger.debug("Analyzing computers");
		jenkinsServerAnalyzer.analyzeComputers();
		Logger.debug("Analyzing views");
		jenkinsServerAnalyzer.analyzeViews();
		Logger.debug("Done with the analysis");
		Logger.debug("Ready");
	}

	public void registerListener(final AnalysisListener newListener)
	{
		listeners.put(newListener, newListener);
	}

	public void unregisterListener(final AnalysisListener listener)
	{
		listeners.remove(listener);
	}
}
