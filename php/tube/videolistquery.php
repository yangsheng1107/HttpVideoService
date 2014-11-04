<?php
	include("config.php");
	if($_SERVER["REQUEST_METHOD"] == "POST")
	{
		$mode=mysqli_real_escape_string($db,$_POST['mode']); 
		switch($mode){
		    case "1":
		        $sql="SELECT * FROM videolists ORDER BY title DESC";
		        break;
		    case "2":
		        $sql="SELECT * FROM videolists ORDER BY duration";
		        break;
		    case "3":
		        $sql="SELECT * FROM videolists ORDER BY duration DESC";
		        break;
		    default:
		        $sql="SELECT * FROM videolists";
		        break;
		}

		$result=mysqli_query($db,$sql);
		$count = 0;
		while($row=mysqli_fetch_array($result,MYSQLI_ASSOC))
		{
			$response[$count]["count"] = $count;
			$response[$count]["title"] = $row['title'];
			$response[$count]["thumb_url"] = $row['thumb_url'];
			$response[$count]["video_url"] = $row['video_url'];
			$response[$count]["duration"] = $row['duration'];
			$response[$count]["description"] = $row['description'];
			$count++;
		}
		
		die(json_encode(array('lists' => $response)));
	}
?>
<form action="videolistquery.php" method="post">
<label>Sort:</label>
<input type="text" name="mode"/><br />
<input type="submit" value=" Submit "/><br />
</form>