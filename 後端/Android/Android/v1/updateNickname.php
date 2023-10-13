<?php
$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if (isset($_POST['oldNickname']) and isset($_POST['Nickname'])) {
        // operate the data further 

        $db = new DbOperations();

        $result = $db->updateMyNickname(
            $_POST['oldNickname'],
            $_POST['newNickname']
        );

        if ($result == 1) {
            $response['error'] = false;
            $response['message'] = "User updateNickname successfully";
        } else {
            $response['error'] = true;
            $response['message'] = "Failed to update user nickname";
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
