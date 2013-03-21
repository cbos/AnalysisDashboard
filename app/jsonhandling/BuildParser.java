package jsonhandling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.JsonNode;

public class BuildParser extends RunParser
{
	public BuildParser(final JsonNode node)
	{
		super(node);
	}

	public boolean hasRuns()
	{
		return !path("runs").isMissingNode();
	}

	public List<RunParser> getRuns(final JsonReader reader)
	{
		if (hasRuns())
		{
			ArrayList<RunParser> runs = new ArrayList<>();
			JsonNode runsNode = path("runs");
			for (JsonNode runNode : runsNode)
			{
				runs.add(new RunParser(reader.getJSonResult(runNode.path("url").asText())));
			}
			return runs;
		}
		return Collections.emptyList();
	}

	@Override
	public TestReportParser loadTestReport(final JsonReader reader)
	{
		if (hasRuns())
		{
			throw new IllegalStateException("Get the testReport by querying the runs");
		}
		return super.loadTestReport(reader);
	}
}
