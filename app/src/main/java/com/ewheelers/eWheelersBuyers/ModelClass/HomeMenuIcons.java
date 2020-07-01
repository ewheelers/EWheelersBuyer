package com.ewheelers.eWheelersBuyers.ModelClass;

public class HomeMenuIcons {
    private int menu_icon;
    private String menutitle;
    private String id;

    public HomeMenuIcons(int menu_icon, String menutitle) {
        this.menu_icon = menu_icon;
        this.menutitle = menutitle;
    }

    public HomeMenuIcons() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMenu_icon() {
        return menu_icon;
    }

    public void setMenu_icon(int menu_icon) {
        this.menu_icon = menu_icon;
    }

    public String getMenutitle() {
        return menutitle;
    }

    public void setMenutitle(String menutitle) {
        this.menutitle = menutitle;
    }
}
