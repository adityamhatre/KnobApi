package com.adityamhatre.knobapi

import com.pi4j.io.gpio.*

class GPIOProvider private constructor() {

    private val gpioInstance: GpioController by lazy { GpioFactory.getInstance() }

    companion object {
        val instance by lazy { GPIOProvider() }
    }

    fun setPinToOutput(pinNumber: Int): GpioPinDigitalOutput {
        return gpioInstance.provisionDigitalOutputPin(RaspiPin.getPinByAddress(pinNumber), PinState.LOW)
    }

    fun set(pin: GpioPinDigitalOutput, state: Int) {
        if (state == 1) {
            pin.high()
            return
        }
        if (state == 0) {
            pin.low()
        }
    }

}
