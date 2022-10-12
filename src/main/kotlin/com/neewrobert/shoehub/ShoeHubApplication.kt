package com.neewrobert.shoehub

import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@AutoConfiguration
@EnableConfigurationProperties
class ShoeHubApplication

fun main(args: Array<String>) {
    runApplication<ShoeHubApplication>(*args)
}
