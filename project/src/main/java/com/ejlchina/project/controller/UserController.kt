package com.ejlchina.project.controller

import com.ejlchina.project.annotation.RequestTime
import com.ejlchina.project.entity.Platform
import com.ejlchina.project.entity.PlatformEntity
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Slf4j
@RequestMapping("/user")
open class UserController {

    @Autowired
    lateinit var platformEntity: PlatformEntity

    @GetMapping("/test4")
    @RequestTime
    open fun test4(): List<Platform?> {
        return platformEntity.findAllByIdIn(listOf(1L, 17L))
    }

}