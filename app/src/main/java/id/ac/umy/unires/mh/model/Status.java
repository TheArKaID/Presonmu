package id.ac.umy.unires.mh.model;

public class Status {
    String nama, masjid, status;

    public Status(){

    }

    public Status(String nama, String masjid, String status) {
        this.nama = nama;
        this.masjid = masjid;
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getMasjid() {
        return masjid;
    }

    public void setMasjid(String masjid) {
        this.masjid = masjid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
