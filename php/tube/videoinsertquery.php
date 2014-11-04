<?php
	include("config.php");
	if($_SERVER["REQUEST_METHOD"] == "POST")
	{
		$title=mysqli_real_escape_string($db,$_POST['title']); 
		$duration=mysqli_real_escape_string($db,$_POST['duration']);
		$video_rul=mysqli_real_escape_string($db,$_POST['video_rul']); 
		$thumb_rul=mysqli_real_escape_string($db,$_POST['thumb_rul']);
		$description=mysqli_real_escape_string($db,$_POST['description']);
		$sql="INSERT INTO videolists VALUES (NULL, '".$title."', '".$thumb_rul."', '".$video_rul."', '".$duration."', '".$description."')";
 
		$result=mysqli_query($db,$sql);
	}
?>
<form action="videoinsertquery.php" method="post">
<label>Title:</label>
<input type="text" name="title"/><br />
<label>Duration:</label>
<input type="text" name="duration"/><br />
<label>Description:</label>
<input type="text" name="description"/><br />
<input type="submit" value=" Submit "/><br />
</form>