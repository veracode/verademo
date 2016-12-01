package com.veracode.verademo.controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.veracode.verademo.utils.UserSession;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException ;
import java.security.NoSuchProviderException ;
 
@Controller
@Scope("request")
public class Utils {
	private static final Logger logger = LogManager.getLogger("VeraDemo:Utils");

	private String[] usersUsername = {
			"admin",
			"john",
			"paul",
			"chrisc",
			"laurie",
			"nabil",
			"julian", //7
			"joash",
			"andrzej",//9
			"april",
			"armando",
			"ben",
			"brian",//13
			"caitlin",
			"christraut",
			"christyson",
			"clint",//17
			"cody",
			"derek",
			"glenn",
			"grant",//21
			"gregory",
			"jacob",
			"jeremy",//24
			"johnny",
			"kevin",
			"scottrum",//27
			"scottsim"
			};
	String[] usersPassword = {
			"admin",
			"john",
			"paul",
			"chrisc",
			"laurie",
			"nabil",
			"julian", //7
			"joash",
			"andrzej",//9
			"april",
			"armando",
			"ben",
			"brian",//13
			"caitlin",
			"christraut",
			"christyson",
			"clint",//17
			"cody",
			"derek",
			"glenn",
			"grant",//21
			"gregory",
			"jacob",
			"jeremy",//24
			"johnny",
			"kevin",
			"scottrum",//27
			"scottsim"
			};
	String[] usersRealName = {
			"Thats Mr Administrator to you.",
			"John Smith",
			"Paul Farrington",
			"Chris Campbell",
			"Laurie Mercer",
			"Nabil Bousselham",
			"Julian Totzek-Hallhuber",
			"Joash Herbrink",
			"Andrzej Szaryk",//9
			"April Sauer",
			"Armando Bioc",
			"Ben Stoll",
			"Brian Pitta",//13
			"Caitlin Johanson",
			"Chris Trautwein",
			"Chris Tyson",
			"Clint Pollock",//17
			"Cody Bertram",
			"Derek Chowaniec",
			"Glenn Whittemore",
			"Grant Robinson",//21
			"Gregory Wolford",
			"Jacob Martel",
			"Jeremy Anderson",//24
			"Johnny Wong",
			"Kevin Rise",
			"Scott Rumrill",//27
			"Scott Simpson"
			};
	String[] usersBlabName = {
			"admin",
			"John",
			"Paul",
			"Chris",
			"Laurie",
			"Nabil",
			"Julian",
			"Joash",
			"Andrzej",//9
			"April",
			"Armando",
			"Ben",
			"Brian",//13
			"Caitlin",
			"Chris Trautwein",
			"Chris Tyson",
			"Clint",//17
			"Cody",
			"Derek",
			"Glenn",
			"Grant",//21
			"Gregory",
			"Jacob",
			"Jeremy",//24
			"Johnny",
			"Kevin",
			"Scott Rumrill",//27
			"Scott Simpson"
			};
	String[] resetQueries = {
			"DROP TABLE IF EXISTS users;",
			"CREATE TABLE users (userid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, username VARCHAR(100), password VARCHAR(100), date_created DATE, last_login DATETIME, real_name VARCHAR(100), blab_name VARCHAR(100));",
			"DROP TABLE IF EXISTS listeners;",
			"CREATE TABLE listeners (blabber INT NOT NULL, listener INT NOT NULL,status VARCHAR(20));",
			"DROP TABLE IF EXISTS blabs;",
			"CREATE TABLE blabs (blabid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,blabber INT NOT NULL, content VARCHAR(250),timestamp DATETIME);",
			"DROP TABLE IF EXISTS comments;",
			"CREATE TABLE comments (commentid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,blabid INT NOT NULL,blabber INT NOT NULL, content VARCHAR(250),timestamp DATETIME);",				
			};
	
	String  usersSQL = "INSERT INTO users (username, password, date_created, last_login, real_name, blab_name) values (?, ?, ?, ?, ?, ?);";
	

	
	@Autowired
	private UserSession theUser;
	
	private String dbConnStr = "jdbc:mysql://localhost/blab?user=blab&password=z2^E6J4$;u;d";

	@RequestMapping(value="/reset", method=RequestMethod.GET)
	public String showReset(@RequestParam(value="type", required=false) String type, Model model) {
		logger.info("Entering showReset");
		
		
		model.addAttribute("usersUsername",  Arrays.asList(usersUsername));
		
		return "reset";
	}

