<?php
$expo;
$rate;
$device_id;
$link = mysqli_connect('127.0.0.1', 'root');

//if connection is not successful you will see text error
if (!$link) {
       die('Could not connect: ' . mysqli_error());
}
//if connection is successfuly you will see message bellow
echo 'Connected successfully';

if(isset($_GET['expo'])) {
    $expo = $_GET['expo'];
}

if(isset($_GET['rate'])){
    $rate = $_GET['rate'];
}

if(isset($_GET['device_id'])){
    $device_id = $_GET['device_id'];
}

if(!empty($device_id) and !empty($rate) and !empty($expo) ) {
	echo "i insert to db: Device id = $device_id <br> Rating = $rate <br> Expo = $expo";
	mysqli_select_db($link,'etnograph_rating');
	$gdi_querry = "SELECT device_info FROM 'expo_rating' WHERE id=1";
	$getdevice_info = mysql_query($link,$gdi_querry);
	echo $getdevice_info;
	$sql="INSERT INTO expo_rating (device_info, rating, expo) VALUES ('$device_id','$rate','$expo')";
echo $sql;
mysqli_query($link,$sql);
}else{
	echo "i do nothing";
}
mysqli_close($link);
//XAMPP PROGA NAZIVAETSYA!!!!!!!!!!!
?>
