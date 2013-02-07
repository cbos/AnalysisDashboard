package controllers.data;

import java.io.IOException;

import model.EntityBase;
import model.EntityHelper;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class EntityController<T extends EntityBase> extends Controller
{
	private final Class<T> m_clazz;

	public EntityController(final Class<T> clazz)
	{
		m_clazz = clazz;
	}

	@Transactional(readOnly = true)
	public Result entity(final Long id)
	{
		if (id > 0)
		{
			return ok(Json.toJson(EntityHelper.getEntityById(m_clazz, id)));
		}
		return ok(Json.toJson(EntityHelper.getAll(m_clazz)));
	}

	@Transactional()
	public Result newInstance() throws JsonParseException, JsonMappingException, IOException
	{
		JsonNode json = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		T entity = mapper.readValue(json, m_clazz);
		System.out.println(json);
		EntityHelper.persist(entity);

		return ok(Json.toJson(entity));
	}

	@Transactional()
	public Result save(final Long id) throws JsonParseException, JsonMappingException, IOException
	{
		JsonNode json = request().body().asJson();
		ObjectMapper mapper = new ObjectMapper();
		T entity = mapper.readValue(json, m_clazz);
		System.out.println(json);
		EntityHelper.persist(entity);

		return ok(Json.toJson(entity));
	}

	@Transactional()
	public Result delete(final Long id)
	{
		EntityHelper.getEntityById(m_clazz, id).delete();
		return ok();
	}
}
