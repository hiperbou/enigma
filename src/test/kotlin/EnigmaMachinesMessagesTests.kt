import com.hiperbou.enigma.*
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Messages and info taken from:
 * http://wiki.franklinheath.co.uk/index.php/Enigma/Sample_Messages
 */

class EnigmaMachinesMessagesTests {

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
        val machine = EnigmaM3(reflectorA, rII, rI, rIII, "AM FI NV PS TU WZ")
                .setInnerRingOffset(24,13,22).setKey("ABL")

        val decoded = machine.encode("GCDSEAHUGWTQGRKVLFGXUCALXVYMIGMMNMFDXTGNVHVRMMEVOUYFZSLRHDRRXFJWCFHUHMUNZEFRDISIKBGPMYVXUZ")
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
        Entry Weel Wiring: QWERTZ....

        QSZVI DVMPN EXACM RWWXU IYOTY NGVVX DZ---

        German: Deutsche Truppen sind jetzt in England.
        English: German troops are now in England.
         */
        val machine = EnigmaKRailway(krIII, krI, krII)
                .setInnerRingOffset(26,17,16,13).setKey("JEZA")

        val decoded = machine.encode("QSZVIDVMPNEXACMRWWXUIYOTYNGVVXDZ")
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

        Message key: LSD

        SFBWD NJUSE GQOBH KRTAR EEZMW KPPRB XOHDR OEQGB BGTQV PGVKB VVGBI MHUSZ YDAJQ IROAX SSSNR EHYGG RPISE ZBOVM QIEMM ZCYSG QDGRE RVBIL EKXYQ IRGIR QNRDN VRXCY YTNJR

        German: Aufklärung abteilung von Kurtinowa nordwestlich Sebez [auf] Fliegerstraße in Richtung Dubrowki, Opotschka. Um 18:30 Uhr angetreten angriff. Infanterie Regiment 3 geht langsam aber sicher vorwärts. 17:06 Uhr röm eins InfanterieRegiment 3 auf Fliegerstraße mit Anfang 16km ostwärts Kamenec.
        English: Reconnaissance division from Kurtinowa north-west of Sebezh on the flight corridor towards Dubrowki, Opochka. Attack begun at 18:30 hours. Infantry Regiment 3 goes slowly but surely forwards. 17:06 hours [Roman numeral I?] Infantry Regiment 3 on the flight corridor starting 16 km east of Kamenec.
         */

        val machine = EnigmaM3(reflectorB, rII, rIV, rV, "AV BS CG DL FU HZ IN KM OW RX")
                .setInnerRingOffset(2,21,12).setKey("BLA")

        var decoded = machine.encode("EDPUDNRGYSZRCXNUYTPOMRMBOFKTBZREZKMLXLVEFGUEYSIOZVEQMIKUBPMMYLKLTTDEISMDICAGYKUACTCDOMOHWXMUUIAUBSTSLRNBZSZWNRFXWFYSSXJZVIJHIDISHPRKLKAYUPADTXQSPINQMATLPIFSVKDASCTACDPBOPVHJK")
        assertEquals("AUFKLXABTEILUNGXVONXKURTINOWAXKURTINOWAXNORDWESTLXSEBEZXSEBEZXUAFFLIEGERSTRASZERIQTUNGXDUBROWKIXDUBROWKIXOPOTSCHKAXOPOTSCHKAXUMXEINSAQTDREINULLXUHRANGETRETENXANGRIFFXINFXRGTX", decoded)

        machine.setKey("LSD")

        decoded = machine.encode("SFBWDNJUSEGQOBHKRTAREEZMWKPPRBXOHDROEQGBBGTQVPGVKBVVGBIMHUSZYDAJQIROAXSSSNREHYGGRPISEZBOVMQIEMMZCYSGQDGRERVBILEKXYQIRGIRQNRDNVRXCYYTNJR")
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
        val machine = EnigmaM4(reflectorBThin, rBeta, rII, rIV, rI, "AT BL DF GJ HM NW OP QY RZ VX")
                .setInnerRingOffset(1,1,1,22).setKey("VJNA")

        val decoded = machine.encode("NCZWVUSXPNYMINHZXMQXSFWXWLKJAHSHNMCOCCAKUQPMKCSMHKSEINJUSBLKIOSXCKUBHMLLXCSJUSRRDVKOHULXWCCBGVLIYXEOAHXRHKKFVDREWEZLXOBAFGYUJQUKGRTVUKAMEURBVEKSUHHVOYHABCJWMAKLFKLMYFVNRIZRVVRTKOFDANJMOLBGFFLEOPRGTFLVRHOWOPBEKVWMUQFMPWPARMFHAGKXIIBG")
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

        val machine = EnigmaM3(reflectorB, rIII, rVI, rVIII, "AN EZ HK IJ LR MQ OT PV SW UX")
                .setInnerRingOffset(1,8,13).setKey("UZV")

        val decoded = machine.encode("YKAENZAPMSCHZBFOCUVMRMDPYCOFHADZIZMEFXTHFLOLPZLFGGBOTGOXGRETDWTJIQHLMXVJWKZUASTR")
        assertEquals("STEUEREJTANAFJORDJANSTANDORTQUAAACCCVIERNEUNNEUNZWOFAHRTZWONULSMXXSCHARNHORSTHCO", decoded)
    }
}