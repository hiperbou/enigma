package com.hiperbou.enigma

abstract class EnigmaMachine {
    protected var scramblers = arrayOf<IScrambler>()
    protected var rotors = arrayOf<Rotor>()

    fun encode(text:String):String {
        return encode(text, scramblers)
    }
    fun setKey(key:String):EnigmaMachine {
        key.forEachIndexed { index, c ->
            rotors[index].withKey(c)
        }
        return this
    }

    fun getRotorsPosition():String {
        return rotors.map { alphabet[it.rotation] }.joinToString("")
    }

    fun setInnerRingOffset(vararg offsets:Int):EnigmaMachine{
        offsets.forEachIndexed { index, i -> rotors[index].withInnerRing(i) }
        return this
    }
}

class EnigmaM3(reflector: String, rotorLeft: RotorProperties,
               rotorMiddle: RotorProperties, rotorRight: RotorProperties, plugboardConfig: String = ""):EnigmaMachine(){
    init{
        rotors = arrayOf(Rotor(rotorLeft, alphabet),
                Rotor(rotorMiddle, alphabet),
                Rotor(rotorRight, alphabet))
        scramblers = buildMachine(reflector, rotors[0], rotors[1], rotors[2], plugboardConfig)
    }

    private fun buildMachine(reflector: String, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor, plugboardConfig: String = ""): Array<IScrambler> {
        val plugboard = Plugboard(plugboardConfig, alphabet)
        val reflector = Reflector(reflector, alphabet)
        return arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotorRight),
                RotateAlways(rotorRight),
                Connector(rotorRight, rotorMiddle),
                RotateNotchDoubleStep(rotorMiddle, rotorRight),
                Connector(rotorMiddle, rotorLeft),
                RotateNotch(rotorLeft, rotorMiddle),
                Connector(rotorLeft, reflector),
                reflector)
    }
}

class EnigmaM4(reflector: String, rotorThin:RotorProperties, rotorLeft: RotorProperties,
               rotorMiddle: RotorProperties, rotorRight: RotorProperties, plugboardConfig: String = ""):EnigmaMachine(){
    init{
        rotors = arrayOf(Rotor(rotorThin, alphabet),
                Rotor(rotorLeft, alphabet),
                Rotor(rotorMiddle, alphabet),
                Rotor(rotorRight, alphabet))
        scramblers = buildMachine(reflector, rotors[0], rotors[1], rotors[2], rotors[3], plugboardConfig)
    }

    private fun buildMachine(reflector: String, rotorThin: Rotor, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor, plugboardConfig: String = ""): Array<IScrambler> {
        val plugboard = Plugboard(plugboardConfig, alphabet)
        val reflector = Reflector(reflector, alphabet)
        return arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotorRight),
                RotateAlways(rotorRight),
                Connector(rotorRight, rotorMiddle),
                RotateNotchDoubleStep(rotorMiddle, rotorRight),
                Connector(rotorMiddle, rotorLeft),
                RotateNotch(rotorLeft, rotorMiddle),
                Connector(rotorLeft, rotorThin),
                RotateNotch(rotorThin, rotorLeft),
                Connector(rotorThin, reflector),
                reflector)
    }
}

class EnigmaKRailway(rotorLeft: RotorProperties,
               rotorMiddle: RotorProperties, rotorRight: RotorProperties):EnigmaMachine(){
    init{
        rotors = arrayOf(AdjustableReflector(UKW_KR_ADJUSTABLE_REFLECTOR, alphabet),
                Rotor(rotorLeft, alphabet),
                Rotor(rotorMiddle, alphabet),
                Rotor(rotorRight, alphabet))
        scramblers = buildMachine(rotors[0], rotors[1], rotors[2], rotors[3])
    }

    private fun buildMachine(adjustableReflector: Rotor, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor): Array<IScrambler> {
        val entryWheel = EntryWheel(ETW_QWERTZ, alphabet)
        return arrayOf<IScrambler>(entryWheel,
                Connector(entryWheel, rotorRight),
                RotateAlways(rotorRight),
                Connector(rotorRight, rotorMiddle),
                RotateNotchDoubleStep(rotorMiddle, rotorRight),
                Connector(rotorMiddle, rotorLeft),
                RotateNotch(rotorLeft, rotorMiddle),
                Connector(rotorLeft, adjustableReflector),
                RotateNever(adjustableReflector))
    }
}