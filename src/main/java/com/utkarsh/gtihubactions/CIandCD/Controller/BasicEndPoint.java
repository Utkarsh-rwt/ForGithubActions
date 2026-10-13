package com.utkarsh.gtihubactions.CIandCD.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class BasicEndPoint {

    @GetMapping("/")
    public String  Welcome (){
        System.out.println("Welcome to Server");
        return "You Hitted the server";

    }

}
