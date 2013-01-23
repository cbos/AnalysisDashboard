package jsonhandling;

import org.codehaus.jackson.JsonNode;

public abstract class BaseParser
{
	private final JsonNode m_node;

	protected BaseParser(final JsonNode node)
	{
		m_node = node;
	}

	protected JsonNode path(final String... fieldNames)
	{
		JsonNode node = m_node;
		for (String fieldname : fieldNames)
		{
			node = node.path(fieldname);
		}
		return node;
	}
}
