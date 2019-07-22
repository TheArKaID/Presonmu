<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");
    
    $email = isset($_POST['email']) ? $_POST['email'] : false;

    $result = mysqli_query($mysqli, "SELECT * FROM user WHERE email = '".$email."'");

    if($data = mysqli_fetch_array($result)){
        $nama = $data['nama'];
        $masjid = $data['masjid'];
        $status = $data['status'];
        
        //get Image Name
        $dir = './images';
        $files = array_values(array_diff(scandir($dir), array('.', '..')));
        
        function customSearch($keyword, $arrayToSearch){
            foreach($arrayToSearch as $key => $arrayItem){
                if( stristr( $arrayItem, $keyword ) ){
                    return $key;
                }
            }
        }
        $indeximage = isset($files[customSearch($data['email'], $files)]) ? $files[customSearch($data['email'], $files)] : "default.png";
        echo json_encode(array(
            'nama' => $nama,
            'masjid' => $masjid,
            'status' => $status,
            'foto' => "https://presonmu.000webhostapp.com/images/".$indeximage),
            JSON_PRETTY_PRINT);
    } else{
        echo json_encode(array(
            'Nothing' => $inp,
            'email' => $email), 
            JSON_PRETTY_PRINT);
    }

    
}
else{
    echo "Cek Koneksi Internet Anda";
}
?>