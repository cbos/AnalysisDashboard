package model;

import utils.EMHelper;

public abstract class EntityBase
{
	public abstract Long getId();

	protected abstract void setId(Long id);

	public void update()
	{
		EMHelper.em().merge(this);
		EMHelper.em().flush();
	}

	public void create()
	{
		EMHelper.em().persist(this);
	}

	public void delete()
	{
		EMHelper.em().remove(this);
	}

	public static String truncate(final String content, final int length)
	{
		if ((content != null) && (content.length() > length))
		{
			return content.substring(0, length - 5) + " ...";
		}
		return content;
	}
}
