package jsonhanding;

import org.codehaus.jackson.JsonNode;

public class ViewParser
{
	private JsonNode m_node;

	public ViewParser(JsonNode node)
	{
		m_node = node;
	}
	
	public JsonNode getJobs()
	{
		return m_node.path("jobs");
	}

}
