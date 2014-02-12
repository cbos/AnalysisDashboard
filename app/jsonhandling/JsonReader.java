package jsonhandling;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonReader
{
	JsonNode getJSonResult(String url);
}
