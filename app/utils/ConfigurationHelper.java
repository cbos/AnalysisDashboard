package utils;

import play.Configuration;

public class ConfigurationHelper
{

	public static String getEmailSMTPHost()
	{
		return getStringProperty("email.smtphost");
	}

	public static String getEmailFromName()
	{
		return getStringProperty("email.from.name");
	}

	public static String getEmailFromMailAddress()
	{
		return getStringProperty("email.from.mailaddress");
	}

	private static String getStringProperty(final String key)
	{
		return Configuration.root().getString(key);
	}

}
