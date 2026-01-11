package com.customerSupport.customersupport.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatEntry {

    private String content;
    private String  role; //user,assistance, system


}
