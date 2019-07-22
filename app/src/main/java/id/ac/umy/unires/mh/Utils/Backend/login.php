<?php

if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");
    
    $email = isset($_POST['email']) ? $_POST['email'] : false;
    $password = isset($_POST['password']) ? $_POST['password'] : false;
    $logged = isset($_POST['logged']) ? $_POST['logged'] : false;

    $result = mysqli_query($mysqli, "SELECT * FROM user 
        WHERE email = '".$email."' AND password = '".$password."'");

    if($data = mysqli_fetch_array($result)){
        if($logged=="true"){
            echo 'Data Matched';
        } else{
            if($data['login'] == 0 || $data['login'] == '0'){
                $update = mysqli_query($mysqli, "UPDATE user SET login = 1 WHERE email ='".$email."'");
                echo 'Data Matched';
            } else{
                echo 'Anda sudah pernah Login. Silahkan Hubungi Kantor Unires';
            }
        }
    } else{
        echo "Maaf, email atau Password anda Salah";
    }
}
else{
    echo "Harap Cek Koneksi Internet Anda";
}
exit();
?>