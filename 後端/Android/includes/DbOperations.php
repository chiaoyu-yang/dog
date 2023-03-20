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
			$stmt = $this->con->prepare("INSERT INTO `user_login` (`Uid`, `username`, `email`) VALUES (NULL, ?, ?);");
			$stmt->bind_param("ss",$username,$email);

			if($stmt->execute()){
				return 1; 
			}else{
				return 2; 
			}
		}
	}

?>

	