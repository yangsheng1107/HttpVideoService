<?php
	include("config.php");
	if($_SERVER["REQUEST_METHOD"] == "POST")
	{
		$title=mysqli_real_escape_string($db,$_POST['title']);
		$sql="SELECT * FROM videolists WHERE title = '".$title."'";
		$result=mysqli_query($db,$sql);

		//delete file
		while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
		{
			$thumb = $row['thumb_url'];
			$video = $row['video_url'];
			if(file_exists($thumb)){
			    unlink($thumb);
			}
			if(file_exists($video)){
			    unlink($video);
			}
		}
		mysqli_free_result($result);

		//delete from sql
		$sql="DELETE FROM videolists WHERE title = '".$title."'";
		$result=mysqli_query($db,$sql);

        $response["success"] = 1;
        $response["message"] = "Delete successful!";
        die(json_encode($response));
	}
?>
<form action="videodelquery.php" method="post">
<label>Title:</label>
<input type="text" name="title"/><br />
<input type="submit" value=" Submit "/><br />
</form>