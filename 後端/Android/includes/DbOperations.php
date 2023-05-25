<?php 
	class DbOperations{

		private $con; 

		function __construct(){

			require_once dirname(__FILE__).'/DbConnect.php';

			$db = new DbConnect();

			$this->con = $db->connect();

		}

		/*CRUD -> C -> CREATE */

		public function createUser($username, $email){
			$stmt = $this->con->prepare("INSERT INTO `test` (`Uid`, `username`, `email`) VALUES (NULL, ?, ?);");
			$stmt->bind_param("ss",$username,$email);

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
			$stmt = $this->con->prepare("SELECT division, username, integral FROM ranks ORDER BY integral desc LIMIT 100");
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

		public function getMyranks($uid) {
			$stmt = $this->con->prepare("SELECT division, username, integral FROM ranks WHERE Uid = '$uid'");
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
		// 取得使用者的 Uid
		$uid = $_SESSION['uid']; // 假設 Uid 儲存在 Session 中
	}

?>

	
