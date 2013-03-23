package analysis.listener;

import java.util.Date;

import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;
import analysis.executor.AnalysisExecutor;

public class WebSocketAnalysisListener extends WebSocket<String> implements AnalysisListener
{
	private Out<String> webSocketOut = null;

	private static String JSONRespons = "{\"lastExecution\":%s, \"isExecuting\":%s, \"isSuccessful\":%s }";

	@Override
	public void startAnalysis()
	{
		writeToWebSocket(true);
	}

	private void writeToWebSocket(final boolean isAnalyzing)
	{
		if (webSocketOut != null)
		{
			String executionDate = "";
			AnalysisExecutor analysisExecutor = AnalysisExecutor.getInstance();
			Date lastExecution = analysisExecutor.lastExecution();
			if (lastExecution != null)
			{
				executionDate = lastExecution.getTime() + "";
			}
			webSocketOut.write(String.format(JSONRespons, executionDate, isAnalyzing, analysisExecutor.isSuccessful()));
		}
	}

	@Override
	public void endAnalysis()
	{
		writeToWebSocket(false);
	}

	@Override
	public void onReady(final In<String> in, final Out<String> out)
	{

		final AnalysisListener listener = this;

		// For each event received on the socket,
		in.onMessage(new Callback<String>()
		{
			@Override
			public void invoke(final String event)
			{
				// incoming messages
			}
		});

		// When the socket is closed.
		in.onClose(new Callback0()
		{
			@Override
			public void invoke()
			{
				AnalysisExecutor.getInstance().unregisterListener(listener);
			}
		});

		webSocketOut = out;
		AnalysisExecutor.getInstance().registerListener(listener);
		writeToWebSocket(false);
	}
}
