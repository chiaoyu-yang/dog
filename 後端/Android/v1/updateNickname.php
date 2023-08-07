<?php
header('Content-Type: application/json; charset=utf-8');
require_once '../includes/DbOperations.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['oldNickname']) && isset($_POST['newNickname'])) {
        $db = new DbOperations();
        $result = $db->updateNickname($_POST['oldNickname'], $_POST['newNickname']);

        if ($result) {
            $response['newNicknames'] = $result;
        } else {
            $response['message'] = "暱稱更新失敗。";
        }
    } else {
        $response['message'] = "缺少必要的參數。";
    }
} else {
    $response['message'] = "無效的請求方法。";
}

echo json_encode($response, JSON_UNESCAPED_UNICODE);
?>
