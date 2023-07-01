<?php 
header('Content-Type: application/json; charset=utf-8');
require_once '../includes/DbOperations.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['gmail'])) {
        // Further data operations
        $db = new DbOperations();
        $result = $db->getNickname($_POST['gmail']);

        if ($result) {
            $response['myNickname'] = $result;
        } else {
            $response['message'] = "無法獲取稱號。";
        }
    } else {
        $response['message'] = "缺少必要的參數。";
    }
} else {
    $response['message'] = "無效的請求方法。";
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);


?>