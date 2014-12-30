package jsonhandling;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class BaseParser
{
	private JsonNode m_node;

	protected BaseParser(final JsonNode node)
	{
		if (node == null)
		{
			throw new IllegalArgumentException("JsonNode is not allowed to be null");
		}
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

	protected void replaceNode(final JsonNode node)
	{
		m_node = node;
	}
}
