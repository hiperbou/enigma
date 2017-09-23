import org.junit.Test
import kotlin.test.assertEquals

class EnigmaTests {
    fun defaultMachine(plugboardValues:String = alphabet): Array<IScrambler> {
        val plugboard = Plugboard(plugboardValues, alphabet)
        val rotor3 = Rotor(rIII, alphabet)
        val rotor2 = Rotor(rII, alphabet)
        val rotor1 = Rotor(rI, alphabet)
        val reflector = Reflector(reflectorB, alphabet)
        return arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotor3),
                RotateAlways(rotor3),
                Connector(rotor3, rotor2),
                RotateNotchDoubleStep(rotor2,rotor3),
                Connector(rotor2, rotor1),
                RotateNotch(rotor1,rotor2),
                Connector(rotor1, reflector),
                reflector)
    }

    @Test
    fun helloWorld() {
        val encoded = encode("HELLOWORLD",  defaultMachine())
        val decoded = encode(encoded,  defaultMachine())

        assertEquals("ILBDAAMTAZ", encoded)
        assertEquals("HELLOWORLD", decoded)
    }

    @Test
    fun plugboardTest() {
        val plugboardValues = toPlugboard(alphabet,"AB CD EF GH IJ KL MN OP QR ST")

        val encoded = encode("ABCDEFGHIJKLMNOPQRSTUVWXYZ", defaultMachine(plugboardValues))
        val decoded = encode(encoded, defaultMachine(plugboardValues))

        assertEquals("BCIAHMSKJUPDSQCVOGHSSMDENF", encoded)
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", decoded)
    }

    @Test
    fun simpleEncodingTest(){
        val scramblers = defaultMachine()

        assertEquals(encode("AAAAA",scramblers), "BDZGO") //"BDZGO"
        assertEquals(encode("BBBBB",scramblers), "YJIFK") //"AJLCS"
        assertEquals(encode("CCCCC",scramblers),"WKQOY") //"QREBN"
        assertEquals(encode("DDDDD",scramblers), "SAMIG") //"MAJLI"
    }

    @Test
    fun doubleStepTest() {
        val plugboard = Plugboard(alphabet, alphabet)
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

        rotor3.offset = 19
        rotor2.offset = 4
        rotor1.offset = 16
        fun getRotorsPosition():String {
            return "${alphabet[rotor1.offset]}${alphabet[rotor2.offset]}${alphabet[rotor3.offset]}"
        }
        assertEquals("QET", getRotorsPosition())

        assertEquals(encode("AAAAA", scramblers), "IHRWF")
        assertEquals("RGY", getRotorsPosition())

        assertEquals(encode("BBBBB", scramblers), "YLGKK")
        assertEquals("RGD", getRotorsPosition())

        assertEquals(encode("CCCCC", scramblers), "HAAWW")
        assertEquals("RGI", getRotorsPosition())

        assertEquals(encode("DDDDD", scramblers), "ISPZL")
        assertEquals("RGN", getRotorsPosition())
    }
}
