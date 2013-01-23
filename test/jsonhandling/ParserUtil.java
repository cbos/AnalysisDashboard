package jsonhandling;

import java.io.IOException;
import java.io.InputStream;

import jsonhandling.computerparser.ComputerParserTest;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class ParserUtil
{

	public static JsonNode parseJsonFile(Object testInstance, String fileName) throws IOException
	{
		String packageName = testInstance.getClass().getPackage().getName().replace('.', '/');
		try (InputStream input = ComputerParserTest.class.getResourceAsStream("/" + packageName + "/" + fileName))
		{
			return new ObjectMapper().readTree(input);
		}
	}
}
