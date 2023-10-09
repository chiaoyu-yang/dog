<?php 
	class DbOperations{

		private $con; 

		function __construct(){

			require_once dirname(__FILE__).'/DbConnect.php';

			$db = new DbConnect();

			$this->con = $db->connect();

		}

		/*CRUD -> C -> CREATE */

		public function createUser($gmail){
			$stmt = $this->con->prepare("INSERT INTO 
			`user_login` (`Uid`, `gmail`, `isStop`, `create_time`) 
			VALUES (NULL, ?, 'N', now());");

			$stmt->bind_param("s", $gmail);

			if($stmt->execute()){
				return 1; 
			}else{
				return 2; 
			}
		}


		public function createProfile($gmail){
			$stmt = $this->con->prepare("INSERT INTO `user_profile` 
			SELECT `UID`, concat('玩家',`UID`), 0,'default', now() FROM `user_login`
			WHERE gmail = ?;");
			$stmt->bind_param("s", $gmail);

			if($stmt->execute()){
				return 1; 
			}else{
				return 2; 
			}
		}

		public function getQuestions(){
			$stmt = $this->con->prepare("SELECT question, choice1, choice2, choice3, choice4, ans FROM questions ORDER BY RAND() LIMIT 1");
			$stmt->execute();
			$result = $stmt->get_result();
		
			if($result->num_rows > 0){
				$questions = array();
				while($question = $result->fetch_assoc()){
					$questions[] = $question;
				}
				return $questions;
			}else{
				return null;
			}
		}

		public function addAnswerRecord($uid, $question, $selectedOption, $isCorrect) {
			// 先找到對應的 qid
			$stmt = $this->con->prepare("SELECT Qid FROM `112-dog`.questions WHERE question = ?");
			$stmt->bind_param("s", $question);
			$stmt->execute();
			$result = $stmt->get_result();
			
			if ($result->num_rows > 0) {
				$qid = $result->fetch_assoc()['Qid'];
				
				// 新增資料到 answer_records 資料表中
				$stmt = $this->con->prepare("INSERT INTO `112-dog`.`answer_records` (`Uid`, `Qid`, `u_answer`, `is_correct`, `create_time`) VALUES (?, ?, ?, ?, now())");
				$stmt->bind_param("ssss", $uid, $qid, $selectedOption, $isCorrect);
				$stmt->execute();
				
				return true;
			} else {
				return false; // 找不到對應的 question，新增失敗
			}
		}

		// 更新使用者的點數
		public function updateMyPoint($uid, $newPoint) {
			// 尋找使用者的目前點數
			$stmt = $this->con->prepare("SELECT points FROM `112-dog`.user_profile WHERE Uid = ?;");
			$stmt->bind_param("s", $uid);
			$stmt->execute();
			$result = $stmt->get_result();
			
			$row = $result->fetch_assoc();
			$currentPoint = $row['points'];
				
			// 計算新的點數
			$updatedPoint = $currentPoint + $newPoint;
				
			// 如果新的點數小於 0，將其設為 0
			if ($updatedPoint < 0) {
				$updatedPoint = 0;
			}
		
			// 更新資料庫
			$stmt = $this->con->prepare("UPDATE `112-dog`.user_profile SET points = ? WHERE uid = ?");
			$stmt->bind_param("is", $updatedPoint, $uid);
				
			if ($stmt->execute()) {
				echo "點數更新成功";
			} else {
				echo "點數更新失敗: " . $stmt->error;
			}
		}

		public function getMyranks($nickname) {
			$stmt = $this->con->prepare("SELECT division, nickname, points FROM ranks WHERE nickname = ?;");
			$stmt->bind_param("s", $nickname);
			$stmt->execute();
			$result = $stmt->get_result();
			
			if ($result->num_rows > 0) {
				$myranks = array();
				while ($myrank = $result->fetch_assoc()) {
					$myranks[] = $myrank;
				}
				return $myranks;
			} else {
				return null;
			}
		}

		public function getRanks() {
			$stmt = $this->con->prepare("SELECT division, nickname, points FROM ranks");
			$stmt->execute();
			$result = $stmt->get_result();
			
			if ($result->num_rows > 0) {
				$ranks = array();
				while ($rank = $result->fetch_assoc()) {
					$ranks[] = $rank;
				}
				return $ranks;
			} else {
				return null;
			}
		}

		public function getBooks() {
			$stmt = $this->con->prepare("SELECT * FROM `112-dog`.book_and_beautytop;");
			$stmt->execute();
			$result = $stmt->get_result();
			
			if ($result->num_rows > 0) {
				$ranks = array();
				while ($rank = $result->fetch_assoc()) {
					$ranks[] = $rank;
				}
				return $ranks;
			} else {
				return null;
			}
		}

		public function getNews() {
			$stmt = $this->con->prepare("SELECT Nid,title,content,newsimage as image FROM `112-dog`.news;");
			$stmt->execute();
			$result = $stmt->get_result();
			
			if ($result->num_rows > 0) {
				$ranks = array();
				while ($rank = $result->fetch_assoc()) {
					$ranks[] = $rank;
				}
				return $ranks;
			} else {
				return null;
			}
		}

		public function updateMyNickname($uid, $newNickname, $oldNickname){
			$stmt = $this->con->prepare("UPDATE `user_profile` SET `nickname` = ?, ` WHERE nickname = ?;");
			$stmt->bind_param("ss", $newNickname, $oldNickname);
			
			if($stmt->execute()){
				return 1; 
			}else{
				return 2; 
			}
		}

		public function getAllImages() {
			$stmt = $this->con->prepare("SELECT userimage FROM `112-dog`.user_profile");
			$stmt->execute();
			$result = $stmt->get_result();
		
			if ($result->num_rows > 0) {
				$images = array();
				while ($row = $result->fetch_assoc()) {
					// 构建图像 URL 的完整路径
					$imagePath = 'http://140.131.114.145/Android/112_dog/uploads/' . $row['userimage'];
					$images[] = $imagePath;
				}
				return $images;
			} else {
				return null;
			}
		}
		
		
		
		public function getDetail() {
			$stmt = $this->con->prepare("SELECT * FROM `112-dog`.books where Bid = 1;");
			$stmt->execute();

			$result = $stmt->get_result();
			$row = $result->fetch_assoc();

			$response['Bid'] = $row['Bid'];
			$response['name'] = $row['name'];

			return $response;
		}

		
		public function updateDetail($Bid) {
			$stmt = $this->con->prepare("SELECT * FROM `112-dog`.books where Bid = ?;");
			$stmt->execute();

			$result = $stmt->get_result();
			$row = $result->fetch_assoc();

			$response['Bid'] = $row['Bid'];
			$response['name'] = $row['name'];

			return $response;
		}

		public function updateImage($oldImage, $newImage, $imagePath){
			$stmt = $this->con->prepare("UPDATE `user_profile` SET `userimage` = ? WHERE userimage = ?");
			$stmt->bind_param("ss", $newImage, $oldImage);
			
			if($stmt->execute()){
				// 另外，您可以根据需要更新路径
				$stmt->close();
		
				// 更新图像路径
				$stmt = $this->con->prepare("UPDATE `user_profile` SET `image_path_column` = ? WHERE userimage = ?");
				$stmt->bind_param("ss", $imagePath, $oldImage);
				$stmt->execute();
		
				return 1; 
			} else {
				return 2; 
			}
		}
		
		public function getNickname($gmail) {
			$stmt = $this->con->prepare("SELECT a.Uid, Nickname FROM `112-dog`.user_login AS a
				LEFT JOIN user_profile AS b ON a.uid = b.uid
				WHERE gmail = ?");
			$stmt->bind_param("s", $gmail); // 將參數綁定到佔位符
			$stmt->execute();
			$result = $stmt->get_result();
		
			if ($result->num_rows > 0) {
				$myNickname = array();
				while ($row = $result->fetch_assoc()) {
					$myNickname[] = $row;
				}
				return $myNickname;
			} else {
				return null;
			}
		}
		
		public function getBeauty(){
			$stmt = $this->con->prepare("SELECT `image`,`name`,`like` FROM `112-dog`.beauty;");
			$stmt->execute();
			$result = $stmt->get_result();

			if ($result->num_rows > 0) {
				$ranks = array();
				while ($beauty = $result->fetch_assoc()) {
					$beautys[] = $beauty;
				}
				return $beautys;
			} else {
				return null;
			}
		}

		public function getTopBeauty(){
			$stmt = $this->con->prepare("SELECT `image`,`name`,`like` FROM `112-dog`.beauty_top_season;");
			$stmt->execute();
			$result = $stmt->get_result();

			if ($result->num_rows > 0) {
				$ranks = array();
				while ($topbeauty = $result->fetch_assoc()) {
					$topbeautys[] = $topbeauty;
				}
				return $topbeautys;
			} else {
				return null;
			}
		}

		public function publish($uid, $filename, $title, $content ){
			$stmt = $this->con->prepare("INSERT INTO `112-dog`.`news` (`Uid`, `title`, `content`, `newsimage`, `create_time`) 
			VALUES (?, ?, ?, ?, now());");

			$stmt->bind_param("ssss", $uid, $title, $content, $filename);

			if($stmt->execute()){
				return 1; 
			}else{
				return 2; 
			}
		}

	}

?>

	