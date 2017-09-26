package com.hiperbou.enigma

class EnigmaMachineBuilder {
    private val scramblers = mutableListOf<IScrambler>()
    private val rotors = mutableListOf<IRotor>()
    fun add(component: IScrambler): EnigmaMachineBuilder {
        scramblers.add(component)
        return this
    }

    fun addRotatingComponent(component: IScrambler): EnigmaMachineBuilder {
        scramblers.add(Connector(scramblers.last(), component))
        scramblers.add(component)
        return this
    }

    fun addRotor(component: IRotor): EnigmaMachineBuilder {
        addRotatingComponent(component)
        rotors.add(component)
        return this
    }
    fun addReflector(component: IScrambler) = addRotatingComponent(component)

    fun build(): EnigmaMachine {
        return EnigmaMachine(scramblers.toTypedArray(), rotors.reversed().toTypedArray())
    }
}