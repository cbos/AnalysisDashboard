package analysis;

import jsonhandling.JsonReader;

import org.codehaus.jackson.JsonNode;

import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;

public class JsonReaderImpl implements JsonReader
{

	@Override
	public JsonNode getJSonResult(final String url)
	{
		System.out.println(url);
		String enhancedURL = enhanceURL(url);
		System.out.println(enhancedURL);
		Promise<Response> promise = WS.url(enhancedURL).get();
		Response response = promise.get();

		if (response.getStatus() == 200)
		{
			return response.asJson();
		}

		throw new RuntimeException(String.format("Requested url '%s' failed with status %s. %s",
																						 enhancedURL,
																						 response.getStatus(),
																						 response.getBody()));
	}

	private String enhanceURL(final String url)
	{
		if (!url.contains("api/json"))
		{
			return String.format("%s/api/json", url);
		}
		return url;
	}

}
