package com.hiperbou.enigma

fun EnigmaM3(reflector: String, rotorLeft: RotorProperties,
              rotorMiddle: RotorProperties, rotorRight: RotorProperties, plugboardConfig: String = ""): EnigmaMachine {

    fun buildMachine(reflector: String, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor, plugboardConfig: String = ""): EnigmaMachine {
        return EnigmaMachineBuilder()
                .add(Plugboard(plugboardConfig, alphabet))
                .addRotor(RotateAlways(rotorRight))
                .addRotor(RotateNotchDoubleStep(rotorMiddle,rotorRight))
                .addRotor(RotateNotch(rotorLeft,rotorMiddle))
                .addReflector(Reflector(reflector, alphabet))
                .build()
    }

    return buildMachine(reflector, Rotor(rotorLeft, alphabet),
            Rotor(rotorMiddle, alphabet), Rotor(rotorRight, alphabet), plugboardConfig)
}

fun EnigmaM4(reflector: String, rotorThin:RotorProperties, rotorLeft: RotorProperties,
               rotorMiddle: RotorProperties, rotorRight: RotorProperties, plugboardConfig: String = ""): EnigmaMachine {

    fun buildMachine(reflector: String, rotorThin: Rotor, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor, plugboardConfig: String = ""): EnigmaMachine {
        return EnigmaMachineBuilder()
                .add(Plugboard(plugboardConfig, alphabet))
                .addRotor(RotateAlways(rotorRight))
                .addRotor(RotateNotchDoubleStep(rotorMiddle,rotorRight))
                .addRotor(RotateNotch(rotorLeft,rotorMiddle))
                .addRotor(RotateNever(rotorThin))
                .addReflector(Reflector(reflector, alphabet))
                .build()
    }
    return buildMachine(reflector, Rotor(rotorThin, alphabet), Rotor(rotorLeft, alphabet), Rotor(rotorMiddle, alphabet), Rotor(rotorRight, alphabet), plugboardConfig)
}

fun EnigmaKRailway(rotorLeft: RotorProperties,
               rotorMiddle: RotorProperties, rotorRight: RotorProperties): EnigmaMachine {

    fun buildMachine(adjustableReflector: Rotor, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor): EnigmaMachine {
        return EnigmaMachineBuilder()
                .add(EntryWheel(ETW_QWERTZ, alphabet))
                .addRotor(RotateAlways(rotorRight))
                .addRotor(RotateNotchDoubleStep(rotorMiddle,rotorRight))
                .addRotor(RotateNotch(rotorLeft,rotorMiddle))
                .addRotor(RotateNever(adjustableReflector))
                .build()
    }
    return buildMachine(AdjustableReflector(UKW_KR_ADJUSTABLE_REFLECTOR, alphabet), Rotor(rotorLeft, alphabet), Rotor(rotorMiddle, alphabet), Rotor(rotorRight, alphabet))
}
