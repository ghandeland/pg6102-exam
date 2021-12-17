package no.kristiania.pg6102.exam.trip

import org.springframework.boot.SpringApplication

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, "--spring.profiles.active=test")
}