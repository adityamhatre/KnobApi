package com.adityamhatre.knobapi

import com.pi4j.io.gpio.GpioController
import com.pi4j.io.gpio.GpioFactory
import com.pi4j.io.gpio.RaspiPin

class GPIOProvider private constructor() {

    private val gpioInstance: GpioController by lazy { GpioFactory.getInstance() }

    companion object {
        val instance by lazy { GPIOProvider() }
    }

    fun setPinToOutput(pinNumber: Int) {
        gpioInstance.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pinNumber))
    }

    fun setHigh(pinNumber: Int) {
        set(pinNumber, 1)
    }

    fun setLow(pinNumber: Int) {
        set(pinNumber, 0)
    }

    fun set(pinNumber: Int, state: Int) {
        if (state == 1) {
            gpioInstance.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pinNumber)).high()
            return
        }
        if (state == 0) {
            gpioInstance.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pinNumber)).low()
        }
    }

}
