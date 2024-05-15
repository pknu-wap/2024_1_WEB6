package com.web6.server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseVo {

    String ucd;      //ucd: 상태코드 (00: 정상, 99: 오류)
    String message;  //message: 응답메세지

    public ResponseVo() {
    }

}
