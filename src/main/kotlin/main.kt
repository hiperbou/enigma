import com.hiperbou.enigma.*

/**
 * Created by Dani on 18/09/17.
 */


fun main(arg:Array<String>) {
    val plugboard = Plugboard("AB CD EF GH IJ KL MN OP QR ST", alphabet)
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
}
