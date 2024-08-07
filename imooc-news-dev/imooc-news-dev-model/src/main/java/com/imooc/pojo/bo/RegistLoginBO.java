package com.imooc.pojo.bo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//加上@data 会自动生成getter+setter
public class RegistLoginBO {
    //不为空 空的话可以返回 不用NOTNULL因为无法校验空字符串 用NotBlank
    @NotBlank(message = "手机号不能为空")
    private String mobile;
    @NotBlank(message = "短信验证码不能为空")
    private String smsCode;

    @Override
    public String toString() {
        return "RegistLoginBO{" +
                "mobile='" + mobile + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
