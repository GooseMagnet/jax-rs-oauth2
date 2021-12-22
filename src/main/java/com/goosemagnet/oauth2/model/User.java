package com.goosemagnet.oauth2.model;

import lombok.Value;

@Value
public class User {
    String username;
    UserType type;
}
