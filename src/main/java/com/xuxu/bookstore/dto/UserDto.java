package com.xuxu.bookstore.dto;

import com.xuxu.bookstore.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String newPassword;

    private String confirmPassword;
}
