package model.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import model.EntityBase;

@Entity(name = "user")
public class User extends EntityBase
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userName;

	private String fullName;

	private String emailAddress;
	
	private String avatarURL;

	@Override
	protected void setId(final Long id)
	{
		this.id = id;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(final String userName)
	{
		this.userName = userName;
		setAvatarURL(userName);
	}

	public String getFullName()
	{
		return fullName;
	}

	public void setFullName(final String fullName)
	{
		this.fullName = fullName;
	}

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(final String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	
	public String getAvatarURL()
	{
		return avatarURL;
	}
	
	public void setAvatarURL(final String userName)
	{
		/* Below URL is of the Crucible ( codereview ) server */
		this.avatarURL = "http://srv-ind-scrat.vanenburg.com:8060/avatar/" + userName;
	}
}
