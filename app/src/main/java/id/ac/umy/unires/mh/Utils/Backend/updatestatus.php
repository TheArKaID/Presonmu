<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");

    $email = isset($_POST['email']) ? $_POST['email'] : false;
    $status = isset($_POST['status']) ? $_POST['status'] : false;
    
    $query = mysqli_query($mysqli, "SELECT email FROM user WHERE email = '$email'");
    if(mysqli_fetch_assoc($query)){
        $update = mysqli_query($mysqli, "UPDATE user SET status = '$status' WHERE email ='".$email."'");
        echo 'Status Updated';    
    }
}
else{
    echo "Cek Koneksi Internet Anda";
}
?>