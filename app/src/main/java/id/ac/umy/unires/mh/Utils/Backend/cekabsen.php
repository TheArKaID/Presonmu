<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");

    $email = $mysqli->real_escape_string($_POST['email']);
    $tanggal = $mysqli->real_escape_string($_POST['tanggal']);
    $shift = $mysqli->real_escape_string($_POST['shift']);
    
    
    $query = mysqli_query($mysqli, "SELECT * FROM `riwayat` WHERE
        `email` = '".$email."' AND
        `tanggal` = '".$tanggal."' AND
        `shift` = '".$shift."'");
    
    if($data = mysqli_fetch_array($query)){
        echo "Done";
    }
}
else{
    echo "Error";
}
?>