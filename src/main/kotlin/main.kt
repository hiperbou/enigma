/**
 * Created by Dani on 18/09/17.
 */

val alphabet =      "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//val rIII =        "BDFHJLCPRTXVZNYEIWGAKMUSQO"
//val rII =         "AJDKSIRUXBLHWTMCQGZNPYFVOE"
//val rI =          "EKMFLGDQVZNTOWYHXUSPAIBRCJ"

val reflectorA =    "EJMZALYXVBWFCRQUONTSPIKHGD"
val reflectorB =    "YRUHQSLDPXNGOKMIEBFZCWVJAT"
val reflectorBeta = "LEYJVCNIXWPBQMDRTAKZGFUHOS"
val reflectorGamma ="FSOKANUERHMBTIYCWLQPZXVGJD"


data class RotorProperties(val values:String, val notch:String, val name:String)

val rI = RotorProperties("EKMFLGDQVZNTOWYHXUSPAIBRCJ", "Q", "I")
val rII = RotorProperties("AJDKSIRUXBLHWTMCQGZNPYFVOE", "E", "II")
val rIII = RotorProperties("BDFHJLCPRTXVZNYEIWGAKMUSQO", "V", "III")
val rIV = RotorProperties(alphabet, "J", "IV")
val rV = RotorProperties(alphabet, "Z", "V")
val rVI = RotorProperties(alphabet, "ZM", "VI")
val rVII = RotorProperties(alphabet, "ZM", "VII")
val rVIII = RotorProperties(alphabet, "ZM", "VIII")

interface IConnector{
    fun input(char:Char):Char
    fun output(char:Char):Char
}

interface IScrambler:IConnector {
    fun step()
    var offset:Int
    fun rotateInput(char:Char, rotOffset:Int):Char = char
}

open class Scrambler(var values:String, var alphabet:String):IScrambler {
    var _offset = 0
    override var offset: Int
        get() = _offset
        set(value) {
            _offset = value
        }

    override fun input(char:Char):Char = values[alphabet.indexOf(char)]
    override fun output(char:Char):Char = alphabet[values.indexOf(char)]
    override fun step() = Unit
    override fun rotateInput(char:Char, rotOffset: Int):Char {
        val rotated = (alphabet.indexOf(char) + rotOffset + 26) % 26
        return alphabet[rotated]
    }
}

class Rotor(val props: RotorProperties, alphabet:String):Scrambler(props.values, alphabet){
    var hasRotated = false
    fun isMovedFromNotchPoint() = hasRotated && isNotchPoint(offset-1)
    fun isInNotchPoint() = isNotchPoint(offset)

    private fun isNotchPoint(position:Int):Boolean {
        return props.notch.indexOf(alphabet[(((position)+26)%26)])>=0
    }

    fun rotate() {
        hasRotated = true
        offset++
        offset %= 26
    }
}
class Plugboard(values:String, alphabet:String):Scrambler(values, alphabet)
class Reflector(values:String, alphabet:String):Scrambler(values, alphabet){
    override fun output(char:Char):Char = char
}

class Connector(val a:IScrambler, val b:IScrambler):IScrambler{
    override fun input(char: Char) = b.rotateInput(char, b.offset-a.offset)
    override fun output(char: Char) = b.rotateInput(char, a.offset-b.offset)
    override var offset: Int = 0

    override fun step() = Unit
    override fun rotateInput(char:Char, rotOffset: Int) = char
}

class RotateAlways(val rotor:Rotor):IScrambler by rotor {
    override fun step() {
        rotor.rotate()
        ////println("Rotating ${rotor.values}")
        //rotor.values = rotor.values.drop(1) + rotor.values.first()
        //rotor.plainValues = rotor.plainValues.drop(1) + rotor.plainValues.first()
        //rotor.plainValues = rotor.plainValues.last() + rotor.plainValues.dropLast(1)
        //rotor.values = rotor.values.last() + rotor.values.dropLast(1)
    }
}

class RotateNever(val rotor:Rotor):IScrambler by rotor {
    override fun step() = Unit
}

class RotateFixed(val rotor:Rotor, val rotorOffset:Int):IScrambler by rotor {
    override fun step() { rotor.offset = rotorOffset }
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
    return (result)
}

fun toPlugboard(alphabet:String, config:String):String{
    val result = alphabet.toCharArray()
    config.split(" ").forEach{
        val a = result.indexOf(it[0])
        val b = result.indexOf(it[1])
        result[a] = it[1]
        result[b] = it[0]
    }
    return result.joinToString("")
}

fun main(arg:Array<String>) {
    //val plugboardValues = alphabet
    val plugboardValues = toPlugboard(alphabet,"AB CD EF GH IJ KL MN OP QR ST")

    val plugboard = Plugboard(plugboardValues, alphabet)
    val rotor3 = Rotor(rIII, alphabet)
    val rotor2 = Rotor(rII, alphabet)
    val rotor1 = Rotor(rI, alphabet)
    val reflector = Reflector(reflectorB, alphabet)
    val scramblers = arrayOf<IScrambler>(plugboard,
            Connector(plugboard, rotor3),
            RotateAlways(rotor3),
            Connector(rotor3, rotor2),
            RotateNotchDoubleStep(rotor2,rotor3),
            Connector(rotor2, rotor1),
            RotateNotch(rotor1,rotor2),
            Connector(rotor1, reflector),
            reflector)
    var r = ""

    fun encodeLetters(c:Char):String {
        var r = ""
        (1..5).forEach {
            //println(it)
            rotor3.offset = it
            r += encode(c, scramblers)
        }
        return r
    }
    /*r = encodeLetters('A')
    println("A ${encodeLetters('A')} = BDZGO") //"BDZGO"
    println("B ${encodeLetters('B')} = AJLCS") //"AJLCS"
    println("C ${encodeLetters('C')} = QREBN") //"QREBN"
    println("D ${encodeLetters('D')} = MAJLI") //"MAJLI"
*/

}
