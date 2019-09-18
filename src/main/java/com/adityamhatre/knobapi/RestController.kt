package com.adityamhatre.knobapi

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableAutoConfiguration
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

    private var isTurning = false

    private val failedJson = mapOf("success" to false, "errorReason" to "Knob is turning")
    private val successfulJson = mapOf("success" to true, "errorReason" to null)

    @RequestMapping("/lock")
    fun lock(): Map<String, Any?> {
        if (isTurning) return failedJson
        isTurning = !isTurning
        try {
            for (i in 0 until getStepsForAngle(angle = 600)) {
                for (halfStep in 0 until 8) {
                    for (pin in 0 until 4) {
                        GPIOProvider.instance.set(
                            Application.provisionedPins[pin],
                            halfStepSequence[halfStep][pin]
                        )
                    }
                    Thread.sleep(1)
                }
            }
        } catch (ex: Exception) {
            return mapOf("success" to false, "errorReason" to ex.toString())
        }
        isTurning = !isTurning
        return successfulJson
    }

    @RequestMapping("/unlock")
    fun unlock(): Map<String, Any?> {
        if (isTurning) return failedJson
        isTurning = !isTurning
        try {
            for (i in 0 until getStepsForAngle(angle = 600)) {
                for (halfStep in 0 until 8) {
                    for (pin in 0 until 4) {
                        GPIOProvider.instance.set(
                            Application.provisionedPins[pin],
                            reverseHalfStepSequence[halfStep][pin]
                        )
                    }
                    Thread.sleep(1)
                }
            }
        } catch (ex: Exception) {
            return mapOf("success" to false, "errorReason" to ex.toString())
        }
        isTurning = !isTurning
        return successfulJson
    }

    private fun getStepsForAngle(angle: Int): Int {
        return (angle / (motorStepAngle / 2) / halfStepSequence.size).toInt()
    }
}