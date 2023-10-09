<?php
    require_once '../includes/DbOperations.php'; // 导入数据库连接类

    $response = array();

    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $db = new DbOperations();
        $detailResponse = $db->getDetail();

        // 将从函数获取的响应合并到总的响应中
        $response = array_merge($response, $detailResponse);
    } else {
        $response['success'] = false;
        $response['message'] = "Invalid request method.";
    }

    // 返回 JSON 响应
    echo json_encode($response);
?>
