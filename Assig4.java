//Solomon Astley, ID #3938540
//CS 0401 Ramirez, Lab Section Thursday 10:00
//Assignment 4 - This code will simulate a voting machine

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Assig4
{
	private JFrame myWindow;
	private Ballot [] ballots;
	private ArrayList<Voter> voters;
	private String filename, voterID, voterName, voted_string;
	private int numBallots;
	private boolean voted;

	public Assig4(String file) throws IOException
	{
		this.filename = new String(file);

		File ballotsFile = new File(filename);
		Scanner ballotsScan = new Scanner(ballotsFile);

		//scans in number of ballots
		numBallots = Integer.parseInt(ballotsScan.nextLine());
		ballots = new Ballot [numBallots];

		int i = 0;
		while (ballotsScan.hasNext())
		{
			//parses through ballots file
			String [] tokens = ballotsScan.nextLine().split(":");
			String ballotID = tokens[0];
			String ballotName = tokens[1];
			//parses one section of the ballots line with commas as delimeters
			String [] choices_str = tokens[2].split(", ");

			//creates an array of choices from split file line
			Choice [] choices = new Choice [choices_str.length];
			for (int k = 0; k < choices.length; k++)
			{
				choices[k] = new Choice(choices_str[k]);
			}

			//sends the entire array of choices to each choice object for later reference
			for (int m = 0; m < choices.length; m++)
			{
				choices[m].giveAllChoices(choices);
			}

			//creates a ballot object
			ballots[i] = new Ballot(ballotID, ballotName, choices);
			i++;
		}
		ballotsScan.close();

		//initializes an arraylist of voter objects
		voters = new ArrayList<Voter>();
		File votersFile = new File("voters.txt");
		Scanner votersScan = new Scanner(votersFile);

		//scans in each voter's data and creates a voter object
		while (votersScan.hasNext())
		{
			String [] tokens = votersScan.nextLine().split(":");
			voterID = new String(tokens[0]);
			voterName = new String(tokens[1]);
			voted = Boolean.parseBoolean(new String(tokens[2]));

			//adds voter object to arraylist
			voters.add(new Voter(voterID, voterName, voted));
		}
		votersScan.close();

		//ceeates files for each ballot ID
		for (int b = 0; b < ballots.length; b++)
		{
			File choicesFile = new File(ballots[b].getBallotID() + ".txt");
			PrintWriter choicesWriter = new PrintWriter(choicesFile);
			for (int v = 0; v < ballots[b].choices.length; v++)
			{
				choicesWriter.print(ballots[b].choices[v].getChoiceName() + ":0\n");
			}
			choicesWriter.close();
		}

		//creates the JFrame for the voting machine GUI
		myWindow = new JFrame("Voting Machine");
		myWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		myWindow.setLayout(new GridLayout(1, numBallots + 1));

		//adds the ballots to the JFrame
		for (int j = 0; j < numBallots; j++)
		{
			myWindow.add(ballots[j]);
		}

		//adds a panel for the login button and cast vote button
		myWindow.add(new RightPanel());

		myWindow.pack();
		myWindow.setVisible(true);
	}

	//ballot class which inherits from JPanel
	public class Ballot extends JPanel
	{
		private String ballotID, ballotName;
		public Choice [] choices;
		private JLabel label;

		public Ballot(String ballotID, String ballotName, Choice [] choices)
		{
			this.ballotID = new String(ballotID);
			this.ballotName = new String(ballotName);

			this.choices = new Choice [choices.length];
			setLayout(new GridLayout(choices.length + 1, 1));

			label = new JLabel(this.ballotName);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			add(label);

			for (int i = 0; i < choices.length; i++)
			{
				this.choices[i] = new Choice(choices[i]);
				add(this.choices[i]);
				this.choices[i].setEnabled(false);
			}
			for (int j = 0; j < choices.length; j++)
			{
				this.choices[j].giveAllChoices(this.choices);
			}
		}

		public String getBallotID()
		{
			return ballotID;
		}
	}

	//RightPanel class that also extends JPanel
	public class RightPanel extends JPanel
	{
		private JButton loginButton, voteButton;

		public RightPanel()
		{
			setLayout(new GridLayout(1, 2));

			loginButton = new JButton("Login to Vote");
			voteButton = new JButton("Cast Your Vote");

			ActionListener listener = new RightListener();

			loginButton.addActionListener(listener);
			voteButton.addActionListener(listener);
			voteButton.setEnabled(false);

			loginButton.setFont(new Font("Tahoma", Font.BOLD, 12));
			voteButton.setFont(new Font("Tahoma", Font.BOLD, 12));

			add(loginButton);
			add(voteButton);
		}

		public class RightListener implements ActionListener
		{
			private String userID;
			private boolean found, alreadyVoted;
			private int confirmed;
			private Voter currentVoter;

			public void actionPerformed(ActionEvent e)
			{
				//if login button was clicked
				if (e.getSource() == loginButton)
				{
					//prompts user for voter ID
					userID = JOptionPane.showInputDialog(null, "Enter your voter ID");

					found = false;
					alreadyVoted = false;

					//loops through all the voters to see if their ID is valid and if they have voted
					for (int i = 0; i < voters.size(); i++)
					{
						//if the prompt window was closed, get out of here
						if (userID == null)
						{
							break;
						}
						
						//if the voter ID was valid
						else if (userID.equals(voters.get(i).getVoterID()))
						{
							currentVoter = voters.get(i);

							if (currentVoter.hasVoted())
							{
								JOptionPane.showMessageDialog(null, currentVoter.getVoterName() + " has already voted!", "Invalid", JOptionPane.PLAIN_MESSAGE);
								alreadyVoted = true;
								break;
							}

							JOptionPane.showMessageDialog(null, "Welcome " + currentVoter.getVoterName() + "! Make your chocies", "Welcome", JOptionPane.PLAIN_MESSAGE);
							
							//loops through all the ballots and choices and enables them
							for (int j = 0; j < ballots.length; j++)
							{
								for (int k = 0; k < ballots[j].choices.length; k++)
								{
									ballots[j].choices[k].setEnabled(true);
								}
							}
							//enables vote button and disables login button
							voteButton.setEnabled(true);
							loginButton.setEnabled(false);
							found = true;
							break;
						}
					}

					//if the voter ID was invalid
					if (!found && !alreadyVoted && userID != null)
					{
						JOptionPane.showMessageDialog(null, "Invalid Voter ID", "Invalid", JOptionPane.PLAIN_MESSAGE);
					}
				}

				//else if the confirm vote button was clicked
				else
				{
					confirmed = JOptionPane.showConfirmDialog(null, "Confirm your vote?");

					if (confirmed == JOptionPane.YES_OPTION)
					{
						currentVoter.justVoted();

						//if voter confirms their vote, the count variable for each choice is iterated
						for (int i = 0; i < ballots.length; i++)
						{
							for (int j = 0; j < ballots[i].choices.length; j++)
							{
								//if button is clicked
								if (ballots[i].choices[j].getStatus())
								{
									//update count
									ballots[i].choices[j].addCount();
								}
							}
						}

						try 
						{
							//updates the voters file in a safe way
							File votersFile = new File("voters.txt");

							File tempVotersFile = new File("temp.txt");
							PrintWriter tempVotersWriter = new PrintWriter(tempVotersFile);

							for (int m = 0; m < voters.size(); m++)
							{
								tempVotersWriter.print(voters.get(m).toFile());
							}
							tempVotersWriter.close();

							votersFile.delete();
							tempVotersFile.renameTo(new File("voters.txt"));
						}
						//catches IOException
						catch (IOException io)
						{
							System.out.println("Bad voters file");
						}

						try
						{
							//updates the ballot ID files in a safe way
							for (int i = 0; i < ballots.length; i++)
							{
								File choicesFile = new File(ballots[i].getBallotID() + ".txt");
								File tempChoicesFile = new File("tempFile.txt");
								PrintWriter tempChoicesWriter = new PrintWriter(tempChoicesFile);

								for (int j = 0; j < ballots[i].choices.length; j++)
								{
									tempChoicesWriter.print(ballots[i].choices[j].getChoiceName() + ":" + ballots[i].choices[j].getCount() + "\n");
								}
								tempChoicesWriter.close();

								choicesFile.delete();
								tempChoicesFile.renameTo(new File(ballots[i].getBallotID() + ".txt"));
							}
						}
						//catches IOException
						catch(IOException ioe)
						{
							System.out.println("Bad choices file");
						}

						//changes status of buttons accordingly
						for (int j = 0; j < ballots.length; j++)
						{
							for (int k = 0; k < ballots[j].choices.length; k++)
							{
								ballots[j].choices[k].setForeground(Color.black);
								ballots[j].choices[k].setEnabled(false);
								ballots[j].choices[k].setStatus(false);
							}
						}

						voteButton.setEnabled(false);
						loginButton.setEnabled(true);
					}
				}
			}
		}
	}

	public static void main(String [] args) throws IOException
	{
		String file = new String(args[0]);
		new Assig4(file);
	}
}