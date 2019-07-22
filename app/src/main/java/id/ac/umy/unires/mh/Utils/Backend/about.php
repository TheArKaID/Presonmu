<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    include_once("config.php");

    $result = mysqli_query($mysqli, "SELECT * FROM informasi");

    $data = mysqli_fetch_array($result);

    $about = $data['about'];
    $version = $data['version'];

    echo json_encode(array(
        'informasi' => $about,
        'version' => $version
        ), JSON_PRETTY_PRINT);
}
else{
    echo "Error";
}
?>