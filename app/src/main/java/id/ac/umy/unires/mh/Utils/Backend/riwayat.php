<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");
    
    $email = $mysqli->real_escape_string($_POST['email']);
    
    $query = mysqli_query($mysqli, "SELECT * FROM riwayat WHERE email = '".$email."'");

    $result = array();

    while($row = mysqli_fetch_array($query)){
        array_push($result, array(
            "tanggal"=>$row['tanggal'],
            "jam"=>$row['jam'],
            "shift"=>$row['shift']
        ));
    }

    echo json_encode($result, JSON_PRETTY_PRINT);
    
}
else{
    echo "Error";
}
?>