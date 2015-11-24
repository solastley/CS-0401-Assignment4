//Solomon Astley, ID #3938540
//CS 0401 Ramirez, Lab Section Thursday 10:00
//Voter.java - This class will serve to represent individual voters

import java.io.*;
import java.util.*;

public class Voter
{
	private String voterID;
	private String voterName;
	private boolean voted;

	public Voter(Voter v)
	{
		this.voterID = v.getVoterID();
		this.voterName = v.getVoterName();
		this.voted = v.hasVoted();
	}

	public Voter(String voterID, String voterName, boolean voted)
	{
		this.voterID = voterID;
		this.voterName = voterName;
		this.voted = voted;
	}

	public String getVoterID()
	{
		return voterID;
	}

	public String getVoterName()
	{
		return voterName;
	}

	public boolean hasVoted()
	{
		return voted;
	}

	public void justVoted()
	{
		voted = true;
	}

	public String toFile()
	{
		StringBuilder b = new StringBuilder(voterID + ":");
		b.append(voterName + ":");

		if (voted)
		{
			b.append("true");
		}
		else {
			b.append("false");
		}

		b.append("\n");

		return b.toString();
	}
}