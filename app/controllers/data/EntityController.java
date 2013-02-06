package controllers.data;

import model.EntityBase;
import model.EntityHelper;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class EntityController extends Controller
{

	@Transactional(readOnly = true)
	public static <T extends EntityBase> Result entity(final Class<T> clazz, final Long id)
	{
		if (id > 0)
		{
			return ok(Json.toJson(EntityHelper.getEntityById(clazz, id)));
		}
		return ok(Json.toJson(EntityHelper.getAll(clazz)));
	}
}
