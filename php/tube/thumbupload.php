<?php
// Reference : http://www.w3bees.com/2013/02/multiple-file-upload-with-php.html
// Remember to modify upload_max_filesize on php.ini
$valid_formats = array('jpg', 'png');
$max_file_size = 1024*1024*10; //10 M
$path = "thumb/"; // Upload directory

if (isset($_FILES['files'])) {
    if (!is_dir($path)) {
        mkdir($path);
    }

    if ($_FILES['files']['size']['name'] > $max_file_size) {
        $response["success"] = 0;
        $response["message"] = "file too large!";
        die(json_encode($response));
    }

	if( ! in_array(pathinfo($_FILES['files']['name'], PATHINFO_EXTENSION), $valid_formats) ){
        $response["success"] = 0;
        $response["message"] = "invalid file formats!";
        die(json_encode($response));
	}

    if (move_uploaded_file($_FILES['files']['tmp_name'], $path . $_FILES['files']['name'])) 
    {
        $response["success"] = 1;
        $response["message"] = "Upload successful!";
        die(json_encode($response));
    }
    else
    {
        $response["success"] = 0;
        $response["message"] = "Upload failure!";
        die(json_encode($response));
    }
}
?>
<form enctype="multipart/form-data" action="thumbupload.php" method="POST">
<label>Choose file to upload:</label>
<input type="file" name="files"></br>
<input type="submit" value="Upload"></br>
</form>