package example.com.fielthyapps.Feature.Smoker;

public class SmokerTipsList {
    String desc;
    int img_number,img_icon;

    public SmokerTipsList(String desc, int img_number, int img_icon) {
        this.desc = desc;
        this.img_number = img_number;
        this.img_icon = img_icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImg_number() {
        return img_number;
    }

    public void setImg_number(int img_number) {
        this.img_number = img_number;
    }

    public int getImg_icon() {
        return img_icon;
    }

    public void setImg_icon(int img_icon) {
        this.img_icon = img_icon;
    }
}
