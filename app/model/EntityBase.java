package model;

import play.db.jpa.JPA;

public abstract class EntityBase
{
	public abstract Long getId();
	
	protected abstract void setId(Long id);

	public void update()
	{
		JPA.em().flush();
	}

	public void create()
	{
		JPA.em().persist(this);
	}

	public void delete()
	{
		JPA.em().remove(this);
	}
}
