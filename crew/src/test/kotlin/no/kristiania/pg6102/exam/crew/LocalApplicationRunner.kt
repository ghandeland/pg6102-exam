package no.kristiania.pg6102.exam.crew

import org.springframework.boot.SpringApplication

fun main(arg: Array<String>) {
    SpringApplication.run(Application::class.java, "--spring.profiles.active=test")
}