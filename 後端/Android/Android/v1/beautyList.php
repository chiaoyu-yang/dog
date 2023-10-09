<?php 
header('Content-Type: text/html; charset=utf-8');
require_once '../includes/DbOperations.php';

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){

    $db = new DbOperations(); 

    $result = $db->getBeauty();

    if($result){
            $response['beautys'] = $result;
    }else{
            $response['message'] = "Failed to retrieve beautys.";
    }
    
}else{
    $response['message'] = "Invalid request method.";
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);

?>