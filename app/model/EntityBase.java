package model;

import play.db.jpa.JPA;

public abstract class EntityBase
{
	public abstract Long getId();

	protected abstract void setId(Long id);

	public void update()
	{
		JPA.em().merge(this);
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

	public static String truncate(final String content, final int length)
	{
		if (content != null && content.length() > length)
		{
			return content.substring(0, length - 5) + " ...";
		}
		return content;
	}
}
