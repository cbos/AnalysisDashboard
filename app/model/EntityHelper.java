package model;

import java.util.List;

import javax.persistence.Entity;

import utils.EMHelper;

public class EntityHelper
{

	public static <T extends EntityBase> T persist(final T entityToPersist)
	{
		if ((null == entityToPersist.getId()) || (entityToPersist.getId() < 1L))
		{
			entityToPersist.create();
		}
		else
		{
			entityToPersist.update();
		}
		return entityToPersist;
	}

	public static <T extends EntityBase> T getEntityById(final Class<T> clazz, final Long id)
	{
		return EMHelper.em().find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public static <T extends EntityBase> List<T> getAll(final Class<T> clazz)
	{
		Entity entityAnnotation = clazz.getAnnotation(Entity.class);
		String entityName = entityAnnotation.name();
		return EMHelper.em().createQuery("from " + entityName).getResultList();
	}
}
