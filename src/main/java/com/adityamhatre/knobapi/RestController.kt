package com.adityamhatre.knobapi

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/knobapi")
class RestController {

    private val motorStepAngle = 1.8

    private val halfStepSequence =
        arrayOf(
            intArrayOf(1, 0, 0, 0),
            intArrayOf(1, 1, 0, 0),
            intArrayOf(0, 1, 0, 0),
            intArrayOf(0, 1, 1, 0),
            intArrayOf(0, 0, 1, 0),
            intArrayOf(0, 0, 1, 1),
            intArrayOf(0, 0, 0, 1),
            intArrayOf(1, 0, 0, 1)
        )

    private val reverseHalfStepSequence = halfStepSequence.copyOf().reversed()

    @RequestMapping("/lock")
    fun lock() {
        for (i in 0 until getStepsForAngle(angle = 600)) {
            for (halfStep in 0 until 8) {
                for (pin in 0 until 4) {
                    GPIOProvider.instance.set(Application.provisionedPins[pin], halfStepSequence[halfStep][pin])
                }
                Thread.sleep(1)
            }
        }
    }

    @RequestMapping("/unlock")
    fun unlock() {
        for (i in 0 until getStepsForAngle(angle = 600)) {
            for (halfStep in 0 until 8) {
                for (pin in 0 until 4) {
                    GPIOProvider.instance.set(Application.provisionedPins[pin], reverseHalfStepSequence[halfStep][pin])
                }
                Thread.sleep(1)
            }
        }
    }

    private fun getStepsForAngle(angle: Int): Int {
        return (angle / (motorStepAngle / 2) / halfStepSequence.size).toInt()
    }
}