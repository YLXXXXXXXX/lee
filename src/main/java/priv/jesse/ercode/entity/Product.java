package priv.jesse.ercode.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Product implements Serializable {
    @Id
    @GeneratedValue
    @Column
    private Integer id;
    /**
     * 商品标题
     */
    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Integer csid;
    /**
     * 商品创建日期
     */
    @Column
    private Date pdate;

    @Column
    private String image;


    @Column
    private Integer status;

    @Column
    private String ercodeUrl;

    @Column
    private String secret;


    @Transient
    private Classification categorySec;

    public Classification getCategorySec() {
        return categorySec;
    }

    public void setCategorySec(Classification categorySec) {
        this.categorySec = categorySec;
    }

    private static final long serialVersionUID = 1L;

    public Product() {
    }

    public Product(String title, Integer csid, Date pdate, Classification categorySec) {
        this.title = title;
        this.csid = csid;
        this.pdate = pdate;
        this.categorySec = categorySec;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCsid() {
        return csid;
    }

    public void setCsid(Integer csid) {
        this.csid = csid;
    }

    public Date getPdate() {
        return pdate;
    }

    public void setPdate(Date pdate) {
        this.pdate = pdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErcodeUrl() {
        return ercodeUrl;
    }

    public void setErcodeUrl(String ercodeUrl) {
        this.ercodeUrl = ercodeUrl;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
