<?php
//TODO: Cek jam ketika absen ditekan dan waktu saat ini, guna menghindari Fragment absen dibuka dari awal dan absen melewati waktu absen
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");

    $email = $mysqli->real_escape_string($_POST['email']);
    $tanggal = $mysqli->real_escape_string($_POST['tanggal']);
    $shift = $mysqli->real_escape_string($_POST['shift']);
    $latlng = $mysqli->real_escape_string($_POST['latlng']);

    
    
    $dt = new DateTime("now", new DateTimeZone('Asia/Jakarta'));
    $jam = $dt->format('H:i:s');
    
    try {
        mysqli_query($mysqli, "INSERT INTO riwayat(
            `id_riwayat`,
            `email`,
            `tanggal`,
            `jam`,
            `shift`,
            `latlng`
        )VALUES(
            NULL,
            '$email',
            '$tanggal',
            '$jam',
            '$shift',
            '$latlng'
        )");

    echo "Sukses";
    } catch (Exception $e) {
        echo "Gagal ".$e->getMessage();
    }
}
else{
    echo "Error";
}
?>