	// SecureRandom
	// NOTE: Use this method to get a SecureRandom object for use whenever
	//       you need a Random number
	public static SecureRandom generateRandom(String arg[]) {
	        SecureRandom random1 = new SecureRandom();
	        random1.nextBytes(new byte[20]);
	        //random1.setSeed(123);
	        return random1;
	}

	@RequestMapping(value="/reset", method=RequestMethod.POST)
	public String processReset(@RequestParam(value="confirm", required=true) String confirm, 
			                   @RequestParam(value="primary", required=false) String primary, Model model) {
		logger.info("Entering processReset");
		
		 int yy=0;
		 
		/* BEGIN BAD CODE */
		Random rand = new Random();
		/* END BAD CODE */
		/* BEGIN GOOD CODE 
		SecureRandom rand = generateRandom(new String[]{});
		/* END GOOD CODE */
		int days_1 = 60 * 60 * 24;
		int months_1 = days_1 * 31;
		
		// adding so I can commit
		int y =0;
		
		Connection connect = null;
		Statement tablesStatement = null;
		PreparedStatement usersStatement = null;
		PreparedStatement listenersStatement = null;
		PreparedStatement blabsStatement = null;
		PreparedStatement commentsStatement = null;
		java.util.Date now = new java.util.Date();

		try {
			logger.info("Getting Database connection");
			// Get the Database Connection
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(dbConnStr);

			// Drop and re-create the Tables
			logger.info("Creating Statement for resetting the Database");
			tablesStatement = connect.createStatement();
			logger.info("Adding queries to batch");
			for (int i=0; i< resetQueries.length; i++) {
				tablesStatement.addBatch(resetQueries[i]);
			}
			logger.info("Executing batch queries");
			int[] resetResults = tablesStatement.executeBatch();
			logger.info("Batch Statement Execution completed");
			
			// Add the users
			logger.info("Preparing the Stetement for adding users");
			usersStatement = connect.prepareStatement(usersSQL);
			for (int i=0; i<usersUsername.length; i++) {
				long vary = rand.nextInt(months_1);
				Date created = new Date(now.getTime() - (vary * 1000));
				vary = rand.nextInt(days_1);
				Timestamp lastLogin = new Timestamp(now.getTime() - (vary * 1000));
				usersStatement.setString(1,  usersUsername[i]);
				usersStatement.setString(2,  usersPassword[i]);
				usersStatement.setDate(3,  created);
				usersStatement.setTimestamp(4,  lastLogin);
				usersStatement.setString(5,  usersRealName[i]);
				usersStatement.setString(6,  usersBlabName[i]);
				logger.info("Adding a user");
				boolean usersResult = usersStatement.execute();
				logger.info((!usersResult ? "Success":"Failed"));

			}
			
			// Add the listeners
			logger.info("Preparing the Stetement for adding listeners");
			String  listenersSQL = "INSERT INTO listeners (blabber, listener, status) values (?, ?, 'Active');";
			listenersStatement = connect.prepareStatement(listenersSQL);
			for (int i=2; i<=usersUsername.length; i++) {
				for (int j=2; j<=usersUsername.length; j++) {
					if (rand.nextBoolean() && i!=j) {
						listenersStatement.setInt(1,  i);
						listenersStatement.setInt(2,  j);
						logger.info("Adding a listener");
						boolean listenersResult = listenersStatement.execute();
						logger.info((!listenersResult ? "Success":"Failed"));
					}
				}

			}
			
			// Add the blabs
			logger.info("Preparing the Stetement for adding blabs");
			String  blabsSQL = "INSERT INTO blabs (blabber, content, timestamp) values (?, ?, ?);";
			blabsStatement = connect.prepareStatement(blabsSQL);
			for (int i=0; i<blabsContent.length; i++) {
				// Pick a random userID
				// userID's in the database are 1 based, Random.nextInt() is 0 based
				// and we don't want 1 - thats admin
				blabsStatement.setInt(1,  rand.nextInt(usersUsername.length-2)+2);
				blabsStatement.setString(2, blabsContent[i] );
				long vary = rand.nextInt(months_1);
				blabsStatement.setTimestamp(3, new Timestamp( now.getTime() - (vary * 1000)));
				logger.info("Adding a blab");
				boolean blabResult = blabsStatement.execute();
				logger.info((!blabResult ? "Success":"Failed"));
			}
			
			
			// Add the comments
			logger.info("Preparing the Statement for adding comments");
			String  commentsSQL = "INSERT INTO comments (blabid, blabber, content, timestamp) values (?, ?, ?, ?);";
			commentsStatement = connect.prepareStatement(commentsSQL);
			for (int i=1; i<=blabsContent.length; i++) {
				// For each Blab...
				
				// How many comments?
				int count = rand.nextInt(6); //(between 0 and 6)
				for (int j=0; j<count; j++) {
					int blabber = rand.nextInt(usersUsername.length-2) + 2;
					int commentNum = rand.nextInt(commentsContent.length);
					commentsStatement.setInt(1,  i);
					commentsStatement.setInt(2, blabber);
					commentsStatement.setString(3,  commentsContent[commentNum]);
					long vary = rand.nextInt(months_1);
					commentsStatement.setTimestamp(4, new Timestamp(now.getTime() - (vary * 1000) ));
					logger.info("Adding a comment");
					boolean commentsResult = commentsStatement.execute();
					logger.info((!commentsResult ? "Success":"Failed"));
					
				}

			}
			
			
			

			

		}catch (SQLException exceptSql) {
			logger.error(exceptSql);
        } catch (ClassNotFoundException cnfe) {
			logger.error(cnfe);
        	
        } finally {
        	try {
                if (tablesStatement != null) {
                	tablesStatement.close();
                }
        	} catch (SQLException exceptSql) {
    			logger.error(exceptSql);
            }
        	try {
                if (connect != null){
                    connect.close();
                }
            } catch (SQLException exceptSql) {
    			logger.error(exceptSql);
            }
        }

		
		return "redirect:reset";
	}

