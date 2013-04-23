package utils.email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import model.task.Task;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import play.Logger;
import utils.ConfigurationHelper;

public class TaskUpdateMailer
{

	//Mail as sensitive for styling
	//If you want to change the markup of the mail
	//Then change the task_update_template.html
	//Visit http://premailer.dialect.ca/ and transform the page
	//Save the transformed page in task_update_generated_email.html
	//That file will be used to send the mail

	static String TASK_UPDATE_GENERATED_EMAIL_PATH = "./app/utils/email/task_update_generated_email.html";

	private final Task task;

	private final String url;

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
		email.setSubject(String.format("Task '%s' has been assign to you", task.getSummary()));
	}

	private void setEmailBody(final HtmlEmail email) throws IOException, EmailException
	{
		byte[] readAllBytes = Files.readAllBytes(Paths.get(TASK_UPDATE_GENERATED_EMAIL_PATH));
		String emailContent = new String(readAllBytes);
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
}
