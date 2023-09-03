<?php
require_once '../includes/DbOperations.php'; // 导入数据库连接类

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_FILES['image']['name'])) {
        $db = new DbOperations();
        $imageResponse = $db->uploadImage($_FILES['image']);

        // 将从函数获取的上传图像的响应合并到总的响应中
        $response = array_merge($response, $imageResponse);
        
        // 获取所有图像
        $allImages = $db->getAllImages();
        if ($allImages !== null) {
            $response['all_images'] = $allImages;
        }
    } else {
        $response['success'] = false;
        $response['message'] = "Image not provided.";
    }
} else {
    $response['success'] = false;
    $response['message'] = "Invalid request method.";
}

// 返回 JSON 响应
echo json_encode($response);
?>
