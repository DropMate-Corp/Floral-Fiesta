-- MySQL dump 10.13  Distrib 8.0.33, for Linux (x86_64)
--
-- Host: localhost    Database: db
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `order_item_id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `plant_id` bigint NOT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKh19tu3fmpiq6task47pamxkg8` (`plant_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `FKh19tu3fmpiq6task47pamxkg8` FOREIGN KEY (`plant_id`) REFERENCES `plants` (`plant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,3,1,1),(2,2,2,2),(3,1,3,3),(4,1,4,4),(5,4,5,5);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `acpid` int DEFAULT NULL,
  `delivery_date` date DEFAULT NULL,
  `description` varchar(255) NOT NULL,
  `pickup_code` varchar(255) NOT NULL,
  `pickup_date` date DEFAULT NULL,
  `status` int DEFAULT NULL,
  `total_price` double NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`order_id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,3,'2023-06-07','Package 1','KKKK0000',NULL,1,27,1),(2,3,'2023-06-05','Package 2','MMMM0000',NULL,1,10,1),(3,3,'2023-06-07','Package 3','KKKK5555',NULL,1,11,1),(4,1,'2023-06-08','Delivered Package 1','MMAJ3902','2023-06-08',3,20,1),(5,2,'2023-05-31','Package Waiting for Pickup 1','WQUU3222',NULL,2,100,1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plant_categories`
--

DROP TABLE IF EXISTS `plant_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plant_categories` (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `photo` varchar(255) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plant_categories`
--

LOCK TABLES `plant_categories` WRITE;
/*!40000 ALTER TABLE `plant_categories` DISABLE KEYS */;
INSERT INTO `plant_categories` VALUES (1,'Flowering House Plants','https://assets.hgtv.ca/wp-content/uploads/2021/12/Kalanchoe-Plants-That-Flower-In-Winter-FT.jpg'),(2,'Foliage Plants','https://www.gardendesign.com/pictures/images/675x529Max/site_3/colorful-foliage-plants-proven-winners_16616.jpg'),(3,'Bromeliads','https://www.gardeningknowhow.com/wp-content/uploads/2012/03/bromeliads-1.jpg'),(4,'Orchids','https://www.allaboutgardening.com/wp-content/uploads/2021/11/Types-of-Orchids-1200x667.jpg');
/*!40000 ALTER TABLE `plant_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plants`
--

DROP TABLE IF EXISTS `plants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plants` (
  `plant_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `photo` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`plant_id`),
  KEY `FKth574r1qvlom4o6qimpex1ahm` (`category_id`),
  CONSTRAINT `FKth574r1qvlom4o6qimpex1ahm` FOREIGN KEY (`category_id`) REFERENCES `plant_categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plants`
--

LOCK TABLES `plants` WRITE;
/*!40000 ALTER TABLE `plants` DISABLE KEYS */;
INSERT INTO `plants` VALUES (1,'Chenile Plant is so cool!','Chenile Plant','https://bs.plantnet.org/image/o/3c47e1574ef9846ae1782095a4252a112dc7c260',9,1),(2,'Magic, you know.','Magic Flower','https://www.pacificbulbsociety.org/pbswiki/files/Achimenes/Achimenes_George_Houche_JS1.jpg',5,2),(3,'Very patriotic.','Sweet Flag','https://www.everwilde.com/media/0800/FACOCAL-A-Sweet-Flag-Seeds.jpg',11,3),(4,'Mini patriot','Miniature Sweet Flag','https://bs.plantnet.org/image/o/718ec737b1f6d56d3d0c80fe35159a6b4f235532',20,4),(5,'Herbaceous Perennial','Dwarf Crystal Anthurium','https://bs.plantnet.org/image/o/948397289d9601e81d22d717d6f9f679c92c0f51',45,2),(6,'Annual','Caladium','https://s3.amazonaws.com/eit-planttoolbox-prod/media/images/Copy_of_Caladium-cra_aHi3eQvW5CUs.jpeg',100,2),(7,'Perennial','Miniature Grape Ivy','https://cdn.shopify.com/s/files/1/0069/7299/7717/articles/skotselrad-cissus-striata-400092_400x.jpg',14,2),(8,'Herbaceous Perennial','Striped Inch Plant','https://thumbs.dreamstime.com/b/callisia-elegans-plant-striped-green-white-leaves-58067235.jpg',10,2),(9,'Perennial','Kangaroo Vine','https://bs.plantnet.org/image/o/9a8eb95f28ff5ab65d2380099df881c248baeb8c',345,2),(10,'Perennial','Ti Plant','https://bs.plantnet.org/image/o/bf0103b99f91a14017dfdd613e12e4b24366ba5e',34,2),(11,'Perennial','Exotica Perfection','https://www.houseplantsexpert.com/wp-content/uploads/2022/09/dieffenbachia_amoena.jpg',70,2),(12,'Perennial','Bougainvillea','https://s3.amazonaws.com/eit-planttoolbox-prod/media/images/Bougainvillea_flower_ltUqxe8X3SW3.jpg',10,1),(13,'Annual','Wax Begonia','https://s3.amazonaws.com/eit-planttoolbox-prod/media/images/Begonia_semperfloren_JJ1UVkWTyxoS.jpe',5,1),(14,'Herbaceous Perennial','Kafir Lily','https://smartgardenguide.com/wp-content/uploads/2019/12/clivia-care-1-768x512.jpg',34,1),(15,'Annual','Coleus','https://bs.plantnet.org/image/o/074429d98f5ad8e4d792cd856900d26e485798a8',67,1),(16,'Annual','Crossandra','https://bs.plantnet.org/image/o/4b0d40d738436c2a07d79c16476a0cf7e214e761',12,1),(17,'Perennial','Silver and Gold Dyckia','https://www.nurseriesonline.com/wp-content/uploads/2019/06/dyckia-fosteriana.jpg',100,3),(18,'Perennial','Scarlet Star','https://s3.amazonaws.com/eit-planttoolbox-prod/media/images/Guzmania_Lingulatae_t51KZg65e8qJ.jpeg',43,3),(19,'Herbaceous Perennial','Striped Torch','https://bs.plantnet.org/image/o/4fc7b15e3801aee164feb8973dc1767f8e463fc0',60,3),(20,'Perennial','Zonata','https://bs.plantnet.org/image/o/4f3bbad82925bd71e87f8263a74bc82254732e91',22,3);
/*!40000 ALTER TABLE `plants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `phone_number` int NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'123 Main St','user@email.com','User','password',123456789);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-31  1:57:44
