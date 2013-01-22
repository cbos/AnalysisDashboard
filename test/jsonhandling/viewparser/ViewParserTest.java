package jsonhandling.viewparser;

import java.io.IOException;
import java.io.InputStream;

import jsonhandling.ViewParser;
import jsonhandling.computerparser.ComputerParserTest;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class ViewParserTest
{

	private ViewParser parseFile(final String fileName) throws IOException
	{

		try (InputStream input = ComputerParserTest.class.getResourceAsStream(fileName))
		{
			JsonNode rootNode = new ObjectMapper().readTree(input);
			return new ViewParser(rootNode);
		}
	}

}
