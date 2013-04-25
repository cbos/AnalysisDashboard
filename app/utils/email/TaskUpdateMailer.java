package utils.email;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;

import model.task.Task;

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
		catch (EmailException | IOException | URISyntaxException e)
		{
			Logger.error("Error during sending of the email", e);
			throw new RuntimeException(e);
		}
	}

	private void setEmailSubject(final HtmlEmail email)
	{
		email.setSubject(String.format("Task '%s' has been assign to you", task.getSummary()));
	}

	private void setEmailBody(final HtmlEmail email) throws IOException, EmailException, URISyntaxException
	{
		String emailContent = getGeneratedEmail();
		emailContent = emailContent.replaceAll("%task.summary%", task.getSummary());
		emailContent = emailContent.replaceAll("%task.details%", task.getDetails());
		emailContent = emailContent.replaceAll("%url%", url);
		email.setHtmlMsg(emailContent);
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

	private static String getGeneratedEmail() throws IOException, URISyntaxException
	{
		if (null == s_emailContent)
		{
			s_emailContent = readFile(getURI(TASK_UPDATE_GENERATED_EMAIL_PATH));
		}
		return s_emailContent;
	}

	private static URI getURI(final String relativePath) throws URISyntaxException
	{
		return Play.application().resource(relativePath).toURI();
	}

	private static String readFile(final URI uri) throws IOException
	{
		return new String(Files.readAllBytes(new File(uri).toPath()));
	}
}