	String[] blabsContent = {
			"Just changed my Facebook name to ‘No one' so when I see stupid posts I can click like and it will say ‘No one likes this'.",
			"How do you make holy water? You boil the hell out of it.",
			"I am a nobody, nobody is perfect, therefore I am perfect.",
			"What do you call a bear with no teeth? -- A gummy bear!",
			"I once farted in an elevator, it was wrong on so many levels.",
			"If con is the opposite of pro, it must mean Congress is the opposite of progress?",
			"I wondered why the frisbee was getting bigger, and then it hit me.",
			"What do you call a fat psychic? A four chin teller.",
			"I used to like my neighbors, until they put a password on their Wi-Fi.",
			"Never argue with a fool, they will lower you to their level, and then beat you with experience.",
			"Light travels faster than sound. This is why some people appear bright until they speak.",
			"Doctor: You're overweight. Patient: I think I want a second opinion. Doctor: You're also ugly.",
			"If practice makes perfect, and nobody's perfect, why practice?",
			"Why did the duck go to rehab? Because he was a quack addict!",
			"What did the fish say when he swam into the wall? -- Damn",
			"The early bird might get the worm, but the second mouse gets the cheese.",
			"A plateau is the highest form of flattery.",
			"Some just told me to stop acting like a flamingo, so I had to put my foot down.",
			"The writer of 'The Hokey Cokey' song has died, it was a struggle getting him in the coffin, they put his left leg in, then the trouble started.",
			"My friend has opened up an ice rink charging just 10p a go, what a cheap skate.",
			"What do you call an alligator with GPS? A navigator.",
			"What do you call an alligator in a vest? An investigator.",
			"To that bloke in a wheelchair who nicked my camouflage jacket, you can hide, but you can't run!",
			"I haven't talked to my girlfriend for days now, I don't like to interrupt her.",
			"The man that invented throat lozenges died last week, there was no coffin at the funeral.",
			"I've discovered I have a logic fetish, I can't stop coming to conclusions.",
			"Arriving at work today a clown opened the door for me, I thought, that's a nice jester.",
			"Our daughter took a degree in ballet, and got a 2:2",
			"What's the difference between a kangaroo & a kangaroot?  One is a kangaroo & the other is a Geordie stuck in a lift. ",
			"A photon checks into a hotel and is asked if he needs any help with his luggage, 'no thanks, I'm travelling light.'",
			"Personal ads:- 'Alcoholic man seeks similar woman for a drink or two, maybe more'.  ",
			"If it's the case that girls tend to marry men like their fathers, you can see why their mothers cry at the weddings. ",
			"What's red and bad for your teeth? A brick.",
			"I still remember what my grandpa said before he kicked the bucket, it was, “how far do you think I can kick this bucket?",
			"I was trying to play FIFA on the computer, but it wouldn't load, & just kept saying, 'Fifa is corrupt, Fifa is corrupt' !!",
			"BREAKING: Swiss Police confirm that, when arrested, all seven FIFA officials threw themselves on the ground and pretended to be injured.",
			"Our neighbourhood has a tiny ghost that helps out during hard times, it's good to have a little community spirit.",
			"Last night I bought an alcoholic ginger beer, he wasn't happy about it.",
			"I tried to start up a chicken dating agency but failed, it was a struggle to make hens meet.",
			"I could barely lift my bottle of water earlier, it was an Evian.",
			"Someone's having a BBQ 1760 yards away, you can smell it a mile off.",
			"Just dropped my new phone in the jacuzzi, I think it's syncing.",
			"I walked into a Baker's and asked, 'Is that a doughnut or a meringue?', 'No, you're right, its a Doughnut.', he said.",
			"On Election day, I'll take my voting slip for a candle lit dinner, champagne and truffles, I'm gonna spoil my ballot.",
			"I've invented a new flavour of crisps,  if they're successful I'll make a packet.",
			"They say mums have eyes in the back of their heads, well one woman really did, but had an op to put them where they belong, hasn't looked back since.",
			"I just fell through the roof of a French bakery, I'm in a world of pain.",
			"My cockney mate is doing really well in the over-sized trouser business, he's making huge strides.",
			"News:- A coach containing session musicians has overturned on the motorway, drivers may expect lengthy jams.",
			"I've just put my friend Richard on speed dial on the phone, it's my Get-Rich-Quick scheme.",
			"I met Phil Spector's brother, Crispin, the other day,he's head of quality control at Walkers.",
			"A man has died after falling in a vat of coffee, it was instant.",
			"Our Grandad got his tongue shot off in the First World War, but he doesn't talk about it.",
			"I told my boss I come out in a rash every time I get my wages, he asked why, 'because I'm allergic to peanuts'.",
			"My Doc asked if I drank to excess, I said I'd drink to anything.",
			"So what if I can't spell 'armaggedon'?, it's not like it's the end of the world.",
			"Why did the scarecrow get promotion? Because he was outstanding in his field.",
			"A man goes to the doctor with a carrot up his nose, and a parsnip in his ear,  the doc said, 'clearly you’re not eating properly.'",
			"My friend was a victim of his own success, his trophy cabinet fell on him.",
			"Alphabet Spaghetti warning:- 'May contain N, U, T and S'.",
			"A suspect was charged with killing a man with sandpaper, in defence he said,  ' I only meant to rough him up a bit'.",
			"Did you hear about the lonely pyromaniac?, he's still looking for the perfect match. ",
			"Why didn’t the lifeguard save the hippie?, cos he was so far out man!",
			"Did you hear about the mad Mexican train murderer? He had locomotives.",
			"As this magician was walking down the high street, he turned into a chemist shop.",
			"My mate went to a hardcore Star Trek fan convention dressed as Chewbacca, it was a wookie mistake ",
			"My mate's a safety officer in a kids playground, his careers on the slide. ",
			"A man was arrested for stealing helium balloons, police held him for a while then let him go.",
			"A man was in court for stealing a bag, took just 3 minutes to get sentenced, it was a briefcase ",
			"The tiles, A,E,I,O,and U were discovered in a dead scrabble players stomach, vowel play is supected.",
			"I was in a restaurant when I got hit in the head with a prawn cocktail, as I looked round, the waiter shouted, 'that's for starters!!'"
			};
	String[] commentsContent = {
			"I give that 1/10.",
			"I give that 2/10.",
			"I give that 3/10.",
			"I give that 4/10.",
			"I give that 5/10.",
			"I give that 6/10.",
			"I give that 7/10.",
			"I give that 8/10.",
			"I give that 9/10.",
			"I give that 10/10.",
			"So funny I fell off my chair.",
			"Its funny because its true!",
			"Oh man, you suck.",
			"Awful. Just awful.",
			"I want to laugh, I really do. But thats just not funny.",
			"Don't give up the day job.",
			"You make me laugh - a lot.",
			"Love it.",
			"Hate it.",
			"Feel kind of indifferent about that one.",
			"I wonder whether fish would find that funny."
			};

}
