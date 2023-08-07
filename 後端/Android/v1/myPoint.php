<?php 

require_once '../includes/DbOperations.php';

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
	if(
		isset($_POST['uid']) and 
			isset($_POST['newPoint']))
		{
		//operate the data further 

		$db = new DbOperations(); 

		$result = $db->updateMyPoint( 	
									$_POST['uid'],
                                    $_POST['newPoint']
								);

		if($result == 1){
			$response['error'] = false; 
			$response['message'] = "User newPoint successfully";
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