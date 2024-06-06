package com.web6.server.dto;

public class WithdrawDTO {

    private String confirmPassword;

    public WithdrawDTO () {}

    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
