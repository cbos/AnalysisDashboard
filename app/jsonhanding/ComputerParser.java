package jsonhanding;

import java.math.BigDecimal;

import org.codehaus.jackson.JsonNode;

public class ComputerParser
{

	private JsonNode m_node;

	public ComputerParser(JsonNode node)
	{
		m_node = node;
	}

	public String getDisplayName()
	{
		return m_node.path("displayName").asText();
	}

	public boolean isOffline()
	{
		return m_node.path("offline").getBooleanValue();
	}

	public boolean isTemporarilyOffline()
	{
		return m_node.path("temporarilyOffline").getBooleanValue();
	}

	public boolean isOfflineCauseAvailable()
	{
		return !m_node.path("offlineCause").isNull();
	}

	public String getOfflineCause()
	{
		if (isOfflineCauseAvailable())
		{
			return m_node.path("offlineCauseReason").asText();
		}
		return null;
	}

	public BigDecimal getGbLeft()
	{
		JsonNode diskSpaceMonitor = m_node.path("monitorData").path("hudson.node_monitors.DiskSpaceMonitor");
		if (!diskSpaceMonitor.isNull())
		{
			long space = diskSpaceMonitor.path("size").asLong();
			space /= 1024L; // convert to KB
			space /= 1024L; // convert to MB
			return new BigDecimal(space).scaleByPowerOfTen(-3);
		}
		return null;
	}
}
