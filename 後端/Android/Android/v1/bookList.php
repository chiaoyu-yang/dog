<?php 
header('Content-Type: text/html; charset=utf-8');
require_once '../includes/DbOperations.php';

$response = array(); 



$db = new DbOperations(); 

$result = $db->getBooks();



echo json_encode($result, JSON_UNESCAPED_UNICODE);

?>