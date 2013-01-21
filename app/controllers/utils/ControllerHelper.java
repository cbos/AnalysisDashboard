package controllers.utils;

import static play.mvc.Controller.form;
import static model.EntityHelper.getEntityById;
import model.EntityBase;
import model.EntityHelper;
import play.data.Form;

public class ControllerHelper<T extends EntityBase>
{
	private final Class<T> entityClass;

	public ControllerHelper(final Class<T> clazz)
	{
		this.entityClass = clazz;
	}

	public Form<T> getNewForm()
	{
		return form(entityClass);
	}

	public Form<T> getFilledDataForm(final Long id)
	{
		return form(entityClass).fill(getEntityById(entityClass, id));
	}

	public Form<T> getDataFormBindFromRequest(final Long id)
	{
		final Form<T> dataForm;
		if (id < 1L)
		{
			dataForm = form(entityClass).bindFromRequest();
		}
		else
		{
			dataForm = form(entityClass).fill(getEntityById(entityClass, id)).bindFromRequest();
		}
		return dataForm;
	}
	
	public T getEntityToPersist(final Form<T> dataForm)
	{
		return dataForm.get();
	}

	public T persist(T entityToPersist)
	{
		return EntityHelper.persist(entityToPersist);
	}
}
