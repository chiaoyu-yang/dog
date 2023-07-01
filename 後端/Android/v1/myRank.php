<?php 
header('Content-Type: text/html; charset=utf-8');
require_once '../includes/DbOperations.php';

$response = array(); 

if($_SERVER['REQUEST_METHOD']=='POST'){
    if( isset($_POST['nickname']) ) {

        //operate the data further 

        $db = new DbOperations(); 

        $result = $db->getMyranks($_POST['nickname']);

        if($result){
                $response['myranks'] = $result;
        }else{
                $response['message'] = "Failed to retrieve ranks.";
        }
    }
}else{
    $response['message'] = "Invalid request method.";
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);

?>