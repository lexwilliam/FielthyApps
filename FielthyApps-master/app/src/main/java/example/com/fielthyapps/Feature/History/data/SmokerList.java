package example.com.fielthyapps.Feature.History.data;

public class SmokerList {
    String id,uid,date,batang,tahun,bungkus,rupiah;

    public SmokerList() {
    }

    public SmokerList(String id, String uid, String date, String batang, String tahun, String bungkus, String rupiah) {
        this.id = id;
        this.uid = uid;
        this.date = date;
        this.batang = batang;
        this.tahun = tahun;
        this.bungkus = bungkus;
        this.rupiah = rupiah;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBatang() {
        return batang;
    }

    public void setBatang(String batang) {
        this.batang = batang;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getBungkus() {
        return bungkus;
    }

    public void setBungkus(String bungkus) {
        this.bungkus = bungkus;
    }

    public String getRupiah() {
        return rupiah;
    }

    public void setRupiah(String rupiah) {
        this.rupiah = rupiah;
    }
}
