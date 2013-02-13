package analysis;

import jsonhandling.JsonReader;

import org.codehaus.jackson.JsonNode;

import play.Logger;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;

public class JsonReaderImpl implements JsonReader
{

	@Override
	public JsonNode getJSonResult(final String url)
	{
		String enhancedURL = enhanceURL(url);
		Logger.of(JsonReaderImpl.class).info("Loading url " + enhancedURL);
		try
		{
			Promise<Response> promise = WS.url(enhancedURL).get();
			Response response = promise.get(60000L);

			if (response.getStatus() == 200)
			{
				return response.asJson();
			}

			throw new RuntimeException(String.format("Requested url '%s' failed with status %s. %s",
																							 enhancedURL,
																							 response.getStatus(),
																							 response.getBody()));
		}
		catch (Throwable t)
		{
			throw new RuntimeException("Error during fetch of url " + enhancedURL, t);
		}
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
