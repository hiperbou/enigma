package com.hiperbou.enigma

val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

//Entry Wheel Wiring
var ETW_QWERTZ = "JWULCMNOHPQZYXIRADKEGVBTSF" // Enigma K
var ETW_KZROUQ = "ILXRZTKGJYAMWVDUFCPQEONSHB" // Enigma T

val reflectorA =    "EJMZALYXVBWFCRQUONTSPIKHGD"
val reflectorB =    "YRUHQSLDPXNGOKMIEBFZCWVJAT"
val reflectorBThin = "ENKQAUYWJICOPBLMDXZVFTHRGS"
val reflectorCThin = "RDOBJNTKVEHMLFCWZAXGYIPSUQ"
val reflectorBeta = "LEYJVCNIXWPBQMDRTAKZGFUHOS"
val reflectorGamma = "FSOKANUERHMBTIYCWLQPZXVGJD"

//Enigma K Railway
val ETW =	"QWERTZUIOASDFGHJKPYXCVBNML"
val krI =	RotorProperties("JGDQOXUSCAMIFRVTPNEWKBLZYH","N", "krI")
val krII =	RotorProperties("NTZPSFBOKMWRCJDIVLAEYUXHGQ","E", "krII")
val krIII =	RotorProperties("JVIUBHTCDYAKEQZPOSGXNRMWFL","Y","krIII")
val UKW_KR_ADJUSTABLE_REFLECTOR =	RotorProperties("QYHOGNECVPUZTFDJAXWMKISRBL","?","UKW(Railway)")

data class RotorProperties(val values:String, val notch:String, val name:String)

val rI = RotorProperties("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "Q", "I")
val rII = RotorProperties("AJDKSIRUXBLHWTMCQGZNPYFVOE", "E", "II")
val rIII = RotorProperties("BDFHJLCPRTXVZNYEIWGAKMUSQO", "V", "III")
val rIV = RotorProperties("ESOVPZJAYQUIRHXLNFTGKDCMWB", "J", "IV")
val rV = RotorProperties("VZBRGITYUPSDNHLXAWMJQOFECK", "Z", "V")
val rVI = RotorProperties("JPGVOUMFYQBENHZRDKASXLICTW", "ZM", "VI")
val rVII = RotorProperties("NZJHGRCXMYSWBOUFAIVLPEKQDT", "ZM", "VII")
val rVIII = RotorProperties("FKQHTLXOCBJSPDZRAMEWNIUYGV", "ZM", "VIII")
val rBeta = RotorProperties(reflectorBeta, "ZM", "rBeta")

interface IScrambler {
    fun input(char:Char):Char
    fun output(char:Char):Char
    fun step()
    var rotation:Int
    var innerRingOffset:Int
    fun rotateInput(char:Char, rotOffset:Int):Char = char
}

open class Scrambler(var values:String, var alphabet:String):IScrambler {
    override var innerRingOffset = 0
    override var rotation = 0

    override fun input(char:Char):Char = values[alphabet.indexOf(char)]
    override fun output(char:Char):Char = alphabet[values.indexOf(char)]
    override fun step() = Unit
    override fun rotateInput(char:Char, rotOffset: Int):Char {
        var rotated = (alphabet.indexOf(char) + rotOffset) % alphabet.length
        while (rotated < 0){ rotated += alphabet.length }
        return alphabet[rotated]
    }
}

open class Rotor(val props: RotorProperties, alphabet:String):Scrambler(props.values, alphabet){
    var hasRotated = false
    fun isMovedFromNotchPoint() = hasRotated && isNotchPoint(rotation - 1)
    fun isInNotchPoint() = isNotchPoint(rotation)

    private fun isNotchPoint(position:Int):Boolean {
        return props.notch.indexOf(alphabet[(((position)+alphabet.length)%alphabet.length)])>=0
    }

    fun rotate() {
        hasRotated = true
        rotation++
        rotation %= alphabet.length
    }

    fun withInnerRing(offset:Int):Rotor {
        innerRingOffset = offset - 1
        return this
    }

    fun withKey(char:Char):Rotor {
        rotation = alphabet.indexOf(char)
        return this
    }
}

class Plugboard(config:String, alphabet:String):Scrambler(configToScramblerValues(config, alphabet), alphabet){
    companion object {
        private fun configToScramblerValues(config:String, alphabet:String):String{
            if(config.isEmpty()) return alphabet
            val result = alphabet.toCharArray()
            config.split(" ").forEach{
                val a = result.indexOf(it[0])
                val b = result.indexOf(it[1])
                result[a] = it[1]
                result[b] = it[0]
            }
            return result.joinToString("")
        }
    }
}
class EntryWheel(values:String, alphabet:String):Scrambler(values, alphabet)
class Reflector(values:String, alphabet:String):Scrambler(values, alphabet){
    override fun output(char:Char):Char = char
}
class AdjustableReflector(props: RotorProperties, alphabet:String):Rotor(props, alphabet){
    override fun output(char:Char):Char = char
}

class Connector(val a:IScrambler, val b:IScrambler):IScrambler{
    override fun input(char: Char) = b.rotateInput(char, b.rotation - a.rotation -b.innerRingOffset + a.innerRingOffset)
    override fun output(char: Char) = b.rotateInput(char, a.rotation - b.rotation + b.innerRingOffset - a.innerRingOffset)
    override var rotation: Int = 0
    override var innerRingOffset: Int = 0

    override fun step() = Unit
    override fun rotateInput(char:Char, rotOffset: Int) = char
}

class RotateAlways(val rotor:Rotor):IScrambler by rotor {
    override fun step() {
        rotor.rotate()
    }
}

class RotateNever(val rotor:Rotor):IScrambler by rotor {
    override fun step() = Unit
}

class RotateFixed(val rotor:Rotor, val rotorOffset:Int):IScrambler by rotor {
    override fun step() { rotor.rotation = rotorOffset }
}

class RotateNotch(val rotor:Rotor, val otherRotor:Rotor):IScrambler by rotor {
    override fun step() {
        rotor.hasRotated = false
        if(otherRotor.isMovedFromNotchPoint()) {
            rotor.rotate()
        }
    }
}
class RotateNotchDoubleStep(val rotor:Rotor, val otherRotor:Rotor):IScrambler by rotor {
    override fun step() {
        rotor.hasRotated = false
        if(otherRotor.isMovedFromNotchPoint()) {
            rotor.rotate()
        } else if (rotor.isInNotchPoint()) {
            rotor.rotate()
        }
    }
}

fun encode(char:Char, scramblers:Array<IScrambler>):Char {
    var result = char
    scramblers.forEach { result = it.input(result) }
    scramblers.reversed().forEach { result = it.output(result) }

    return result
}

fun encode(text:String, scramblers:Array<IScrambler>):String {
    var result = ""
    text.forEach {
        scramblers.forEach { it.step() }
        result += encode(it, scramblers)
    }
    return result
}
