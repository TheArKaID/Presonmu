<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");
    
    $email = isset($_POST['email']) ? $_POST['email'] : false;
    
    $result = mysqli_query($mysqli, "SELECT * FROM user WHERE status != '' AND email != '".$email."'");
    
    $nama = "";
    $masjid = "";
    $status = "";
    $pos = 0;
    $data = array();
    
    function customSearch($keyword, $arrayToSearch){
        foreach($arrayToSearch as $key => $arrayItem){
            if( stristr( $arrayItem, $keyword ) ){
                return $key;
            }
        }
    }
    while($get=mysqli_fetch_assoc($result)) {
        $nama = $get['nama'];
        $masjid = $get['masjid'];
        $status = $get['status'];
        
        //get Image Name
        $dir = './images';
        $files = array_values(array_diff(scandir($dir), array('.', '..')));
        
       $indeximage = isset($files[customSearch($get['email'], $files)]) ? $files[customSearch($get['email'], $files)] : "default.png";
        
        $row[$pos] = json_encode(array(
            'nama' => $nama,
            'masjid' => $masjid,
            'status' => $status,
            'foto' => "https://presonmu.000webhostapp.com/images/".$indeximage));
        $pos++;
    }
    echo json_encode($row);
}
else{
    echo "Cek Koneksi Internet Anda";
}
?>