package utils.email;

import java.io.IOException;
import java.io.InputStream;

import model.task.Task;

import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import play.Logger;
import play.Play;
import utils.ConfigurationHelper;

public class TaskUpdateMailer
{

	//Mail as sensitive for styling
	//If you want to change the markup of the mail
	//Then change the task_update_template.html
	//Visit http://premailer.dialect.ca/ and transform the page
	//Save the transformed page in task_update_generated_email.html
	//That file will be used to send the mail

	//File is located in the config folder
	static String TASK_UPDATE_GENERATED_EMAIL_PATH = "email/task_update_generated_email.html";

	private final Task task;

	private final String url;

	private static String s_emailContent;

	public TaskUpdateMailer(final Task task, final String url)
	{
		this.task = task;
		this.url = url;
	}

	public void sendMail()
	{
		try
		{
			HtmlEmail email = createEmail();
			setEmailSubject(email);
			setEmailBody(email);
			addRecipient(email, task.getAssignee().getUserName());

			email.send();
		}
		catch (EmailException | IOException e)
		{
			Logger.error("Error during sending of the email", e);
			throw new RuntimeException(e);
		}
	}

	private void setEmailSubject(final HtmlEmail email)
	{
		email.setSubject(String.format("Task '%s' has been assigned to you", task.getSummary()));
	}

	private void setEmailBody(final HtmlEmail email) throws IOException, EmailException
	{
		String emailContent = getGeneratedEmail();
		emailContent = replaceText(emailContent, "%task.summary%", task.getSummary());
		emailContent = replaceText(emailContent, "%task.details%", task.getDetails());
		emailContent = replaceText(emailContent, "%url%", url);
		email.setHtmlMsg(emailContent);
	}

	private String replaceText(final String content, final String toReplace, final String newText)
	{
		return content.replaceAll(toReplace, newText == null ? "" : newText);
	}

	private void addRecipient(final HtmlEmail email, final String userName) throws EmailException
	{
		email.addTo(String.format("%s%s", userName, ConfigurationHelper.getEmailToSuffix()));
	}

	private HtmlEmail createEmail() throws EmailException
	{
		HtmlEmail email = new HtmlEmail();
		email.setHostName(ConfigurationHelper.getEmailSMTPHost());
		email.setFrom(ConfigurationHelper.getEmailFromMailAddress(), ConfigurationHelper.getEmailFromName());
		return email;
	}

	private static String getGeneratedEmail() throws IOException
	{
		if (null == s_emailContent)
		{
			s_emailContent = readFile(getURI(TASK_UPDATE_GENERATED_EMAIL_PATH));
		}
		return s_emailContent;
	}

	private static InputStream getURI(final String relativePath)
	{
		return Play.application().resourceAsStream(relativePath);
	}

	private static String readFile(final InputStream inputStream) throws IOException
	{
		return IOUtils.toString(inputStream, "UTF-8");
	}
}
