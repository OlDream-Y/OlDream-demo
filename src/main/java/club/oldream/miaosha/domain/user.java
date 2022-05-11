package club.oldream.miaosha.domain;

public class user {
    private Integer id;
    private String openid;
    private String headUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "user{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
