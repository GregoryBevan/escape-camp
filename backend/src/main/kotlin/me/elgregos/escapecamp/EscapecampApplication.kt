package me.elgregos.escapecamp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EscapecampApplication

fun main(args: Array<String>) {
	runApplication<EscapecampApplication>(*args)
}
