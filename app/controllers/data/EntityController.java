package controllers.data;

import java.io.IOException;
import java.util.Collection;

import model.EntityBase;
import model.EntityHelper;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class EntityController<T extends EntityBase> extends Controller
{
	private final Class<T> m_clazz;

	public EntityController(final Class<T> clazz)
	{
		m_clazz = clazz;
	}

	protected T getEntityById(final Long id)
	{
		return EntityHelper.getEntityById(m_clazz, id);
	}

	@Transactional(readOnly = true)
	public Result entity(final Long id)
	{
		if (id > 0)
		{
			return ok(toJson(EntityHelper.getEntityById(m_clazz, id)));
		}
		return ok(toJson(EntityHelper.getAll(m_clazz)));
	}

	protected JsonNode toJson(final Object result)
	{
		//This is to fix an issue in Json.toJson
		//Entities with @JsonTypeInfo @JsonSubTypes are not properly handled. The type is not added
		if (result instanceof Collection)
		{
			ArrayNode arrayNode = Json.newObject().arrayNode();
			for (Object entry : (Collection<?>) result)
			{
				arrayNode.add(Json.toJson(entry));
			}
			return arrayNode;
		}
		JsonNode json = Json.toJson(result);
		return json;
	}

	@Transactional()
	@BodyParser.Of(BodyParser.TolerantText.class)
	public Result newInstance() throws JsonParseException, JsonMappingException, IOException
	{
		return parseAndPersist(null);
	}

	private Result parseAndPersist(final Long id) throws IOException, JsonParseException, JsonMappingException
	{
		if (request().body().isMaxSizeExceeded())
		{
			return badRequest("Too much data. Change the parsers.text.maxLength setting in the configuration.");
		}
		String jsonString = request().body().asText();
		ObjectMapper mapper = new ObjectMapper();
		T entity = mapper.readValue(jsonString, m_clazz);
		validateEntity(id, entity);
		EntityHelper.persist(entity);

		return ok(toJson(entity));
	}

	protected void validateEntity(final Long id, final T entity)
	{

	}

	@Transactional()
	@BodyParser.Of(BodyParser.TolerantText.class)
	public Result save(final Long id) throws JsonParseException, JsonMappingException, IOException
	{
		return parseAndPersist(id);
	}

	@Transactional()
	public Result delete(final Long id)
	{
		EntityHelper.getEntityById(m_clazz, id).delete();
		return ok();
	}
}
