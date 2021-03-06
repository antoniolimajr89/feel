package br.com.caelum.feel.feedback.companyteams.domain.models;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.util.Assert;

import br.com.caelum.feel.feedback.companyteams.application.forms.TeamForm;

@Entity(name = "teams")
@Audited
public class CompanyTeam {



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    @Min(1)
    @Column(name = "total_expected_people")
    private Integer totalExpectedPeople;

    @NotBlank
	private String leaderLogin;
    
    private boolean enable;

    /**
     * @deprecated frameworks only
     */
    @Deprecated(since = "1.0.0")
    CompanyTeam(){}

    public CompanyTeam(String name, String leaderLogin, Integer totalExpectedPeople,boolean enable){
		Assert.hasText(name, "Name required");
        Assert.hasText(leaderLogin, "Leader login required");
        Assert.notNull(totalExpectedPeople, "Total expected people required");
        Assert.isTrue(totalExpectedPeople > 0, "Total expected people should be positive");

        this.leaderLogin = leaderLogin;
        this.name = name;
        this.totalExpectedPeople = totalExpectedPeople;
        this.leaderLogin = leaderLogin;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyTeam that = (CompanyTeam) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Integer getTotalExpectedPeople() {
        return totalExpectedPeople;
    }

    public void updateFromForm(TeamForm form) {
        name = form.getName();
        totalExpectedPeople = form.getTotalExpectedPeople();
        leaderLogin = form.getLeaderLogin();
        enable = form.isEnable();
    }
    
    public boolean isEnable() {
		return enable;
	}

	public String getLeaderLogin() {
		return leaderLogin;
	}
	
    @Override
	public String toString() {
		return "CompanyTeam [name=" + name + "]";
	}	
}
