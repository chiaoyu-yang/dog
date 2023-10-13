<?php

require_once '../includes/DbOperations.php';

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (
        isset($_POST['uid']) and
        isset($_POST['question']) and
        isset($_POST['selectedOption']) and
        isset($_POST['isCorrect'])
    ) {
        // 操作資料

        $db = new DbOperations();

        $result = $db->addAnswerRecord(
            $_POST['uid'],
            $_POST['question'],
            $_POST['selectedOption'],
            $_POST['isCorrect']
        );
        
        if ($result) {
            $response['error'] = false;
            $response['message'] = "User addAnswerRecord successfully";
        } else {
            $response['error'] = true;
            $response['message'] = "Failed to add answer record";
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
