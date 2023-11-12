package com.squarecross.photoalbum.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor //일치하는 생성자가 없을때 오류없이
@Getter
public class LoginRequest {
    private String userName;
}
