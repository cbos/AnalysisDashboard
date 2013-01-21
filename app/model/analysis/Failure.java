package model.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;

import model.EntityBase;
import model.jenkins.Build;

@Entity(name = "failure")
public class Failure extends EntityBase
{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 250)
	@Required
	private String summary;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER, targetEntity = Build.class)
	@JoinColumn(name = "build_id", nullable = false, updatable = true, insertable = true)
	private Build build;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	protected void setId(Long id)
	{
		this.id = id;
	}
}
