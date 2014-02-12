package analysis;

import jsonhandling.JsonReader;
import play.Logger;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonReaderImpl implements JsonReader
{

	@Override
	public JsonNode getJSonResult(final String url)
	{
		String enhancedURL = enhanceURL(url);
		Logger.debug("Loading url " + enhancedURL);
		try
		{
			WSRequestHolder requestHolder = WS.url(enhancedURL);
			if (enhancedURL.contains("?"))
			{
				String parameters = enhancedURL.split("\\?")[1];

				String[] parameter = parameters.split(",");
				for (String keyValuePair : parameter)
				{
					String[] keyValuePairArray = keyValuePair.split("=");
					requestHolder.setQueryParameter(keyValuePairArray[0], keyValuePairArray[1]);
				}
			}

			requestHolder.setTimeout(600000);
			Promise<Response> promise = requestHolder.get();
			Response response = promise.get(600000L);

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
