package com.adityamhatre.knobapi

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@EnableAutoConfiguration
@RequestMapping("/knobapi")
class RestController {

    private var lastPosition = ""
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

    private val failedKnobTurningJson = mapOf("success" to false, "errorReason" to "Knob is turning")

    private val failedAlreadyInLockedPositionJson = mapOf("success" to false, "errorReason" to "Knob is already locked")
    private val failedAlreadyInUnlockedPositionJson =
        mapOf("success" to false, "errorReason" to "Knob is already unlocked")

    private val successfulJson = mapOf("success" to true, "errorReason" to null)

    @RequestMapping("/unlock")
    fun lock(): Map<String, Any?> {
        if (lastPosition == "UNLOCKED") return failedAlreadyInUnlockedPositionJson
        if (isTurning) return failedKnobTurningJson
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
            lastPosition = "UNLOCKED"
            clearMotor()
        } catch (ex: Exception) {
            return mapOf("success" to false, "errorReason" to ex.toString())
        }
        isTurning = !isTurning
        return successfulJson
    }

    @RequestMapping("/lock")
    fun unlock(): Map<String, Any?> {
        if (lastPosition == "LOCKED") return failedAlreadyInLockedPositionJson
        if (isTurning) return failedKnobTurningJson
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
            lastPosition = "LOCKED"
            clearMotor()
        } catch (ex: Exception) {
            return mapOf("success" to false, "errorReason" to ex.toString())
        }
        isTurning = !isTurning
        return successfulJson
    }

    private fun clearMotor() {
        Application.provisionedPins.forEach { GPIOProvider.instance.set(it, 0) }
    }

    private fun getStepsForAngle(angle: Int): Int {
        return (angle / (motorStepAngle / 2) / halfStepSequence.size).toInt()
    }
}