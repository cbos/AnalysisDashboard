package jsonhandling.computerparser;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import jsonhanding.ComputerParser;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.Test;

public class ComputerParserTest
{

	@Test
	public void testOnlineComputer() throws IOException
	{
		ComputerParser cp = parseFile("onlineComputer.json");
		assertFalse(cp.isOffline());
		assertFalse(cp.isTemporarilyOffline());
		assertFalse(cp.isOfflineCauseAvailable());
		assertThat(cp.getOfflineCause(), nullValue());
		assertThat(cp.getGbLeft(), equalTo(BigDecimal.valueOf(14.129)));
	}

	@Test
	public void testOfflineComputer() throws IOException
	{
		ComputerParser cp = parseFile("offlineComputer.json");
		assertTrue(cp.isOffline());
		assertTrue(cp.isTemporarilyOffline());
		assertTrue(cp.isOfflineCauseAvailable());
		assertThat(cp.getOfflineCause(), IsNull.notNullValue());
		assertThat(cp.getGbLeft(), equalTo(BigDecimal.valueOf(0.095)));
	}

	@Test
	public void testOfflineComputerBecauseOfConnectionFailure() throws IOException
	{
		ComputerParser cp = parseFile("offlineComputerBecauseOfConnectionFailure.json");
		assertTrue(cp.isOffline());
		assertFalse(cp.isTemporarilyOffline());
		assertTrue(cp.isOfflineCauseAvailable());
		assertThat(cp.getOfflineCause(), IsNull.notNullValue());
		assertThat(cp.getGbLeft(), IsNull.nullValue());
	}

	@Test
	public void testOfflineComputerBecauseOfChannelIssue() throws IOException
	{
		ComputerParser cp = parseFile("offlineComputerChannelIssue.json");
		assertTrue(cp.isOffline());
		assertFalse(cp.isTemporarilyOffline());
		assertTrue(cp.isOfflineCauseAvailable());
		assertThat(cp.getOfflineCause(), IsNull.notNullValue());
		assertThat(cp.getGbLeft(), IsNull.nullValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void allNodes() throws IOException
	{
		try (InputStream input = ComputerParserTest.class.getResourceAsStream("allNodes.json"))
		{
			JsonNode rootNode = new ObjectMapper().readTree(input);
			JsonNode computers = rootNode.path("computer");
			for (JsonNode computerNode : computers)
			{
				ComputerParser computer = new ComputerParser(computerNode);
				if (computer.isOffline())
				{
					assertThat(computer.getDisplayName(), anyOf(equalTo("lab1014.cordyslab.com"),
					                                            equalTo("srv-nl-crd146"),
					                                            equalTo("srv-nl-crd146template"),
					                                            equalTo("srv-nl-crd38"),
					                                            equalTo("srv-nl-vdt010")));
				}
			}
		}
	}

	private ComputerParser parseFile(String fileName) throws IOException
	{
		try (InputStream input = ComputerParserTest.class.getResourceAsStream(fileName))
		{
			JsonNode rootNode = new ObjectMapper().readTree(input);
			return new ComputerParser(rootNode);
		}
	}
}
