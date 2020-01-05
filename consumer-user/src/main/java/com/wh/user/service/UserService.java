package com.wh.user.service;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import com.wh.ticket.service.TicketService;

@Service
public class UserService {

    @Reference
    TicketService ticketService;

    public void hello(){
        System.out.println(ticketService.getTicket());
    }
}
