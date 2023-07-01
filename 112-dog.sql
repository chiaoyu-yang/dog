-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 140.131.114.242    Database: 112-dog
-- ------------------------------------------------------
-- Server version	8.0.33-0ubuntu0.20.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer_records`
--

DROP TABLE IF EXISTS `answer_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer_records` (
  `Aid` int NOT NULL AUTO_INCREMENT COMMENT '答題編號',
  `Uid` int NOT NULL COMMENT '使用者編號',
  `Qid` int NOT NULL COMMENT '題目編號',
  `u_answer` varchar(50) NOT NULL COMMENT '使用者回答的答案',
  `is_correct` char(1) NOT NULL COMMENT 'T(true)/F(false)',
  `create_time` datetime NOT NULL COMMENT '回答時間',
  PRIMARY KEY (`Aid`),
  KEY `user_login UID_idx` (`Uid`),
  KEY `questions QID_idx` (`Qid`),
  CONSTRAINT `questions QID` FOREIGN KEY (`Qid`) REFERENCES `questions` (`Qid`),
  CONSTRAINT `user_login UID` FOREIGN KEY (`Uid`) REFERENCES `user_login` (`Uid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='使用者回答紀錄';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer_records`
--

LOCK TABLES `answer_records` WRITE;
/*!40000 ALTER TABLE `answer_records` DISABLE KEYS */;
INSERT INTO `answer_records` VALUES (10,45,94,'帶給獸醫師醫療','T','2023-07-01 10:45:34'),(11,45,94,'自己治療','F','2023-07-01 10:45:37'),(12,45,94,'放著不管','F','2023-07-01 10:45:39');
/*!40000 ALTER TABLE `answer_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `Bid` smallint NOT NULL AUTO_INCREMENT COMMENT 'PK,AUTO_INCREMENT(寶典編號)',
  `dogname` varchar(10) NOT NULL COMMENT 'NN, 狗狗名稱',
  `dogimage` varchar(50) NOT NULL COMMENT 'UQ,狗狗照片路徑',
  `desc` varchar(1000) DEFAULT NULL COMMENT '圖片詳細資訊',
  `update_id` int NOT NULL COMMENT 'FK(user_login UID)最後編輯者',
  `update_time` datetime NOT NULL COMMENT 'NN, 修改時間',
  PRIMARY KEY (`Bid`),
  UNIQUE KEY `dogimage_UNIQUE` (`dogimage`),
  KEY `user_login_idx` (`update_id`),
  CONSTRAINT `user_login` FOREIGN KEY (`update_id`) REFERENCES `user_login` (`Uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='寶典';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `Nid` int NOT NULL AUTO_INCREMENT COMMENT '討論區編號',
  `Uid` int NOT NULL,
  `title` varchar(10) NOT NULL COMMENT '標題',
  `content` varchar(500) DEFAULT NULL COMMENT '內文\\\\n',
  `message` varchar(100) DEFAULT NULL COMMENT '留言',
  `like` mediumint DEFAULT NULL COMMENT '按讚數',
  `newsimage` varchar(50) DEFAULT NULL COMMENT '照片路徑',
  `create_time` datetime NOT NULL COMMENT '建立時間',
  PRIMARY KEY (`Nid`),
  UNIQUE KEY `newsimage_UNIQUE` (`newsimage`),
  KEY `news_uid_idx` (`Uid`),
  CONSTRAINT `news_uid` FOREIGN KEY (`Uid`) REFERENCES `user_login` (`Uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questions`
--

DROP TABLE IF EXISTS `questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questions` (
  `Qid` int NOT NULL AUTO_INCREMENT COMMENT '題目編號',
  `question` varchar(100) NOT NULL COMMENT '題目',
  `choice1` varchar(50) NOT NULL COMMENT '選項1',
  `choice2` varchar(50) NOT NULL COMMENT '選項2',
  `choice3` varchar(50) NOT NULL COMMENT '選項3',
  `choice4` varchar(50) NOT NULL COMMENT '選項4',
  `ans` varchar(50) NOT NULL COMMENT '答案',
  PRIMARY KEY (`Qid`),
  UNIQUE KEY `question_UNIQUE` (`question`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='題庫';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questions`
--

LOCK TABLES `questions` WRITE;
/*!40000 ALTER TABLE `questions` DISABLE KEYS */;
INSERT INTO `questions` VALUES (1,'下列那一種犬之體味比較濃厚？','沙皮犬','博美犬','貴賓犬','西施犬','沙皮犬'),(2,'博美犬的原產地為','中國','美國','義大利','德國','德國'),(3,'巴哥犬的原產地為','中國','美國','英國','德國','中國'),(4,'北京狗之原產地為','美國','日本','西臘','中國','中國'),(5,'瑪爾濟斯犬之原產地為','馬爾他群島','庫頁島','冰島','蘇格蘭','馬爾他群島'),(6,'柴犬之原產地為','美國','日本','英國','中國','日本'),(7,'下列何犬被稱為日本之國犬？','狐狸犬','狆','秋田犬','土佐犬','秋田犬'),(8,'狗狗懷孕期約多久？','2個月','4個月','6個月','8個月','2個月'),(9,'拉布拉多犬原產地為','紐西蘭','美國','芬蘭','挪威','芬蘭'),(10,'可卡犬原產地為','英國','美國','日本','中國','英國'),(11,'拳師犬原產地為','英國','德國','加拿大','日本','德國'),(12,'古代牧羊犬之原產地為','英國','德國','加拿大','瑞士','英國'),(13,'聖伯那犬原產地為','英國','德國','加拿大','瑞士','瑞士'),(14,'迷你雪納瑞原產地為','英國','德國','瑞士','美國','德國'),(15,'米格魯(Beagle)獵兔犬原產地為','英國','德國','瑞士','美國','英國'),(16,'臘腸犬原產地為','英國','德國','瑞士','美國','德國'),(17,'沙皮犬之原產地為','美國','澳洲','日本','中國','中國'),(18,'伯恩山犬原產地為','日本','挪威','瑞士','比利時','瑞士'),(19,'阿富汗獵犬原產地為','日本','阿富汗','丹麥','埃及','埃及'),(20,'大麥町犬原產地為','南斯拉夫','匈牙利','法國','愛爾蘭','南斯拉夫'),(21,'紐波利頓犬原產地為','蘇俄','埃及','義大利','西班牙','義大利'),(22,'全世界的犬種中，以何種牙齒咬合狀態最為常見？','下齒過突咬合(Under shot)','剪刀咬合(Scissors bite)','上齒 過突咬合(Over shot)','水平咬合(Level bite)','剪刀咬合(Scissors bite)'),(23,'中國古代將犬類的功能分為三種：食用、看門及','導盲','狩獵','牧羊','伴侶','狩獵'),(24,'德國狼犬是屬於','玩賞犬','獸獵犬','工作犬','水獵犬','工作犬'),(25,'犬隻各類耳型中，以何種耳型異味最重','鈕扣耳','垂耳','半立耳','豎耳','垂耳'),(26,'博美犬的毛質是屬於','雙重毛','單一毛','剛毛','平滑毛','雙重毛'),(27,'狼犬的飛節角度與其他犬種相比，狼犬較','深','淺','普通','毫無差異。','深'),(28,'下列那種犬是屬於飛節角度很淺的狗','北京犬','博美犬','鬆獅犬','秋田犬','鬆獅犬'),(29,'北京犬行進時之步態是屬於','快步','慢步','滾步','速步','滾步'),(30,'犬的狼爪(後肢餘趾)不可切除之犬種為','聖伯納犬','大白熊犬','大麥町犬','吉娃娃犬','大白熊犬'),(31,'下列那種犬之被毛屬於硬質剛毛','雪納瑞犬','博美犬','可卡犬','喜樂蒂犬','雪納瑞犬'),(32,'貴賓犬行進之步態屬於','快步','慢步','滾步','交叉步','快步'),(33,'西施犬行進之步態屬於','快步','慢步','滾步','交叉步','滾步'),(34,'世界上常見的犬種中，以那種胸型最為普遍？','桶狀胸型','扁平胸型','波霸胸型','船底胸型','船底胸型'),(35,'下列那一種犬種屬於桶狀胸型？','鬥牛犬','狼犬','埃及跑犬(灰狗)','大麥町犬','鬥牛犬'),(36,'下列那一種犬種屬於船底胸型？','鬥牛犬','狼犬','埃及跑犬(灰狗)','巴哥犬','狼犬'),(37,'下列那一種犬種屬於扁平胸型？','鬥牛犬','狼犬','埃及跑犬(灰狗)','聖伯納犬','埃及跑犬(灰狗)'),(38,'犬隻身高的量法是由何處至地面之垂直距離？','頭頂','頸部','肩胛頂','腰部','肩胛頂'),(39,'犬隻的身長量法是由：','胸骨尖至坐骨端','胸骨尖至尾根','肩胛至尾根','肩胛至坐骨端','胸骨尖至坐骨端'),(40,'狆犬足端長有甚多被毛，稱為','足毛','爪毛','筆毛','蹠毛','筆毛'),(41,'犬隻線狀集合毛流最明顯分佈的部位是','頰部','頸部','胸部','尾部','頸部'),(42,'長毛貓中較具攻擊性的貓種為','喜馬拉雅','白波斯','金吉拉','折耳貓','金吉拉'),(43,'貴賓犬的體型中，標準型體高應達多少英吋以上？','8 英吋','11 英吋','13 英吋','15 英吋','15 英吋'),(44,'博美犬尾型何者為正確的？','與背部平行','成90°豎立於背部','於臀部向右或向右下垂','反豎地平伸於背部','反豎地平伸於背部'),(45,'與狐狸頭部很類似的小型犬是','約克夏犬','博美犬','比熊犬','柯基犬','博美犬'),(46,'處於寒帶的犬種被毛特徵以下何者為非？','雙重毛質','下毛濃密','尾毛長且量豐','單一毛質','單一毛質'),(47,'比熊犬的原產地是','法國','英國','美國','西班牙','法國'),(48,'比熊犬的犬展修剪造型，身軀的毛長度應留','0.5 英吋','1 英吋','2 英吋','3 英吋','2 英吋'),(49,'以下瑪爾濟斯犬特點中何者為非？','有底毛(Under coat)','毛色純白','單一毛質','毛質呈絲狀','有底毛(Under coat)'),(50,'鼻樑長度 1 英吋的犬種最典型的是','雪納瑞','西施犬','可卡犬','鬆獅犬','可卡犬'),(51,'背部鋼藍色(Steel blue)頭部黃褐色(Tan)被毛是','貴賓犬','吉娃娃犬','約克夏犬','蝴蝶犬','約克夏犬'),(52,'約克夏犬牙齒的咬合何者是正確的？','水平咬合(Level bite)','上齒過突咬合(Over shot)','下齒過突咬合(Under shot)','剪刀咬合(Scissors bite)','剪刀咬合(Scissors bite)'),(53,'北京犬的牙齒咬合何者正確？','下齒過突咬合(Under shot)','上齒過突咬合(Over shot)','剪刀咬合(Scissors bite)','水平咬合(Level bite)','下齒過突咬合(Under shot)'),(54,'何者為錯誤的北京犬特徵？','大頭','大眼','扁臉','淺胸','淺胸'),(55,'北京犬有濃密的底毛(Under coat)，因此被毛是屬於','單一毛質','絲狀毛質','雙重毛質','剛性毛質','雙重毛質'),(56,'雪納瑞犬各種體型中何者未列入國際標準？','茶杯型(Tea Cup)','標準型(Standard)','迷你型(Miniature)','巨型(Giant)','茶杯型(Tea Cup)'),(57,'玩具型貴賓犬(Toy Poodle)指身高為','12 英吋','13 英吋','10 英吋','11 英吋 以下','10 英吋'),(58,'貴賓犬尾型豎立的角度，以時鐘表現應呈','12 點～1 點','1 點～3 點','11 點～10 點','10 點～9 點','12 點～1 點'),(59,'貴賓犬犬展修剪造型何者不被允許？','幼犬型','英國鞍型','泰迪型','歐洲大陸型','泰迪型'),(60,'蝴蝶犬是玩具犬種群(Toy breeds)，其身高最適當的是','30～30.5','20～28','18～20','16～18 ㎝','20～28'),(61,'蝴蝶犬的最大特徵在於','被毛','毛色','體型','耳朵','耳朵'),(62,'蝴蝶犬的被毛是屬於？','絲狀毛','索狀毛','剛狀毛','棉狀毛','絲狀毛'),(63,'拉薩犬的原產地為','印度','尼泊爾','不丹','西藏','西藏'),(64,'鬆獅犬的原產地為','中國','尼泊爾','不丹','印度','中國'),(65,'以下何項非鬆獅犬的特徵？','表情憂鬱','舌黑或藍','步伐如竹馬、行進踏幅亦淺短','後軀角度(Angulations)深 踏幅亦寬','後軀角度(Angulations)深 踏幅亦寬'),(66,'鬆獅犬體軀身長及身高的比率是？','10：12','10：14','10：9','10：10','10：10'),(67,'鬆獅犬有濃密的底毛(Under coat)因此被毛是屬於','單一毛質','絲狀毛質','雙重毛質','剛性毛質','雙重毛質'),(68,'下列何種犬隻因遺傳秉性問題，對寵物美容師於操作時易造成傷害需特別留意？','鬆獅犬','瑪爾濟斯犬','貴賓犬','大白熊犬','鬆獅犬'),(69,'狆犬(Chin Japanese spaniel)的原產地是','日本','中國','韓國','印度','日本'),(70,'狆犬(Chin Japanese spaniel)體軀身長及身高的比率是','10：12','10：14','10：9','10：10','10：10'),(71,'狆犬(Chin Japanese spaniel)的毛色何者可接受？','全黑','全白','全紅','黑白或紅白','黑白或紅白'),(72,'後肢具兩枚狼爪(左右各二)的犬種是','大白熊犬','西藏獒犬','法國鬥牛犬','英國馬士提夫犬','大白熊犬'),(73,'大白熊犬的原產地位於法國和西班牙交界的','高加索山','本寧山','庇里牛斯山(Pyrenees)','烏拉山','庇里牛斯山(Pyrenees)'),(74,'大白熊犬秉性屬於哪一種犬種','兇悍','溫馴','爆烈','攻擊','溫馴'),(75,'大白熊犬的被毛屬於哪一種質地','單一毛質','絲狀毛質','雙重毛質','剛性毛質','雙重毛質'),(76,'古代英國牧羊犬秉性屬於哪一種犬種','兇悍','固執','爆烈','攻擊','固執'),(77,'古代英國牧羊犬犬展修剪造型美容要點是','頂毛適度修短','眼眉下雜毛可修剪','頂毛不能剪，只露鼻、口','四肢被毛修短','頂毛不能剪，只露鼻、口'),(78,'貝林登犬是屬於鐵利亞的犬種但毛質是歸列於','長毛','短毛','軟毛','剛毛','軟毛'),(79,'頭蓋窄外型圓滑似雞蛋幾乎無止部(Stop)的犬種最典型的是','比熊犬','拉薩犬','狆犬','貝林登犬','貝林登犬'),(80,'貝林登犬背部需呈','水平背','鲤背','凹背','菱狀背','鲤背'),(81,'吉娃娃犬的被毛不屬於下列何種','雙重毛','剛毛','短毛','長毛','長毛'),(82,'吉娃娃犬(Chihuahua)的頭蓋骨呈','洋梨型','檸檬型','水蜜桃型','蘋果型','蘋果型'),(83,'犬種標準書認可的犬種體型最小的是','蝴蝶犬','吉娃娃','博美犬','約克夏犬','吉娃娃'),(84,'成長後頭蓋骨常癒合不全的犬種，以下何者最常見？','蝴蝶犬','吉娃娃犬','西施犬','約克夏犬','吉娃娃犬'),(85,'臘腸犬的毛質多樣以下何者是不存在的？','剛毛','短毛','絲狀毛','長毛','絲狀毛'),(86,'短腿長身的臘腸犬最適比率是','10：20','10：15','10：12','10：10','10：20'),(87,'西高地白 犬是屬於鐵利亞的犬種毛質是屬於','長毛','短毛','軟毛','剛毛','剛毛'),(88,'可利牧羊犬(Collie)的毛質以下何者是不存在的','剛毛','短毛','長毛','軟毛','剛毛'),(89,'蘇格蘭犬是屬於鐵利亞的犬種毛質是屬於','長毛','短毛','軟毛','剛毛','剛毛'),(90,'貴賓犬各種體型中何者未列入國際標準？','茶杯型(Tea Cup)','標準型(Standard)','迷你型(Miniature)','玩具型(Toy)','茶杯型(Tea Cup)'),(91,'下列何者為依法公告禁止飼養之動物？','蘇格蘭摺耳貓','西藏獒犬','美洲巨水鼠','喜馬拉雅兔','美洲巨水鼠'),(92,'下列何者為依法公告具攻擊性的犬隻品種？','紐波利頓犬','西藏獒犬','高加索犬','德國狼犬','紐波利頓犬'),(93,'以下何者非寵物美容人員在運送寵物時應注意事項？','食物飲水','動物排泄','環境安全','使其遭受驚嚇','使其遭受驚嚇'),(94,'寵物美容人員如遇送來美容犬隻有受傷或罹病時，應請飼主','帶給獸醫師醫療','自己治療','放著不管','再觀察看看','帶給獸醫師醫療'),(95,'對待動物不得有下列何種行為？','提供適當食物飲水','充足活動空間','法定動物傳染病防治','賭博性質的動物競技','賭博性質的動物競技'),(96,'母狗的懷孕期為幾天?','15~25天','30~40天','56~70天','80~95天','56~70天'),(97,'狗狗出生幾週後開始注射疫苗？','1~3週','3~5週','6~8週','10~12週','6~8週'),(98,'哪一種食物是可以給狗狗吃？','葡萄','蘋果','牛奶','洋蔥','蘋果'),(99,'狗狗的前後腳趾各有幾個？','前面4個，後面3個','前面5個，後面4個','前面3個，後面4個','前面4個，後面5個','前面5個，後面4個'),(100,'當狗狗堅挺站立，目光緊盯，尾巴和和耳朵保持直立的狀態時，是想表達什麼意思呢？','打招呼','恐懼','我不歡迎你','想吃東西','我不歡迎你'),(101,'下列哪個不是狗狗的特殊習性?','當團體領袖','領域觀念','刨土挖洞','喜歡追逐獵物','當團體領袖'),(102,'下列何者不是老狗常見的失智行為？','亂吠叫 ','轉圈圈 ','拉肚子','睡覺習慣改變','拉肚子'),(103,'犬隻罹患心絲蟲問題後導致出現呼吸上的問題，在後續照顧上，下列 何者作法較恰當？','過度地運動','牽繩散步','大太陽時散步','放任不管','牽繩散步'),(104,'想領養狗狗須年滿幾歲才有資格？','20歲','18歲','22歲','17歲','20歲'),(105,'狗狗在食物選擇上仰賴的感官為何？','味覺','視覺','嗅覺','聽覺','嗅覺'),(106,'你知道成年狗的體溫嗎?','37.5-38.5','40~42','27~30','31~33','37.5-38.5'),(107,'成年狗有多少顆牙齒?','42顆','35顆','50顆','40顆','42顆'),(108,'狗的平均壽命是多少年?','12年','15年','20年','10年','12年'),(109,'根據美國養犬俱樂部的說法，美國最受歡迎的犬種是什麼？','拉布拉多獵犬','西藏獒犬','西伯利亞雪橇犬','黃金獵犬','拉布拉多獵犬'),(110,'家養犬種大概有多少種？ 200、450、800 還是 1000？','大約450種','大約200種','大約800種','大約1000種','大約450種'),(111,'哪種狗跑得最快？','蝴蝶犬','拉布拉多獵犬','格雷伊獵犬','黃金獵犬','格雷伊獵犬'),(112,'哪種狗以黑舌頭而聞名？','吉娃娃犬','西施犬','格雷伊獵犬','鬆獅犬','鬆獅犬'),(113,'阿爾薩斯犬的另一個名字是什麼？','鬆獅犬','德國牧羊犬','吉娃娃犬','西施犬','德國牧羊犬');
/*!40000 ALTER TABLE `questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `ranks`
--

DROP TABLE IF EXISTS `ranks`;
/*!50001 DROP VIEW IF EXISTS `ranks`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `ranks` AS SELECT 
 1 AS `division`,
 1 AS `nickname`,
 1 AS `points`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user_login`
--

DROP TABLE IF EXISTS `user_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_login` (
  `Uid` int NOT NULL AUTO_INCREMENT COMMENT '使用者編號',
  `gmail` char(40) NOT NULL COMMENT '電子信箱',
  `isStop` char(1) NOT NULL COMMENT '帳號是否停用,Y/N',
  `create_time` datetime NOT NULL COMMENT '建立時間',
  PRIMARY KEY (`Uid`),
  UNIQUE KEY `email_UNIQUE` (`gmail`)
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='使用者基本資料';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_login`
--

LOCK TABLES `user_login` WRITE;
/*!40000 ALTER TABLE `user_login` DISABLE KEYS */;
INSERT INTO `user_login` VALUES (42,'10856035@ntub.edu.tw','N','2023-05-26 10:06:16'),(44,'1','N','2023-05-26 11:59:13'),(45,'10856042@ntub.edu.tw','N','2023-05-28 07:35:20'),(66,'10856006@ntub.edu.tw','N','2023-05-29 11:40:03'),(70,'10856003@ntub.edu.tw','N','2023-05-29 11:40:03');
/*!40000 ALTER TABLE `user_login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_profile` (
  `Uid` int NOT NULL COMMENT '使用者編號',
  `nickname` varchar(10) NOT NULL COMMENT '暱稱',
  `points` mediumint NOT NULL COMMENT '積分',
  `userimage` varchar(50) NOT NULL COMMENT '狗狗照片路徑',
  `update_time` datetime NOT NULL COMMENT '修改時間',
  PRIMARY KEY (`Uid`),
  UNIQUE KEY `nickname_UNIQUE` (`nickname`),
  KEY `user_login UID_idx` (`Uid`) /*!80000 INVISIBLE */,
  CONSTRAINT `user_UID` FOREIGN KEY (`Uid`) REFERENCES `user_login` (`Uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='使用者基本資料';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile`
--

LOCK TABLES `user_profile` WRITE;
/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
INSERT INTO `user_profile` VALUES (42,'玩家42',20,'default','2023-05-26 10:06:16'),(44,'玩家44',0,'default','2023-05-26 11:59:13'),(45,'玩家45',0,'default','2023-05-28 07:35:20'),(70,'玩家70',0,'default','2023-05-29 11:40:03');
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_profile_for_trigger`
--

DROP TABLE IF EXISTS `user_profile_for_trigger`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_profile_for_trigger` (
  `trigger_date` datetime NOT NULL COMMENT '觸發日期',
  `trigger_type` varchar(2) NOT NULL COMMENT '觸發型態',
  `Uid` int NOT NULL COMMENT '使用者編號',
  `nickname` varchar(10) NOT NULL COMMENT '暱稱',
  `points` mediumint NOT NULL COMMENT '積分',
  `userimage` varchar(50) NOT NULL COMMENT '狗狗照片路徑',
  `update_time` datetime NOT NULL COMMENT '修改時間',
  `Tid` int NOT NULL AUTO_INCREMENT COMMENT '流水號',
  PRIMARY KEY (`Tid`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='sym from user_profile';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile_for_trigger`
--

LOCK TABLES `user_profile_for_trigger` WRITE;
/*!40000 ALTER TABLE `user_profile_for_trigger` DISABLE KEYS */;
INSERT INTO `user_profile_for_trigger` VALUES ('2023-06-01 05:37:17','I',66,'玩家66',0,'default','2023-05-29 11:40:03',1),('2023-06-01 05:33:54','UO',70,'玩家70',0,'default','2023-05-29 11:40:03',2),('2023-06-01 05:33:54','UN',70,'玩家71',0,'default','2023-05-29 11:40:03',3),('2023-06-01 05:43:17','D',66,'玩家66',0,'default','2023-05-29 11:40:03',4),('2023-06-01 05:45:25','UN',70,'玩家70',0,'default','2023-05-29 11:40:03',5),('2023-06-01 05:45:25','UO',70,'玩家71',0,'default','2023-05-29 11:40:03',6),('2023-06-01 16:38:09','UN',42,'玩家42',100,'default','2023-05-26 10:06:16',7),('2023-06-01 16:38:09','UO',42,'玩家42',0,'default','2023-05-26 10:06:16',8),('2023-06-28 10:01:26','UN',42,'玩家42',120,'default','2023-05-26 10:06:16',9),('2023-06-28 10:01:26','UO',42,'玩家42',100,'default','2023-05-26 10:06:16',10),('2023-06-28 10:01:47','UN',42,'玩家42',100,'default','2023-05-26 10:06:16',11),('2023-06-28 10:01:47','UO',42,'玩家42',120,'default','2023-05-26 10:06:16',12),('2023-06-28 10:03:09','UN',42,'玩家42',80,'default','2023-05-26 10:06:16',13),('2023-06-28 10:03:09','UO',42,'玩家42',100,'default','2023-05-26 10:06:16',14),('2023-06-28 10:34:03','UN',45,'玩家45',100,'default','2023-05-28 07:35:20',15),('2023-06-28 10:34:03','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',16),('2023-06-28 10:50:27','UN',42,'玩家42',60,'default','2023-05-26 10:06:16',17),('2023-06-28 10:50:27','UO',42,'玩家42',80,'default','2023-05-26 10:06:16',18),('2023-06-28 11:02:17','UN',45,'玩家45',110,'default','2023-05-28 07:35:20',19),('2023-06-28 11:02:17','UO',45,'玩家45',100,'default','2023-05-28 07:35:20',20),('2023-06-28 11:03:06','UN',45,'玩家45',80,'default','2023-05-28 07:35:20',21),('2023-06-28 11:03:06','UO',45,'玩家45',110,'default','2023-05-28 07:35:20',22),('2023-06-28 11:09:02','UN',42,'玩家42',40,'default','2023-05-26 10:06:16',23),('2023-06-28 11:09:02','UO',42,'玩家42',60,'default','2023-05-26 10:06:16',24),('2023-06-28 11:09:52','UN',45,'玩家45',50,'default','2023-05-28 07:35:20',25),('2023-06-28 11:09:52','UO',45,'玩家45',80,'default','2023-05-28 07:35:20',26),('2023-06-28 11:10:08','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',27),('2023-06-28 11:10:08','UO',45,'玩家45',50,'default','2023-05-28 07:35:20',28),('2023-06-28 11:10:32','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',29),('2023-06-28 11:10:32','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',30),('2023-06-28 11:11:26','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',31),('2023-06-28 11:11:26','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',32),('2023-06-28 14:40:59','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',33),('2023-06-28 14:40:59','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',34),('2023-06-28 15:44:35','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',35),('2023-06-28 15:44:35','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',36),('2023-06-28 15:44:40','UN',45,'玩家45',20,'default','2023-05-28 07:35:20',37),('2023-06-28 15:44:40','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',38),('2023-06-29 01:18:33','UN',45,'玩家45',20,'default','2023-05-28 07:35:20',39),('2023-06-29 01:18:33','UO',45,'玩家45',20,'default','2023-05-28 07:35:20',40),('2023-06-29 01:18:51','UN',45,'玩家45',20,'default','2023-05-28 07:35:20',41),('2023-06-29 01:18:51','UO',45,'玩家45',20,'default','2023-05-28 07:35:20',42),('2023-06-29 01:19:00','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',43),('2023-06-29 01:19:00','UO',45,'玩家45',20,'default','2023-05-28 07:35:20',44),('2023-06-29 01:20:53','UN',42,'玩家42',20,'default','2023-05-26 10:06:16',45),('2023-06-29 01:20:53','UO',42,'玩家42',40,'default','2023-05-26 10:06:16',46),('2023-06-29 06:18:24','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',47),('2023-06-29 06:18:24','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',48),('2023-06-29 06:18:44','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',49),('2023-06-29 06:18:44','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',50),('2023-06-29 06:42:18','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',51),('2023-06-29 06:42:18','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',52),('2023-06-29 06:42:27','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',53),('2023-06-29 06:42:27','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',54),('2023-06-29 06:42:44','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',55),('2023-06-29 06:42:44','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',56),('2023-06-29 06:45:19','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',57),('2023-06-29 06:45:19','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',58),('2023-06-29 06:46:57','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',59),('2023-06-29 06:46:57','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',60),('2023-06-29 06:47:48','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',61),('2023-06-29 06:47:48','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',62),('2023-06-29 06:52:43','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',63),('2023-06-29 06:52:43','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',64),('2023-06-29 06:52:56','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',65),('2023-06-29 06:52:56','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',66),('2023-06-29 06:53:01','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',67),('2023-06-29 06:53:01','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',68),('2023-06-29 06:53:06','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',69),('2023-06-29 06:53:06','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',70),('2023-06-29 06:56:36','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',71),('2023-06-29 06:56:36','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',72),('2023-06-29 07:43:43','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',73),('2023-06-29 07:43:43','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',74),('2023-06-29 07:43:49','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',75),('2023-06-29 07:43:49','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',76),('2023-06-29 07:43:56','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',77),('2023-06-29 07:43:56','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',78),('2023-06-29 07:45:06','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',79),('2023-06-29 07:45:06','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',80),('2023-06-29 07:45:12','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',81),('2023-06-29 07:45:12','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',82),('2023-07-01 05:29:25','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',83),('2023-07-01 05:29:25','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',84),('2023-07-01 05:29:30','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',85),('2023-07-01 05:29:30','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',86),('2023-07-01 05:29:34','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',87),('2023-07-01 05:29:34','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',88),('2023-07-01 07:27:51','UN',45,'玩家45',30,'default','2023-05-28 07:35:20',89),('2023-07-01 07:27:51','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',90),('2023-07-01 08:50:10','UN',45,'玩家45',20,'default','2023-05-28 07:35:20',91),('2023-07-01 08:50:10','UO',45,'玩家45',30,'default','2023-05-28 07:35:20',92),('2023-07-01 08:52:03','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',93),('2023-07-01 08:52:03','UO',45,'玩家45',20,'default','2023-05-28 07:35:20',94),('2023-07-01 08:52:14','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',95),('2023-07-01 08:52:14','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',96),('2023-07-01 08:52:39','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',97),('2023-07-01 08:52:39','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',98),('2023-07-01 08:53:04','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',99),('2023-07-01 08:53:04','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',100),('2023-07-01 10:44:33','UN',45,'玩家45',20,'default','2023-05-28 07:35:20',101),('2023-07-01 10:44:33','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',102),('2023-07-01 10:45:13','UN',45,'玩家45',10,'default','2023-05-28 07:35:20',103),('2023-07-01 10:45:13','UO',45,'玩家45',20,'default','2023-05-28 07:35:20',104),('2023-07-01 10:45:24','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',105),('2023-07-01 10:45:24','UO',45,'玩家45',10,'default','2023-05-28 07:35:20',106),('2023-07-01 10:45:41','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',107),('2023-07-01 10:45:41','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',108),('2023-07-01 10:49:57','UN',45,'玩家45',0,'default','2023-05-28 07:35:20',109),('2023-07-01 10:49:57','UO',45,'玩家45',0,'default','2023-05-28 07:35:20',110);
/*!40000 ALTER TABLE `user_profile_for_trigger` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `ranks`
--

/*!50001 DROP VIEW IF EXISTS `ranks`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`dog`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `ranks` AS select dense_rank() OVER (ORDER BY `user_profile`.`points` desc )  AS `division`,`user_profile`.`nickname` AS `nickname`,`user_profile`.`points` AS `points` from `user_profile` order by `user_profile`.`points` desc limit 100 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-01 18:54:54
