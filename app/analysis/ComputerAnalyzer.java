package analysis;

import java.util.Set;

import jsonhandling.ComputerParser;
import model.EntityHelper;
import model.jenkins.Computer;
import model.jenkins.JenkinsServer;
import play.Logger;

public class ComputerAnalyzer
{

	private final JenkinsServer m_jenkinsServer;

	private final ComputerParser m_computerParser;

	public ComputerAnalyzer(final JenkinsServer jenkinsServer, final ComputerParser computerParser)
	{
		m_jenkinsServer = jenkinsServer;
		m_computerParser = computerParser;
	}

	public void analyze()
	{
		Logger.of(ComputerAnalyzer.class).info("Analyzing " + m_computerParser.getDisplayName());

		Set<Computer> computers = m_jenkinsServer.getComputers();
		Computer computerEntity = null;
		for (Computer computer : computers)
		{
			if (computer.getDisplayName().equals(m_computerParser.getDisplayName()))
			{
				computerEntity = computer;
			}
		}

		if (computerEntity == null)
		{
			computerEntity = new Computer();
			computerEntity.setDisplayName(m_computerParser.getDisplayName());
			computerEntity.setWatch(true);
			computerEntity.setJenkinsServer(m_jenkinsServer);
		}

		if (m_computerParser.isOfflineCauseAvailable())
		{
			if (m_computerParser.getOfflineCause().matches("\\d*"))
			{
				computerEntity.setOfflineCause("Offline due to lack of diskspace");
			}
			else
			{
				computerEntity.setOfflineCause(m_computerParser.getOfflineCause());
			}
		}
		else
		{
			computerEntity.setOfflineCause("");
		}
		computerEntity.setOffline(m_computerParser.isOffline());
		computerEntity.setDiskSpaceLeft(m_computerParser.getGbLeft().doubleValue());
		EntityHelper.persist(computerEntity);
	}
}
