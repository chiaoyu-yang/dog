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

		// 更新使用者的點數
		function updateMyPoint($uid, $newPoint) {
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

		public function updateNickname($oldNickname, $newNickname){
			$stmt = $this->con->prepare("UPDATE `user_profile` SET `nickname` = ? WHERE nickname = ?;");
			$stmt->bind_param("ss", $newNickname, $oldNickname);
			$stmt->execute();
			$result = $stmt->get_result();
		
			if ($result->num_rows > 0) {
				$newNicknames = array();
				while ($row = $result->fetch_assoc()) {
					$newNicknames[] = $row['nickname'];
				}
				return $newNicknames;
			} else {
				return null;
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
	}

?>

	