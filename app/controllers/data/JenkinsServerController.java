package controllers.data;

import java.io.IOException;

import model.EntityHelper;
import model.jenkins.JenkinsServer;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;

public class JenkinsServerController extends EntityController
{

	@Transactional(readOnly = true)
	public static Result jenkinsServer(final Long id)
	{
		return entity(JenkinsServer.class, id);
	}

	@Transactional()
	public static Result newInstance() throws JsonParseException, JsonMappingException, IOException
	{
		JsonNode json = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		JenkinsServer jenkinsServer = mapper.readValue(json, JenkinsServer.class);
		System.out.println(json);
		System.out.println(jenkinsServer.getName());
		EntityHelper.persist(jenkinsServer);

		return ok(Json.toJson(jenkinsServer));
	}

	@Transactional()
	public static Result save(final Long id) throws JsonParseException, JsonMappingException, IOException
	{
		JsonNode json = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		JenkinsServer jenkinsServer = mapper.readValue(json, JenkinsServer.class);
		System.out.println(json);
		System.out.println(jenkinsServer.getName());
		EntityHelper.persist(jenkinsServer);

		return ok(Json.toJson(jenkinsServer));
	}

	@Transactional()
	public static Result delete(final Long id)
	{
		EntityHelper.getEntityById(JenkinsServer.class, id).delete();
		return ok();
	}

}
