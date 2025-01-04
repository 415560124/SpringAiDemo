package com.rhy.springaidemo.controller;

import com.rhy.springaidemo.service.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
@Tag(name = "客服模块")
public class ClientController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/call")
    @Operation(summary = "客服对话")
    public String call(@RequestParam("clientId") String clientId,@RequestParam("message") String message){
        return clientService.call(clientId,message);
    }
}
