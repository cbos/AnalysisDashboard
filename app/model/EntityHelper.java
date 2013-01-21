package model;

import play.db.jpa.JPA;

public class EntityHelper
{
	
	public static <T extends EntityBase> T persist(T entityToPersist)
	{
		if (null == entityToPersist.getId() || entityToPersist.getId() < 1L)
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
		return JPA.em().find(clazz, id);
	}
}
