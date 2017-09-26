package com.hiperbou.enigma

class EnigmaMachine(val scramblers:Array<IScrambler>, val rotors:Array<IRotor>) {
    fun encode(text:String):String {
        return encode(text, scramblers)
    }
    fun setKey(key:String): EnigmaMachine {
        key.forEachIndexed { index, c ->
            rotors[index].withKey(c)
        }
        return this
    }

    fun getRotorsPosition():String {
        return rotors.map { it.getKey() }.joinToString("")
    }

    fun setInnerRingOffset(vararg offsets:Int): EnigmaMachine {
        offsets.forEachIndexed { index, i -> rotors[index].withInnerRing(i) }
        return this
    }

    fun setInnerRingOffset(offsets:String):EnigmaMachine{
        offsets.forEachIndexed { index, i -> rotors[index].withInnerRing(i)}
        return this
    }
}