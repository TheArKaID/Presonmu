<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");
    
    $dt = new DateTime("now", new DateTimeZone('Asia/Jakarta'));
    
    $day = $dt->format('l');
    $hari = '';
    switch($day){
        case 'Sunday':
            $hari = 'Ahad';
            break;
        case 'Monday':
            $hari = 'Senin'    ;
            break;
        case 'Tuesday':
            $hari = 'Selasa';
            break;
        case 'Wednesday':
            $hari = 'Rabu';
            break;
        case 'Thursday':
            $hari = 'Kamis';
            break;
        case 'Friday':
            $hari = 'Jumat';
            break;
        case 'Saturday':
            $hari = 'Sabtu';
            break;
    }
    
    $jam = $dt->format('H');
    $shift = '';
    
    $absenable = false;
    
    if($jam >= '14' && $jam < '18'){
        $shift = '1';
        $absenable = true;
    } else if ($jam >= '18' && $jam < '24'){
        $shift = '2';
        $absenable = true;
    } else{
        $shift = '~';
        $absenable = false;
    }
    
    echo json_encode(array(
        'shift' => $shift,
        'tanggal' => $hari.$dt->format(', d F Y'),
        'absenable' => $absenable
        ), JSON_PRETTY_PRINT);
}
else{
    echo "Error";
}
?>