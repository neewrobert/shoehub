package com.neewrobert.shoehub

import io.micrometer.core.annotation.Counted
import io.micrometer.core.annotation.Timed
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException
import javax.validation.constraints.Min
import kotlin.random.Random


@RestController
@RequestMapping("/shoe")
@Timed("shoes")
@Validated
@Slf4j
class ShoeHubController(

    @Autowired
    val collectorRegistry: CollectorRegistry
) {
    val log: Logger = LoggerFactory.getLogger(javaClass)
    val paymentCounter: Counter = Counter.build()
        .name("payments")
        .help("Types of used payments")
        .labelNames("method")
        .register(this.collectorRegistry)

    @Timed
    @Counted
    @GetMapping("/{id}/buy")
    fun buyShoe(@PathVariable @Min(1) id: Int): ResponseEntity<String> {

        log.info("Buying a new shoe number: $id")
        if (id > 100) { // to simulate a not found shoe
            log.error("Shoe not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("shoe $id not found")
        }

        Thread.sleep(Random.nextLong(until = 5000)) // to simulate a different and sometimes long response time
        return ResponseEntity.ok().build();
    }

    @Timed
    @Counted
    @PostMapping("/payment")
    fun payment(@RequestParam paymentMethod: String): ResponseEntity<String> {

        Thread.sleep(Random.nextLong(until = 5000)) //simulate time response
        return when (paymentMethod.uppercase()) {
            "CASH" -> ResponseEntity.ok("Paid with cash").also { log.info("Shoe paid with: $paymentMethod") }
                .also { paymentCounter.labels("cash").inc() }

            "CARD" -> ResponseEntity.ok("Paid with card").also { log.info("Shoe paid with: $paymentMethod") }
                .also { paymentCounter.labels(" card").inc() }

            else -> ResponseEntity.badRequest().body("Payment method not accepted")
                .also {
                    log.error("Payment method not accepted: $paymentMethod")
                    paymentCounter.labels("invalid").inc()
                }
        }
    }

    @ExceptionHandler(ConstraintViolationException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private fun handleConstraintViolationException(e: ConstraintViolationException): ResponseEntity<String?>? {
        return ResponseEntity("not valid due to validation error: " + e.message, HttpStatus.BAD_REQUEST)
    }
}