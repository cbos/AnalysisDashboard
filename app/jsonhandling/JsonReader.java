package jsonhandling;

import org.codehaus.jackson.JsonNode;

public interface JsonReader
{
	JsonNode getJSonResult(String url);
}
