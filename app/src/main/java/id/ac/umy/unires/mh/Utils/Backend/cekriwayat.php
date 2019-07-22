<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
    
    include_once("config.php");
    
    $email = isset($_POST['email']) ? $_POST['email'] : false;

    $riwayatquery = mysqli_query($mysqli, "SELECT * FROM riwayat WHERE email = '$email'");
    $waktuquery = mysqli_query($mysqli, "SELECT * FROM informasi");
    
    $waktu = mysqli_fetch_array($waktuquery);
    
    $first = $waktu["mulai"];
    $last = $waktu["selesai"];
    $output_format = 'd F Y';
    $step = '+1 day';
    
    $dates = array();
    $data = array();
    
    $first = strtotime($first);
    $last = strtotime($last);
    $pos = 0;
    
    while($riwayat = mysqli_fetch_array($riwayatquery)){
        $data[] = array("hari" => $riwayat["tanggal"],
                        "shift" => $riwayat["shift"]);
    }
    
    function getDay($day){
        $hari = "";
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
        
        return $hari.", ";
    }
    
    while( $first <= $last ) {
        
        $day = getDay(date("l", $first)).date($output_format, $first);
        
        $shift1 = 0;
        $shift2 = 0;
        
        foreach($data as $row){
            if($row["hari"] == $day && $row["shift"] == 1){
                $shift1 = 1;
            } else if($row["hari"] == $day && $row["shift"] == 2){
                $shift2 = 1;
            }
        }
        
        $dates[] = array("hari" => $day,
                            "shift1" => $shift1,
                            "shift2" => $shift2);
        $first = strtotime($step, $first);
        $pos++;
    }

    echo json_encode($dates, JSON_PRETTY_PRINT);
}
else{
    echo "Not Found";
}
?>