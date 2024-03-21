package com.example.demo.entity;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matches")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Match 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamHeading;
    private String matchNumberVenu;
    private String battingTeam;
    private String battingTeamScoure;
    private String bowlTeam;
    private String bowlTeamScour;
    private String LiveText;
    private String matchLink;
    private String textComplete;
    @Enumerated
    private MatchStatus status;
    private Date date=new Date();
    
    public void setMatchStatus()
    {
    	if(textComplete.isBlank())
    	{
    		this.status=MatchStatus.live;
    		
    	}
    	else
    	{
    		this.status=MatchStatus.complete;
    		
    	}
    }
    


}
