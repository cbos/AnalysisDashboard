package analysis.analyzers;

import java.util.HashSet;
import java.util.Set;

import jsonhandling.ComputerParser;
import jsonhandling.JsonReader;
import model.jenkins.JenkinsServer;

import org.codehaus.jackson.JsonNode;

public class JenkinsServerAnalyzer
{
	private final JenkinsServer m_jenkinsServer;

	private final JsonReader m_jsonReader;

	public JenkinsServerAnalyzer(final JenkinsServer jenkinsServer, final JsonReader jsonReader)
	{
		m_jenkinsServer = jenkinsServer;
		m_jsonReader = jsonReader;
	}

	public void analyzeComputers()
	{
		String labelsToAnalyze = m_jenkinsServer.getLabelsToAnalyze();
		if (labelsToAnalyze != null)
		{
			String[] labels = labelsToAnalyze.split(" ");

			Set<String> computersToAnalyze = new HashSet<>();

			for (String label : labels)
			{
				String url = String.format("%s/label/%s/api/json", m_jenkinsServer.getUrl(), label);

				JsonNode labelNode = m_jsonReader.getJSonResult(url);

				JsonNode nodes = labelNode.path("nodes");
				for (JsonNode node : nodes)
				{
					computersToAnalyze.add(node.path("nodeName").asText());
				}
			}

			JsonNode computersNode = m_jsonReader.getJSonResult(String.format("%s/computer/api/json",
																																				m_jenkinsServer.getUrl()));

			JsonNode computers = computersNode.path("computer");
			for (JsonNode computerNode : computers)
			{
				ComputerParser computer = new ComputerParser(computerNode);
				if (computersToAnalyze.contains(computer.getDisplayName()))
				{
					new ComputerAnalyzer(m_jenkinsServer, computer).analyze();
				}
			}
		}
	}

	public void analyzeViews()
	{
		String viewsToAnalyze = m_jenkinsServer.getViewsToAnalyze();
		Set<String> jobsAnalyzed = new HashSet<>();
		if (viewsToAnalyze != null)
		{
			String[] views = viewsToAnalyze.split(" ");

			for (String view : views)
			{
				new ViewAnalyzer(m_jenkinsServer, view, m_jsonReader, jobsAnalyzed).analyze();
			}
		}
	}
}
