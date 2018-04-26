package br.com.caelum.feel.feedback.cycles.domain.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Cycle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank
	private String name;
	@NotBlank
	@Lob
	private String welcomeText;
	@NotBlank
	@Lob
	private String closingText;
	
	/**
	 * @deprecated frameworks only
	 */
	@Deprecated(since = "1.0.0")
	Cycle() {}

	public Cycle(String name, String welcomeText, String closingText) {
		this.name = name;
		this.welcomeText = welcomeText;
		this.closingText = closingText;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	
	public String getWelcomeText() {
		return welcomeText;
	}
	
	public String getClosingText() {
		return closingText;
	}

	public void update(Cycle updatedCycle) {
		this.name = updatedCycle.name;
		this.welcomeText = updatedCycle.welcomeText;
		this.closingText = updatedCycle.closingText;
	}
	
	
}