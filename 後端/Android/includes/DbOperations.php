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
			$stmt = $this->con->prepare("INSERT INTO `user_login` (`Uid`, `gmail`, `isStop`, `create_time`) VALUES (NULL, ?, 'N', curdate());");
			$stmt->bind_param("s", $gmail);

			if($stmt->execute()){
				return 1; 
			}else{
				return 2; 
			}
		}

		public function createProfile($gmail){
			$stmt = $this->con->prepare("INSERT INTO `user_profile` 
			SELECT `UID`, concat('玩家',`UID`), 0, curdate() FROM `user_login`
			WHERE gmail = ?;");
			$stmt->bind_param("s", $gmail);

			if($stmt->execute()){
				return 1; 
			}else{
				return 2; 
			}
		}

		public function getQuestions(){
			$stmt = $this->con->prepare("SELECT question, choice1, choice2, choice3, choice4, ans FROM questions ORDER BY RAND() LIMIT 6");
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
	}

?>
