package jsonhandling;

import java.math.BigDecimal;

import org.codehaus.jackson.JsonNode;

public class ComputerParser extends BaseParser
{

	public ComputerParser(final JsonNode node)
	{
		super(node);
	}

	public String getDisplayName()
	{
		return path("displayName").asText();
	}

	public boolean isOffline()
	{
		return path("offline").getBooleanValue();
	}

	public boolean isTemporarilyOffline()
	{
		return path("temporarilyOffline").getBooleanValue();
	}

	public boolean isOfflineCauseAvailable()
	{
		return !path("offlineCause").isNull();
	}

	public String getOfflineCause()
	{
		if (isOfflineCauseAvailable())
		{
			return path("offlineCauseReason").asText();
		}
		return null;
	}

	public BigDecimal getGbLeft()
	{
		JsonNode diskSpaceMonitor = path("monitorData", "hudson.node_monitors.DiskSpaceMonitor");
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
