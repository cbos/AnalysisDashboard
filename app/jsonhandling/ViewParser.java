package jsonhandling;

import org.codehaus.jackson.JsonNode;

public class ViewParser extends BaseParser
{
	public ViewParser(final JsonNode node)
	{
		super(node);
	}

	public JsonNode getJobs()
	{
		return path("jobs");
	}
}
