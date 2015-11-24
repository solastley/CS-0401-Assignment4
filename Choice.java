//Solomon Astley, ID #3938540
//CS 0401 Ramirez, Lab Section Thursday 10:00
//This class represents an individual choice for each ballot

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//Choice class that extends JButton
public class Choice extends JButton
{
	private String choiceName;
	private int count;
	private boolean clicked;
	private Choice [] allChoices;

	public Choice(Choice c)
	{
		super(c.getChoiceName());
		this.choiceName = c.getChoiceName();
		count = 0;
		this.clicked = c.getStatus();

		ActionListener listener = new ClickListener();
		addActionListener(listener);

		setFont(new Font("Tahoma", Font.BOLD, 12));
	}

	public Choice(String choiceName)
	{
		super(choiceName);

		this.choiceName = new String(choiceName);
		count = 0;
		clicked = false;

		ActionListener listener = new ClickListener();
		addActionListener(listener);

		setFont(new Font("Tahoma", Font.BOLD, 12));
	}

	public boolean getStatus()
	{
		return clicked;
	}

	public String getChoiceName()
	{
		return choiceName;
	}

	public void giveAllChoices(Choice [] allChoices)
	{
		this.allChoices = allChoices;
	}

	public void setStatus()
	{
		clicked = !clicked;
	}

	public void setStatus(boolean status)
	{
		clicked = status;
	}

	public int getCount()
	{
		return count;
	}

	public void addCount()
	{
		count++;
	}

	public String toFile()
	{
		StringBuilder b = new StringBuilder(choiceName + ":");
		b.append(count + "\n");

		return b.toString();
	}

	private class ClickListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			clicked = !clicked;
			for (int i = 0; i < allChoices.length; i++)
			{
				if (allChoices[i].getStatus() && !allChoices[i].getChoiceName().equals(choiceName))
				{
					allChoices[i].setStatus();
					allChoices[i].setForeground(Color.black);
					break;
				}
			}

			if (clicked)
			{
				setForeground(Color.red);
			}
			else {
				setForeground(Color.black);
			}
		}
	}
}