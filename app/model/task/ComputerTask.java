package model.task;

import javax.persistence.Entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import play.data.validation.Constraints.Required;

@Entity(name = "computertask")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComputerTask extends Task
{

	@Required
	private Long computerId;

	public Long getComputerId()
	{
		return computerId;
	}

	public void setComputerId(Long computerId)
	{
		this.computerId = computerId;
	}

}
