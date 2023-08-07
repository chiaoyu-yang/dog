<?php 

require_once '../includes/DbOperations.php';

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
	if(
		isset($_POST['username']) and 
			isset($_POST['gmail']))
		{
		//operate the data further 

		$db = new DbOperations(); 

		$result = $db->createUser( 	
									$_POST['gmail']
								);
		$uidResult = $db->createProfile($_POST['gmail']);
		if($result == 1){
			$response['error'] = false; 
			$response['message'] = "User registered successfully";
		}

	}else{
		$response['error'] = true; 
		$response['message'] = "Required fields are missing";
	}
}else{
	$response['error'] = true; 
	$response['message'] = "Invalid Request";
}

echo json_encode($response);

?>