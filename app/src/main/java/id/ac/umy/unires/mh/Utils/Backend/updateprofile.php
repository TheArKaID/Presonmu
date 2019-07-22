<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");

    $email = isset($_POST['email']) ? $_POST['email'] : false;
    $nama = isset($_POST['nama']) ? $_POST['nama'] : false;
    $status = isset($_POST['status']) ? $_POST['status'] : false;
    $foto = isset($_POST['foto']) ? $_POST['foto'] : false;
    $fotosign = isset($_POST['fotosign']) ? $_POST['fotosign'] : false;
    $pass = isset($_POST['pass']) ? $_POST['pass'] : false;
    $newpass = isset($_POST['newpass']) ? $_POST['newpass'] : false;
    $newrepass = isset($_POST['newrepass']) ? $_POST['newrepass'] : false;
    $ischangepass = isset($_POST['ischangepass']) ? $_POST['ischangepass'] : false;
    
    $cekpassquery = mysqli_query($mysqli, "SELECT password FROM user WHERE email = '$email'");
    $cekpass = mysqli_fetch_array($cekpassquery);
    
    function customSearch($keyword, $arrayToSearch){
        foreach($arrayToSearch as $key => $arrayItem){
            if( stristr( $arrayItem, $keyword ) ){
                return $key;
            }
        }
    }
    
    function getBase64ImageSize($base64Image){
        try{
            $size_in_bytes = (int) (strlen(rtrim($base64Image, '=')) * 3 / 4);
            $size_in_kb    = $size_in_bytes / 1024;
            $size_in_mb    = $size_in_kb / 1024;
    
            return $size_in_mb;
        }
        catch(Exception $e){
            return $e;
        }
    }
    
    if($nama==""||$status==""){
        echo "Gagal Data Tidak Lengkap.";
        return;
    }
    
    if($pass==$cekpass[0]){
        
        //Update Profile Picture
        $dir = './images';
        $files = array_values(array_diff(scandir($dir), array('.', '..')));
        
        $imgsize = getBase64ImageSize($foto);
        $sizemax = explode('.', $imgsize)[0];
        $imgnotice="";
        
        if($sizemax<5){
            if($foto!=""){
                $deleted = "images/".$files[customSearch($email, $files)];
                $updated = "images/$email-$fotosign.jpg";
                if(file_exists($deleted)){
                    unlink($deleted);
                }
                file_put_contents("$updated", base64_decode($foto));
            }
        } else{
            $imgnotice = ". Gambar Terlalu Besar.";
        }
        
        if($ischangepass == "false"){
            mysqli_query($mysqli, "UPDATE `user` 
                SET `password`='$pass',
                `nama`='$nama',
                `status`='$status' 
                WHERE `email` = '$email' AND `password` = '$pass'");
        
        echo "Update Berhasil".$imgnotice;
        
        } else{
            if($newpass==$newrepass){
                mysqli_query($mysqli, "UPDATE `user` 
                SET `password`='$newpass',
                `nama`='$nama',
                `status`='$status' 
                WHERE `email` = '$email' AND `password` = '$pass'");
        
                echo "Update Berhasil".$imgnotice;
            } else{
                echo "Password Baru tidak sama";
            }
        }
    } else{
        echo "Password Salah";
    }
    

}
else{
    echo "Cek Koneksi Internet Anda";
}
?>