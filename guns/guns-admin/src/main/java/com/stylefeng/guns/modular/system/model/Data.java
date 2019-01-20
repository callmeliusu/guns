package com.stylefeng.guns.modular.system.model;

import java.util.List;

/**
 * @author lyd
 * @date 2019/1/20 22:03
 */

public class Data {
    private List<Bank> value;

    private Integer code = 200;

    private String massage = "";

    private String redirect = "";

    public List<Bank> getValue() {
        return value;
    }

    public void setValue(List<Bank> value) {
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
