package com.atlantbh.auctionappbackend.request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;

}
