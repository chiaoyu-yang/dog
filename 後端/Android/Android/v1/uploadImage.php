<?php
    require_once '../includes/DbOperations.php';


    if(isset($_POST['image']))
	{
	
	$target_dir = "Images/";
	$image = $_POST['image'];
	$imageStore = rand()."_".time().".jpeg";
	$target_dir = $target_dir."/".$imageStore;
	file_put_contents($target_dir, base64_decode($image));

	$select= "INSERT into imageData(image) VALUES ('$imageStore')";
	$responce = mysqli_query($connect,$select);
	
	if($responce)
			{
							
			    echo "Image Uploaded";
				mysqli_close($connect);
				
			}
	else{
	        echo "Failed";
			}
	}
?>