package top.arhi.wxpush.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@ConfigurationProperties(prefix = "wx.config")
public class ConfigConstant {
    @Value("${wx.config.appId}")
    public String appId;
    @Value("${wx.config.appSecret}")
    public String appSecret;
    @Value("${wx.config.templateId}")
    public String templateId;

    public ArrayList<String> openidList;

    public String getAppId() {
        return appId;
    }

    @Value("${weather.config.appid}")
    public String weatherAppId;

    @Value("${weather.config.appSecret}")
    public String weatherAppSecret;
    @Value("${weather.config.city}")
    public String city;
    @Value("${message.config.lunarSwitch}")
    public Boolean lunarSwitch;
    @Value("${message.config.togetherDate}")
    public String togetherDate;
    @Value("${message.config.birthday1}")
    public String birthday1;
    @Value("${message.config.birthday2}")
    public String birthday2;

    @Value("${message.config.message}")
    public String message;
    @Value("${ApiSpace.enableDaily}")
    private boolean enableDaily = true;
    @Value("${ApiSpace.token}")
    public String token;

    @Value("${youdao.appKey_YouDao}")
    public String appKey_YouDao;

    @Value("${youdao.appSecret_YouDao}")
    public String appSecret_YouDao;


    public String getAppKey_YouDao() {
        return appKey_YouDao;
    }

    public void setAppKey_YouDao(String appKey_YouDao) {
        this.appKey_YouDao = appKey_YouDao;
    }

    public String getAppSecret_YouDao() {
        return appSecret_YouDao;
    }

    public void setAppSecret_YouDao(String appSecret_YouDao) {
        this.appSecret_YouDao = appSecret_YouDao;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public ArrayList<String> getOpenidList() {
        return openidList;
    }

    public void setOpenidList(ArrayList<String> openidList) {
        this.openidList = openidList;
    }

    public String getWeatherAppId() {
        return weatherAppId;
    }

    public void setWeatherAppId(String weatherAppId) {
        this.weatherAppId = weatherAppId;
    }

    public String getWeatherAppSecret() {
        return weatherAppSecret;
    }

    public void setWeatherAppSecret(String weatherAppSecret) {
        this.weatherAppSecret = weatherAppSecret;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getLunarSwitch() {
        return lunarSwitch;
    }

    public void setLunarSwitch(Boolean lunarSwitch) {
        this.lunarSwitch = lunarSwitch;
    }

    public String getTogetherDate() {
        return togetherDate;
    }

    public void setTogetherDate(String togetherDate) {
        this.togetherDate = togetherDate;
    }

    public String getBirthday1() {
        return birthday1;
    }

    public void setBirthday1(String birthday1) {
        this.birthday1 = birthday1;
    }

    public String getBirthday2() {
        return birthday2;
    }

    public void setBirthday2(String birthday2) {
        this.birthday2 = birthday2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEnableDaily() {
        return enableDaily;
    }

    public void setEnableDaily(boolean enableDaily) {
        this.enableDaily = enableDaily;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}