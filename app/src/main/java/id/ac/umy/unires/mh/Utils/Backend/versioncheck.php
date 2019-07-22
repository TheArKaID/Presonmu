<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");
    
    $version = isset($_POST['version']) ? $_POST['version'] : false;

    $result = mysqli_query($mysqli, "SELECT version FROM informasi");

    if($data = mysqli_fetch_assoc($result)){
        $versi = $data["version"];
        
        if($versi == $version)
            echo "Versi Sama";
        else
            echo "Versi Baru";
    }
    
}
else{
    echo "Cek Koneksi Internet Anda";
}
?>