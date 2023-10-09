<?php

require_once '../includes/DbOperations.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['uid']) && isset($_POST['img']) && isset($_POST['title']) && isset($_POST['content'])) {
        $filename = "IMG" . uniqid() . ".jpg"; // 使用uniqid()来生成唯一的文件名
        if (file_put_contents("../112_dog/news/" . $filename, base64_decode($_POST['img']))) {
            $db = new DbOperations();
            $result = $db->publish(
                $_POST['uid'],
                $filename,
                $_POST['title'],
                $_POST['content']
            );

            if ($result == 1) {
                $response['error'] = false;
                $response['message'] = "User publish successfully";
            } else {
                $response['error'] = true;
                $response['message'] = "Failed to publish news";
            }
        } else {
            $response['error'] = true;
            $response['message'] = "Failed to save image";
        }
    } else {
        $response['error'] = true;
        $response['message'] = "Required fields are missing";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Invalid Request";
}

echo json_encode($response);

?>
