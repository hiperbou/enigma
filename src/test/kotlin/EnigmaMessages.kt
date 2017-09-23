import org.junit.Test
import kotlin.test.assertEquals


class EnigmaMessagesTests {

    @Test
    fun messageInstructionManualTest() {
        /**
         * Enigma Instruction Manual, 1930
         *
         * This message is taken from a German army instruction manual for the Enigma I
         * (interoperable with the later navy machine, Enigma M3).

        Message key: ABL

        GCDSE AHUGW TQGRK VLFGX UCALX VYMIG MMNMF DXTGN VHVRM MEVOU YFZSL RHDRR XFJWC
        FHUHM UNZEF RDISI KBGPM YVXUZ

        Machine Settings for Enigma I/M3
        Reflector: 	A
        Wheel order: 	II I III
        Ring positions:  	24 13 22
        Plug pairs: 	AM FI NV PS TU WZ

        German: Feindliche Infanterie Kolonne beobachtet. Anfang Südausgang Bärwalde. Ende 3km ostwärts Neustadt.
        English: Enemy infantry column was observed. Beginning [at] southern exit [of] Baerwalde. Ending 3km east of Neustadt.
         */

        val plugboardValues = toPlugboard(alphabet, "AM FI NV PS TU WZ")
        val plugboard = Plugboard(plugboardValues, alphabet)
        val rotor1 = Rotor(rII, alphabet).withInnerRing(24).withKey('A')
        val rotor2 = Rotor(rI, alphabet).withInnerRing(13).withKey('B')
        val rotor3 = Rotor(rIII, alphabet).withInnerRing(22).withKey('L')
        val reflector = Reflector(reflectorA, alphabet)
        val scramblers =  arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotor3),
                RotateAlways(rotor3),
                Connector(rotor3, rotor2),
                RotateNotchDoubleStep(rotor2,rotor3),
                Connector(rotor2, rotor1),
                RotateNotch(rotor1,rotor2),
                Connector(rotor1, reflector),
                reflector)

        val decoded = encode("GCDSEAHUGWTQGRKVLFGXUCALXVYMIGMMNMFDXTGNVHVRMMEVOUYFZSLRHDRRXFJWCFHUHMUNZEFRDISIKBGPMYVXUZ", scramblers)
        assertEquals("FEINDLIQEINFANTERIEKOLONNEBEOBAQTETXANFANGSUEDAUSGANGBAERWALDEXENDEDREIKMOSTWAERTSNEUSTADT", decoded)
    }


    @Test
    fun messageTouringsTeatrise() {
        /**
         * Turing's Treatise, 1940
         *
         * Message included in a document written by Alan Turing for new codebreaker recruits
         * at Bletchley Park.

        Message key: JEZA

        Machine Settings for Enigma K Railway
        Wheel order: 	III I II
        Ring positions:  	26 17 16 13

        QSZVI DVMPN EXACM RWWXU IYOTY NGVVX DZ---

        German: Deutsche Truppen sind jetzt in England.
        English: German troops are now in England.
         */
        val plugboard = Plugboard(alphabet, alphabet)
        val rotor0 = Rotor(UKW_ROTOR, alphabet).withInnerRing(26).withKey('J')
        val rotor1 = Rotor(kIII, alphabet).withInnerRing(17).withKey('E')
        val rotor2 = Rotor(kI, alphabet).withInnerRing(16).withKey('Z')
        val rotor3 = Rotor(kII, alphabet).withInnerRing(13).withKey('A')
        val reflector = Reflector(UKW, alphabet)
        val scramblers =  arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotor3),
                RotateAlways(rotor3),
                Connector(rotor3, rotor2),
                RotateNotchDoubleStep(rotor2,rotor3),
                Connector(rotor2, rotor1),
                RotateNotch(rotor1,rotor2),
                Connector(rotor1, rotor0),
                RotateNotch(rotor0,rotor1),
                Connector(rotor0, plugboard),
                plugboard
                //reflector)
        )
        val decoded = encode("QSZVIDVMPNEXACMRWWXUIYOTYNGVVXDZ", scramblers)
        assertEquals("DEUTSQETRUPPENSINDJETZTINENGLAND", decoded)
    }

    @Test
    fun messageOperationBarbosa() {
        /**
         * Operation Barbarossa, 1941
         * Sent from the Russian front on 7th July 1941. The message is in two parts:

        Message key: BLA

        Machine Settings for Enigma I/M3
        Reflector: 	B
        Wheel order: 	II IV V
        Ring positions:  	02 21 12
        Plug pairs: 	AV BS CG DL FU HZ IN KM OW RX

        EDPUD NRGYS ZRCXN UYTPO MRMBO FKTBZ REZKM LXLVE FGUEY SIOZV EQMIK UBPMM YLKLT TDEIS MDICA GYKUA CTCDO MOHWX MUUIA UBSTS LRNBZ SZWNR FXWFY SSXJZ VIJHI DISHP RKLKA YUPAD TXQSP INQMA TLPIF SVKDA SCTAC DPBOP VHJK

        German: Aufklärung abteilung von Kurtinowa nordwestlich Sebez [auf] Fliegerstraße in Richtung Dubrowki, Opotschka. Um 18:30 Uhr angetreten angriff. Infanterie Regiment 3 geht langsam aber sicher vorwärts. 17:06 Uhr röm eins InfanterieRegiment 3 auf Fliegerstraße mit Anfang 16km ostwärts Kamenec.
        English: Reconnaissance division from Kurtinowa north-west of Sebezh on the flight corridor towards Dubrowki, Opochka. Attack begun at 18:30 hours. Infantry Regiment 3 goes slowly but surely forwards. 17:06 hours [Roman numeral I?] Infantry Regiment 3 on the flight corridor starting 16 km east of Kamenec.
         */
        val plugboardValues = toPlugboard(alphabet, "AV BS CG DL FU HZ IN KM OW RX")
        val plugboard = Plugboard(plugboardValues, alphabet)
        val rotor1 = Rotor(rII, alphabet).withInnerRing(2).withKey('B')
        val rotor2 = Rotor(rIV, alphabet).withInnerRing(21).withKey('L')
        val rotor3 = Rotor(rV, alphabet).withInnerRing(12).withKey('A')
        val reflector = Reflector(reflectorB, alphabet)
        val scramblers =  arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotor3),
                RotateAlways(rotor3),
                Connector(rotor3, rotor2),
                RotateNotchDoubleStep(rotor2,rotor3),
                Connector(rotor2, rotor1),
                RotateNotch(rotor1,rotor2),
                Connector(rotor1, reflector),
                reflector)

        var decoded = encode("EDPUDNRGYSZRCXNUYTPOMRMBOFKTBZREZKMLXLVEFGUEYSIOZVEQMIKUBPMMYLKLTTDEISMDICAGYKUACTCDOMOHWXMUUIAUBSTSLRNBZSZWNRFXWFYSSXJZVIJHIDISHPRKLKAYUPADTXQSPINQMATLPIFSVKDASCTACDPBOPVHJK", scramblers)
        assertEquals("AUFKLXABTEILUNGXVONXKURTINOWAXKURTINOWAXNORDWESTLXSEBEZXSEBEZXUAFFLIEGERSTRASZERIQTUNGXDUBROWKIXDUBROWKIXOPOTSCHKAXOPOTSCHKAXUMXEINSAQTDREINULLXUHRANGETRETENXANGRIFFXINFXRGTX", decoded)

        rotor1.withKey('L')
        rotor2.withKey('S')
        rotor3.withKey('D')

        decoded = encode("SFBWDNJUSEGQOBHKRTAREEZMWKPPRBXOHDROEQGBBGTQVPGVKBVVGBIMHUSZYDAJQIROAXSSSNREHYGGRPISEZBOVMQIEMMZCYSGQDGRERVBILEKXYQIRGIRQNRDNVRXCYYTNJR", scramblers)
        assertEquals("DREIGEHTLANGSAMABERSIQERVORWAERTSXEINSSIEBENNULLSEQSXUHRXROEMXEINSXINFRGTXDREIXAUFFLIEGERSTRASZEMITANFANGXEINSSEQSXKMXKMXOSTWXKAMENECXK", decoded)
    }


    @Test
    fun messageU264() {
        /**
         * U-264 (Kapitänleutnant Hartwig Looks), 1942
         *
            Machine Settings for Enigma M4
            Message key: VJNA

            Reflector: 	Thin B
            Wheel order: 	β II IV I
            Ring positions:  	01 01 01 22
            Plug pairs: 	AT BL DF GJ HM NW OP QY RZ VX

            Sent from a U-boat on 25th November 1942, this message was enciphered using
            their standard-equipment Enigma M4 machine.
            NCZW VUSX PNYM INHZ XMQX SFWX WLKJ AHSH NMCO CCAK UQPM KCSM HKSE INJU SBLK IOSX CKUB HMLL XCSJ USRR DVKO HULX WCCB GVLI YXEO AHXR HKKF VDRE WEZL XOBA FGYU JQUK GRTV UKAM EURB VEKS UHHV OYHA BCJW MAKL FKLM YFVN RIZR VVRT KOFD ANJM OLBG FFLE OPRG TFLV RHOW OPBE KVWM UQFM PWPA RMFH AGKX IIBG

            German: Von Von 'Looks' F T 1132/19 Inhalt: Bei Angriff unter Wasser gedrückt, Wasserbomben. Letzter Gegnerstandort 08:30 Uhr Marine Quadrat AJ9863, 220 Grad, 8sm, stosse nach. 14mb fällt, NNO 4, Sicht 10.
            English: From Looks, radio-telegram 1132/19 contents: Forced to submerge under attack, depth charges. Last enemy location 08:30 hours, sea square AJ9863, following 220 degrees, 8 knots. [Pressure] 14 millibars falling, [wind] north-north-east 4, visibility 10.
         */
        val plugboardValues = toPlugboard(alphabet, "AT BL DF GJ HM NW OP QY RZ VX")
        val plugboard = Plugboard(plugboardValues, alphabet)
        val rotor0 = Rotor(rBeta, alphabet).withInnerRing(1).withKey('V')
        val rotor1 = Rotor(rII, alphabet).withInnerRing(1).withKey('J')
        val rotor2 = Rotor(rIV, alphabet).withInnerRing(1).withKey('N')
        val rotor3 = Rotor(rI, alphabet).withInnerRing(22).withKey('A')
        val reflector = Reflector(reflectorBThin, alphabet)
        val scramblers =  arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotor3),
                RotateAlways(rotor3),
                Connector(rotor3, rotor2),
                RotateNotchDoubleStep(rotor2,rotor3),
                Connector(rotor2, rotor1),
                RotateNotch(rotor1,rotor2),
                Connector(rotor1, rotor0),
                RotateNotch(rotor0,rotor2),
                Connector(rotor0, reflector),
                reflector)

        val decoded = encode("NCZWVUSXPNYMINHZXMQXSFWXWLKJAHSHNMCOCCAKUQPMKCSMHKSEINJUSBLKIOSXCKUBHMLLXCSJUSRRDVKOHULXWCCBGVLIYXEOAHXRHKKFVDREWEZLXOBAFGYUJQUKGRTVUKAMEURBVEKSUHHVOYHABCJWMAKLFKLMYFVNRIZRVVRTKOFDANJMOLBGFFLEOPRGTFLVRHOWOPBEKVWMUQFMPWPARMFHAGKXIIBG", scramblers)
        assertEquals("VONVONJLOOKSJHFFTTTEINSEINSDREIZWOYYQNNSNEUNINHALTXXBEIANGRIFFUNTERWASSERGEDRUECKTYWABOSXLETZTERGEGNERSTANDNULACHTDREINULUHRMARQUANTONJOTANEUNACHTSEYHSDREIYZWOZWONULGRADYACHTSMYSTOSSENACHXEKNSVIERMBFAELLTYNNNNNNOOOVIERYSICHTEINSNULL", decoded)
    }

    @Test
    fun messageScharnhorst(){
        /**
         * Scharnhorst (Konteradmiral Erich Bey), 1943
         * This message was sent from the battleship Scharnhorst on 26th December 1943, the day on which it was sunk by torpedoes from British destroyers.

            Message key: UZV
            Machine Settings for Enigma M3
            Reflector: 	B
            Wheel order: 	III VI VIII
            Ring positions:  	01 08 13
            Plug pairs: 	AN EZ HK IJ LR MQ OT PV SW UX

            YKAE NZAP MSCH ZBFO CUVM RMDP YCOF HADZ IZME FXTH FLOL PZLF GGBO TGOX GRET DWTJ IQHL MXVJ WKZU ASTR

            German: Steuere Tanafjord an. Standort Quadrat AC4992, fahrt 20sm. Scharnhorst. [hco - padding?]
            English: Heading for Tanafjord. Position is square AC4992, speed 20 knots. Scharnhorst.
         */
        val plugboardValues = toPlugboard(alphabet, "AN EZ HK IJ LR MQ OT PV SW UX")
        val plugboard = Plugboard(plugboardValues, alphabet)
        val rotor1 = Rotor(rIII, alphabet).withInnerRing(1).withKey('U')
        val rotor2 = Rotor(rVI, alphabet).withInnerRing(8).withKey('Z')
        val rotor3 = Rotor(rVIII, alphabet).withInnerRing(13).withKey('V')
        val reflector = Reflector(reflectorB, alphabet)
        val scramblers =  arrayOf<IScrambler>(plugboard,
                Connector(plugboard, rotor3),
                RotateAlways(rotor3),
                Connector(rotor3, rotor2),
                RotateNotchDoubleStep(rotor2,rotor3),
                Connector(rotor2, rotor1),
                RotateNotch(rotor1,rotor2),
                Connector(rotor1, reflector),
                reflector)

        val decoded = encode("YKAENZAPMSCHZBFOCUVMRMDPYCOFHADZIZMEFXTHFLOLPZLFGGBOTGOXGRETDWTJIQHLMXVJWKZUASTR", scramblers)
        assertEquals("STEUEREJTANAFJORDJANSTANDORTQUAAACCCVIERNEUNNEUNZWOFAHRTZWONULSMXXSCHARNHORSTHCO", decoded)
    }
}