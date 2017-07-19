CREATE DATABASE  IF NOT EXISTS `blab` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `blab`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: blab
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blabs`
--

DROP TABLE IF EXISTS `blabs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blabs` (
  `blabid` int(11) NOT NULL AUTO_INCREMENT,
  `blabber` int(11) NOT NULL,
  `content` varchar(250) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`blabid`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blabs`
--

LOCK TABLES `blabs` WRITE;
/*!40000 ALTER TABLE `blabs` DISABLE KEYS */;
INSERT INTO `blabs` VALUES (1,7,'Just changed my Facebook name to â€˜No one\' so when I see stupid posts I can click like and it will say â€˜No one likes this\'.','2016-04-21 19:19:55'),(2,6,'How do you make holy water? You boil the hell out of it.','2016-05-03 03:39:22'),(3,3,'I am a nobody, nobody is perfect, therefore I am perfect.','2016-04-24 01:10:36'),(4,4,'What do you call a bear with no teeth? -- A gummy bear!','2016-05-01 13:41:30'),(5,7,'I once farted in an elevator, it was wrong on so many levels.','2016-04-27 21:03:06'),(6,5,'If con is the opposite of pro, it must mean Congress is the opposite of progress?','2016-05-15 00:59:02'),(7,7,'I wondered why the frisbee was getting bigger, and then it hit me.','2016-04-24 12:03:28'),(8,3,'What do you call a fat psychic? A four chin teller.','2016-05-04 09:23:03'),(9,3,'I used to like my neighbors, until they put a password on their Wi-Fi.','2016-04-17 13:45:49'),(10,6,'Never argue with a fool, they will lower you to their level, and then beat you with experience.','2016-05-09 05:13:42'),(11,7,'Light travels faster than sound. This is why some people appear bright until they speak.','2016-05-05 19:25:06'),(12,4,'Doctor: You\'re overweight. Patient: I think I want a second opinion. Doctor: You\'re also ugly.','2016-05-05 06:52:24'),(13,7,'If practice makes perfect, and nobody\'s perfect, why practice?','2016-05-12 13:12:03'),(14,4,'Why did the duck go to rehab? Because he was a quack addict!','2016-05-14 20:48:18'),(15,6,'What did the fish say when he swam into the wall? -- Damn','2016-04-28 16:25:05'),(16,5,'The early bird might get the worm, but the second mouse gets the cheese.','2016-04-22 12:47:27'),(17,7,'A plateau is the highest form of flattery.','2016-05-09 21:37:30'),(18,2,'Some just told me to stop acting like a flamingo, so I had to put my foot down.','2016-04-23 23:10:11'),(19,7,'The writer of \'The Hokey Cokey\' song has died, it was a struggle getting him in the coffin, they put his left leg in, then the trouble started.','2016-04-30 13:48:40'),(20,6,'My friend has opened up an ice rink charging just 10p a go, what a cheap skate.','2016-04-17 13:45:28'),(21,3,'What do you call an alligator with GPS? A navigator.','2016-05-09 15:49:34'),(22,2,'What do you call an alligator in a vest? An investigator.','2016-05-03 02:21:18'),(23,2,'To that bloke in a wheelchair who nicked my camouflage jacket, you can hide, but you can\'t run!','2016-04-23 01:16:53'),(24,3,'I haven\'t talked to my girlfriend for days now, I don\'t like to interrupt her.','2016-04-25 10:34:26'),(25,4,'The man that invented throat lozenges died last week, there was no coffin at the funeral.','2016-04-29 08:36:58'),(26,3,'I\'ve discovered I have a logic fetish, I can\'t stop coming to conclusions.','2016-05-17 04:37:26'),(27,2,'Arriving at work today a clown opened the door for me, I thought, that\'s a nice jester.','2016-05-04 20:27:10'),(28,6,'Our daughter took a degree in ballet, and got a 2:2','2016-05-08 12:36:54'),(29,6,'What\'s the difference between a kangaroo & a kangaroot?  One is a kangaroo & the other is a Geordie stuck in a lift. ','2016-04-21 12:19:48'),(30,7,'A photon checks into a hotel and is asked if he needs any help with his luggage, \'no thanks, I\'m travelling light.\'','2016-05-03 22:28:44'),(31,4,'Personal ads:- \'Alcoholic man seeks similar woman for a drink or two, maybe more\'.  ','2016-05-10 23:36:44'),(32,2,'If it\'s the case that girls tend to marry men like their fathers, you can see why their mothers cry at the weddings. ','2016-05-15 22:01:48'),(33,3,'What\'s red and bad for your teeth? A brick.','2016-05-14 16:58:43'),(34,7,'I still remember what my grandpa said before he kicked the bucket, it was, â€œhow far do you think I can kick this bucket?','2016-05-14 13:48:33'),(35,3,'I was trying to play FIFA on the computer, but it wouldn\'t load, & just kept saying, \'Fifa is corrupt, Fifa is corrupt\' !!','2016-04-29 16:30:59'),(36,2,'BREAKING: Swiss Police confirm that, when arrested, all seven FIFA officials threw themselves on the ground and pretended to be injured.','2016-04-18 13:38:04'),(37,5,'Our neighbourhood has a tiny ghost that helps out during hard times, it\'s good to have a little community spirit.','2016-04-22 06:52:12'),(38,4,'Last night I bought an alcoholic ginger beer, he wasn\'t happy about it.','2016-05-03 02:14:42'),(39,6,'I tried to start up a chicken dating agency but failed, it was a struggle to make hens meet.','2016-04-21 14:57:42'),(40,4,'I could barely lift my bottle of water earlier, it was an Evian.','2016-05-16 01:54:49'),(41,7,'Someone\'s having a BBQ 1760 yards away, you can smell it a mile off.','2016-04-24 22:46:39'),(42,4,'Just dropped my new phone in the jacuzzi, I think it\'s syncing.','2016-04-21 10:00:41'),(43,6,'I walked into a Baker\'s and asked, \'Is that a doughnut or a meringue?\', \'No, you\'re right, its a Doughnut.\', he said.','2016-05-10 03:54:46'),(44,3,'On Election day, I\'ll take my voting slip for a candle lit dinner, champagne and truffles, I\'m gonna spoil my ballot.','2016-05-10 14:43:50'),(45,7,'I\'ve invented a new flavour of crisps,  if they\'re successful I\'ll make a packet.','2016-04-27 19:12:13'),(46,3,'They say mums have eyes in the back of their heads, well one woman really did, but had an op to put them where they belong, hasn\'t looked back since.','2016-04-27 23:46:41'),(47,4,'I just fell through the roof of a French bakery, I\'m in a world of pain.','2016-05-13 14:38:26'),(48,4,'My cockney mate is doing really well in the over-sized trouser business, he\'s making huge strides.','2016-05-07 10:55:32'),(49,3,'News:- A coach containing session musicians has overturned on the motorway, drivers may expect lengthy jams.','2016-04-20 17:36:20'),(50,3,'I\'ve just put my friend Richard on speed dial on the phone, it\'s my Get-Rich-Quick scheme.','2016-05-04 10:25:37'),(51,5,'I met Phil Spector\'s brother, Crispin, the other day,he\'s head of quality control at Walkers.','2016-05-17 19:23:20'),(52,3,'A man has died after falling in a vat of coffee, it was instant.','2016-04-21 19:12:48'),(53,4,'Our Grandad got his tongue shot off in the First World War, but he doesn\'t talk about it.','2016-04-26 17:48:31'),(54,6,'I told my boss I come out in a rash every time I get my wages, he asked why, \'because I\'m allergic to peanuts\'.','2016-05-08 23:50:38'),(55,3,'My Doc asked if I drank to excess, I said I\'d drink to anything.','2016-05-05 02:30:09'),(56,4,'So what if I can\'t spell \'armaggedon\'?, it\'s not like it\'s the end of the world.','2016-04-21 18:46:16'),(57,4,'Why did the scarecrow get promotion? Because he was outstanding in his field.','2016-04-19 17:41:13'),(58,3,'A man goes to the doctor with a carrot up his nose, and a parsnip in his ear,  the doc said, \'clearly youâ€™re not eating properly.\'','2016-04-25 08:03:56'),(59,7,'My friend was a victim of his own success, his trophy cabinet fell on him.','2016-05-02 05:12:39'),(60,6,'Alphabet Spaghetti warning:- \'May contain N, U, T and S\'.','2016-04-30 10:47:34'),(61,4,'A suspect was charged with killing a man with sandpaper, in defence he said,  \' I only meant to rough him up a bit\'.','2016-04-20 17:01:13'),(62,5,'Did you hear about the lonely pyromaniac?, he\'s still looking for the perfect match. ','2016-04-27 20:15:56'),(63,7,'Why didnâ€™t the lifeguard save the hippie?, cos he was so far out man!','2016-04-20 18:33:04'),(64,7,'Did you hear about the mad Mexican train murderer? He had locomotives.','2016-04-28 05:50:57'),(65,3,'As this magician was walking down the high street, he turned into a chemist shop.','2016-05-06 20:00:06'),(66,2,'My mate went to a hardcore Star Trek fan convention dressed as Chewbacca, it was a wookie mistake ','2016-04-23 05:12:30'),(67,3,'My mate\'s a safety officer in a kids playground, his careers on the slide. ','2016-04-30 11:17:55'),(68,5,'A man was arrested for stealing helium balloons, police held him for a while then let him go.','2016-04-23 00:41:22'),(69,4,'A man was in court for stealing a bag, took just 3 minutes to get sentenced, it was a briefcase ','2016-05-17 04:59:48'),(70,4,'The tiles, A,E,I,O,and U were discovered in a dead scrabble players stomach, vowel play is supected.','2016-04-27 06:13:03'),(71,5,'I was in a restaurant when I got hit in the head with a prawn cocktail, as I looked round, the waiter shouted, \'that\'s for starters!!\'','2016-05-03 06:14:57'),(72,12,'&#8220;Quotation is a serviceable substitute for wit.&#8221; &#8213; Oscar Wilde','2017-07-19 10:36:03');
/*!40000 ALTER TABLE `blabs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `commentid` int(11) NOT NULL AUTO_INCREMENT,
  `blabid` int(11) NOT NULL,
  `blabber` int(11) NOT NULL,
  `content` varchar(250) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`commentid`)
) ENGINE=InnoDB AUTO_INCREMENT=164 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,1,3,'I give that 1/10.','2016-04-24 06:01:08'),(2,2,5,'I give that 2/10.','2016-05-11 18:52:11'),(3,5,6,'I give that 2/10.','2016-05-15 08:12:58'),(4,5,3,'Hate it.','2016-04-24 06:26:54'),(5,6,6,'I give that 8/10.','2016-05-01 15:50:18'),(6,6,7,'Feel kind of indifferent about that one.','2016-05-15 10:01:20'),(7,6,4,'Hate it.','2016-05-02 09:29:01'),(8,6,7,'I wonder whether fish would find that funny.','2016-04-18 02:31:44'),(9,6,4,'Its funny because its true!','2016-04-26 22:05:20'),(10,8,3,'I give that 3/10.','2016-04-20 17:49:52'),(11,8,6,'Don\'t give up the day job.','2016-05-03 17:35:45'),(12,8,5,'I give that 1/10.','2016-05-08 09:22:22'),(13,8,4,'I give that 4/10.','2016-04-27 19:29:02'),(14,9,3,'I give that 2/10.','2016-04-24 05:29:37'),(15,9,4,'Don\'t give up the day job.','2016-05-05 09:05:53'),(16,9,3,'Love it.','2016-04-29 13:06:15'),(17,9,5,'I want to laugh, I really do. But thats just not funny.','2016-05-09 00:59:15'),(18,9,2,'I give that 2/10.','2016-04-27 18:32:20'),(19,10,3,'I give that 1/10.','2016-05-02 11:17:47'),(20,10,7,'Awful. Just awful.','2016-04-29 16:43:22'),(21,10,7,'Feel kind of indifferent about that one.','2016-05-17 01:12:32'),(22,10,4,'Awful. Just awful.','2016-04-25 22:55:41'),(23,11,3,'I give that 8/10.','2016-04-29 06:07:25'),(24,11,6,'I want to laugh, I really do. But thats just not funny.','2016-04-19 15:56:10'),(25,11,5,'Oh man, you suck.','2016-04-25 08:35:11'),(26,11,6,'I give that 1/10.','2016-04-29 06:16:04'),(27,12,4,'I give that 3/10.','2016-05-07 13:53:47'),(28,12,3,'I give that 2/10.','2016-05-07 05:14:15'),(29,12,2,'Oh man, you suck.','2016-05-07 23:51:47'),(30,12,6,'I want to laugh, I really do. But thats just not funny.','2016-05-10 10:41:39'),(31,13,6,'Love it.','2016-05-17 18:55:37'),(32,13,5,'You make me laugh - a lot.','2016-04-24 09:59:05'),(33,13,2,'I give that 9/10.','2016-05-12 14:58:23'),(34,13,4,'I give that 6/10.','2016-05-12 17:15:41'),(35,14,4,'Don\'t give up the day job.','2016-05-16 00:45:22'),(36,14,7,'I give that 7/10.','2016-05-13 07:59:37'),(37,14,5,'I want to laugh, I really do. But thats just not funny.','2016-05-05 22:54:56'),(38,15,6,'So funny I fell off my chair.','2016-05-17 23:16:38'),(39,15,5,'So funny I fell off my chair.','2016-05-09 08:17:10'),(40,15,5,'I want to laugh, I really do. But thats just not funny.','2016-04-26 00:53:49'),(41,15,3,'I give that 10/10.','2016-05-07 00:53:27'),(42,16,6,'I give that 8/10.','2016-04-30 23:36:09'),(43,16,4,'I give that 3/10.','2016-04-19 06:43:05'),(44,16,6,'Don\'t give up the day job.','2016-05-09 01:17:54'),(45,17,4,'I want to laugh, I really do. But thats just not funny.','2016-04-29 05:24:27'),(46,17,3,'You make me laugh - a lot.','2016-05-13 22:18:54'),(47,17,2,'I give that 8/10.','2016-05-13 11:06:51'),(48,17,7,'Awful. Just awful.','2016-04-29 09:52:55'),(49,18,5,'I give that 8/10.','2016-05-06 16:09:42'),(50,18,7,'I give that 2/10.','2016-05-09 16:47:33'),(51,18,4,'I give that 5/10.','2016-04-29 04:16:07'),(52,18,5,'I give that 1/10.','2016-05-12 06:57:28'),(53,19,6,'Awful. Just awful.','2016-05-07 01:45:52'),(54,19,2,'Awful. Just awful.','2016-05-14 01:41:15'),(55,20,5,'I want to laugh, I really do. But thats just not funny.','2016-05-16 02:53:34'),(56,20,6,'I wonder whether fish would find that funny.','2016-04-23 00:17:10'),(57,20,2,'I give that 7/10.','2016-05-10 23:43:32'),(58,20,3,'Hate it.','2016-04-26 00:32:48'),(59,21,3,'I give that 9/10.','2016-04-25 07:32:18'),(60,21,3,'Awful. Just awful.','2016-05-04 23:48:32'),(61,21,7,'Oh man, you suck.','2016-04-24 00:53:30'),(62,21,7,'Hate it.','2016-05-14 11:10:44'),(63,21,3,'I give that 6/10.','2016-05-15 19:41:27'),(64,22,3,'So funny I fell off my chair.','2016-05-10 19:55:32'),(65,24,2,'Awful. Just awful.','2016-04-27 20:01:12'),(66,25,5,'You make me laugh - a lot.','2016-04-27 15:05:17'),(67,25,6,'I give that 7/10.','2016-05-05 04:19:59'),(68,25,6,'I wonder whether fish would find that funny.','2016-05-17 11:06:28'),(69,25,7,'I give that 3/10.','2016-05-05 23:12:17'),(70,25,6,'Love it.','2016-05-09 18:09:58'),(71,27,5,'Feel kind of indifferent about that one.','2016-05-01 16:31:46'),(72,28,6,'So funny I fell off my chair.','2016-04-19 11:34:55'),(73,30,3,'I give that 3/10.','2016-05-12 23:26:04'),(74,30,4,'I give that 9/10.','2016-05-04 16:52:25'),(75,30,2,'You make me laugh - a lot.','2016-05-04 05:12:14'),(76,30,3,'I give that 5/10.','2016-05-11 00:33:06'),(77,30,4,'Feel kind of indifferent about that one.','2016-05-10 07:53:06'),(78,31,2,'So funny I fell off my chair.','2016-05-05 18:05:59'),(79,31,2,'Oh man, you suck.','2016-05-12 20:39:26'),(80,31,3,'Feel kind of indifferent about that one.','2016-04-19 12:12:01'),(81,32,7,'So funny I fell off my chair.','2016-04-20 16:31:14'),(82,32,3,'Feel kind of indifferent about that one.','2016-05-05 04:06:39'),(83,32,4,'I wonder whether fish would find that funny.','2016-05-11 02:51:12'),(84,33,4,'Its funny because its true!','2016-05-12 11:39:47'),(85,33,3,'Hate it.','2016-05-11 03:21:42'),(86,34,5,'I give that 7/10.','2016-05-14 08:49:53'),(87,34,3,'I give that 6/10.','2016-04-30 05:21:27'),(88,34,6,'I give that 3/10.','2016-05-15 20:30:47'),(89,35,4,'So funny I fell off my chair.','2016-05-04 14:33:20'),(90,35,3,'I give that 2/10.','2016-05-01 21:37:09'),(91,35,7,'Love it.','2016-04-28 14:34:49'),(92,35,7,'I give that 9/10.','2016-05-10 07:58:03'),(93,35,2,'I want to laugh, I really do. But thats just not funny.','2016-05-06 10:10:33'),(94,36,4,'I give that 5/10.','2016-05-10 22:10:39'),(95,36,3,'Awful. Just awful.','2016-05-09 23:21:12'),(96,36,3,'I give that 5/10.','2016-04-17 13:41:27'),(97,37,2,'I give that 10/10.','2016-05-03 01:39:29'),(98,37,2,'I give that 5/10.','2016-05-10 23:15:13'),(99,38,5,'Oh man, you suck.','2016-04-29 17:05:22'),(100,38,4,'Its funny because its true!','2016-05-07 17:24:40'),(101,38,2,'I give that 6/10.','2016-04-29 00:25:54'),(102,38,5,'Feel kind of indifferent about that one.','2016-05-09 09:34:37'),(103,38,3,'I wonder whether fish would find that funny.','2016-04-22 13:30:07'),(104,40,3,'I give that 5/10.','2016-05-10 09:19:12'),(105,40,4,'I wonder whether fish would find that funny.','2016-04-20 12:04:47'),(106,40,3,'I give that 3/10.','2016-05-13 06:21:43'),(107,40,6,'I give that 8/10.','2016-05-13 23:42:35'),(108,40,2,'Awful. Just awful.','2016-05-17 18:25:38'),(109,41,6,'I want to laugh, I really do. But thats just not funny.','2016-04-22 03:37:44'),(110,42,3,'Its funny because its true!','2016-04-26 06:22:05'),(111,42,5,'I give that 4/10.','2016-05-07 04:13:13'),(112,43,5,'I give that 9/10.','2016-05-14 01:17:57'),(113,43,5,'I give that 2/10.','2016-04-19 20:51:51'),(114,43,4,'I give that 10/10.','2016-04-22 15:27:17'),(115,44,7,'Feel kind of indifferent about that one.','2016-05-09 22:47:23'),(116,45,6,'I give that 5/10.','2016-05-05 05:19:48'),(117,45,2,'I give that 2/10.','2016-05-04 00:06:43'),(118,45,4,'I give that 7/10.','2016-05-08 12:15:19'),(119,47,2,'Its funny because its true!','2016-05-11 02:55:19'),(120,47,6,'I give that 3/10.','2016-05-02 06:12:23'),(121,47,3,'I give that 7/10.','2016-05-14 06:06:28'),(122,48,4,'So funny I fell off my chair.','2016-05-14 05:52:22'),(123,48,4,'Love it.','2016-04-30 13:53:17'),(124,48,7,'I want to laugh, I really do. But thats just not funny.','2016-04-30 00:04:05'),(125,48,5,'I give that 7/10.','2016-04-21 09:07:06'),(126,51,6,'I give that 5/10.','2016-04-24 00:33:39'),(127,51,7,'Love it.','2016-05-16 06:39:19'),(128,51,6,'Its funny because its true!','2016-05-06 11:35:19'),(129,52,6,'So funny I fell off my chair.','2016-05-03 22:42:16'),(130,52,5,'Love it.','2016-05-09 21:12:55'),(131,52,3,'So funny I fell off my chair.','2016-05-04 22:05:15'),(132,52,4,'I give that 4/10.','2016-04-25 04:25:39'),(133,53,2,'Awful. Just awful.','2016-04-26 06:45:25'),(134,53,2,'You make me laugh - a lot.','2016-04-30 14:13:50'),(135,53,6,'I give that 7/10.','2016-04-24 08:29:53'),(136,53,3,'So funny I fell off my chair.','2016-04-27 21:55:22'),(137,53,7,'I wonder whether fish would find that funny.','2016-05-18 00:00:01'),(138,54,2,'I give that 1/10.','2016-05-10 12:58:16'),(139,56,5,'I give that 3/10.','2016-04-23 19:34:38'),(140,57,6,'Love it.','2016-04-25 17:27:48'),(141,57,6,'Hate it.','2016-05-06 04:16:01'),(142,57,2,'Its funny because its true!','2016-04-24 16:01:22'),(143,58,5,'I give that 6/10.','2016-05-12 16:11:36'),(144,60,2,'You make me laugh - a lot.','2016-05-09 23:21:00'),(145,63,6,'I give that 7/10.','2016-05-05 12:16:05'),(146,63,2,'I give that 8/10.','2016-05-01 03:38:01'),(147,63,5,'I give that 9/10.','2016-05-12 19:55:11'),(148,63,7,'I give that 4/10.','2016-04-25 19:45:08'),(149,63,7,'I give that 1/10.','2016-04-29 13:43:58'),(150,64,4,'I give that 5/10.','2016-05-01 02:45:27'),(151,65,7,'Love it.','2016-04-28 12:02:18'),(152,65,6,'I give that 4/10.','2016-05-09 04:11:10'),(153,65,4,'I give that 4/10.','2016-04-28 14:32:04'),(154,66,7,'Its funny because its true!','2016-05-15 20:50:15'),(155,66,7,'I want to laugh, I really do. But thats just not funny.','2016-04-28 12:32:03'),(156,66,3,'I give that 9/10.','2016-05-04 12:17:28'),(157,66,3,'I give that 2/10.','2016-04-21 17:26:58'),(158,67,2,'I give that 4/10.','2016-04-20 15:57:40'),(159,68,6,'I wonder whether fish would find that funny.','2016-04-17 13:10:36'),(160,69,4,'I give that 6/10.','2016-05-09 07:47:01'),(161,69,7,'I give that 5/10.','2016-04-21 07:16:58'),(162,69,6,'I give that 10/10.','2016-04-23 04:38:47'),(163,72,12,'That is so true!','2017-07-19 10:40:51');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listeners`
--

DROP TABLE IF EXISTS `listeners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `listeners` (
  `blabber` int(11) NOT NULL,
  `listener` int(11) NOT NULL,
  `status` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listeners`
--

LOCK TABLES `listeners` WRITE;
/*!40000 ALTER TABLE `listeners` DISABLE KEYS */;
INSERT INTO `listeners` VALUES (2,3,'Active'),(2,4,'Active'),(2,5,'Active'),(2,6,'Active'),(2,8,'Active'),(3,2,'Active'),(3,4,'Active'),(3,6,'Active'),(4,3,'Active'),(4,5,'Active'),(4,7,'Active'),(4,8,'Active'),(5,3,'Active'),(6,5,'Active'),(6,8,'Active'),(7,2,'Active'),(7,3,'Active'),(7,8,'Active'),(8,2,'Active'),(8,4,'Active'),(8,5,'Active'),(5,12,'Active'),(6,12,'Active'),(7,12,'Active'),(8,12,'Active'),(3,12,'Active'),(4,12,'Active');
/*!40000 ALTER TABLE `listeners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `date_created` date DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `real_name` varchar(100) DEFAULT NULL,
  `blab_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin','2016-04-17','2016-05-18 10:15:50','Thats Mr Administrator to you.','admin'),(2,'john','john','2016-04-30','2016-05-18 07:16:38','John Smith','John'),(3,'paul','paul','2016-05-12','2016-05-17 17:15:18','Paul Farrington','Paul'),(4,'chris','chris','2016-05-10','2016-05-17 14:53:06','Chris Campbell','Chris'),(5,'laurie','laurie','2016-05-02','2016-05-18 02:36:04','Laurie Mercer','Laurie'),(6,'nabil','nabil','2016-04-19','2016-05-17 22:54:59','Nabil Bousselham','Nabil'),(7,'julian','julian','2016-05-17','2016-05-17 16:59:21','Julian Totzek-Hallhuber','Julian'),(8,'joash','joash','2016-04-29','2016-05-18 02:30:19','Joash Herbrink','Joash'),(12,'relaxnow','test','2017-07-19',NULL,'Boy','test');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users_history`
--

DROP TABLE IF EXISTS `users_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blabber` int(11) NOT NULL,
  `event` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_history`
--

LOCK TABLES `users_history` WRITE;
/*!40000 ALTER TABLE `users_history` DISABLE KEYS */;
INSERT INTO `users_history` VALUES (1,12,'test stopped listening to Chris'),(2,12,'test started listening to Chris');
/*!40000 ALTER TABLE `users_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-07-19 13:20:20
