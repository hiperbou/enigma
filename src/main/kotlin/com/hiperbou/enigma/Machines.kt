package com.hiperbou.enigma

abstract class EnigmaMachine {
    abstract val scramblers:Array<IScrambler>
    abstract val rotors:Array<IRotor>

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
        return rotors.map { it.getKey() }.joinToString("")
    }

    fun setInnerRingOffset(vararg offsets:Int):EnigmaMachine{
        offsets.forEachIndexed { index, i -> rotors[index].withInnerRing(i) }
        return this
    }
}

class GenericEnigmaMachine(override val scramblers:Array<IScrambler>, override val rotors:Array<IRotor>):EnigmaMachine()

fun EnigmaM3(reflector: String, rotorLeft: RotorProperties,
              rotorMiddle: RotorProperties, rotorRight: RotorProperties, plugboardConfig: String = ""):EnigmaMachine{

    fun buildMachine(reflector: String, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor, plugboardConfig: String = ""): EnigmaMachine {
        return MachineBuilder()
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
               rotorMiddle: RotorProperties, rotorRight: RotorProperties, plugboardConfig: String = ""):EnigmaMachine{

    fun buildMachine(reflector: String, rotorThin: Rotor, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor, plugboardConfig: String = ""): EnigmaMachine {
        return MachineBuilder()
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
               rotorMiddle: RotorProperties, rotorRight: RotorProperties):EnigmaMachine{

    fun buildMachine(adjustableReflector: Rotor, rotorLeft: Rotor, rotorMiddle: Rotor, rotorRight: Rotor): EnigmaMachine {
        return MachineBuilder()
                .add(EntryWheel(ETW_QWERTZ, alphabet))
                .addRotor(RotateAlways(rotorRight))
                .addRotor(RotateNotchDoubleStep(rotorMiddle,rotorRight))
                .addRotor(RotateNotch(rotorLeft,rotorMiddle))
                .addRotor(RotateNever(adjustableReflector))
                .build()
    }
    return buildMachine(AdjustableReflector(UKW_KR_ADJUSTABLE_REFLECTOR, alphabet), Rotor(rotorLeft, alphabet), Rotor(rotorMiddle, alphabet), Rotor(rotorRight, alphabet))
}

class MachineBuilder(){
    private val scramblers = mutableListOf<IScrambler>()
    private val rotors = mutableListOf<IRotor>()
    fun add(component:IScrambler):MachineBuilder{
        scramblers.add(component)
        return this
    }

    fun addRotatingComponent(component:IScrambler):MachineBuilder{
        scramblers.add(Connector(scramblers.last(),component))
        scramblers.add(component)
        return this
    }

    fun addRotor(component:IRotor):MachineBuilder{
        addRotatingComponent(component)
        rotors.add(component)
        return this
    }
    fun addReflector(component:IScrambler) = addRotatingComponent(component)

    fun build():EnigmaMachine{
        return GenericEnigmaMachine(scramblers.toTypedArray(), rotors.reversed().toTypedArray())
    }
}


