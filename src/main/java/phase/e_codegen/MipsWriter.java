package phase.e_codegen;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Classe utilitaire d'impression MIPS.
 * 
 * @author Pascal Hennequin
 * @author Denis Conan
 */
public class MipsWriter extends PrintWriter {
    /**
     * La virgule MIPS.
     */
    private static final String SEP = ", ";

    /**
     * Construit le "Writer" pour fichier MIPS.
     * 
     * @param outfile le nom du fichier en sortie.
     * @throws FileNotFoundException si erreur sur le fichier de sortie.
     */
    public MipsWriter(final String outfile) throws FileNotFoundException {
        super(outfile);
    }

    /**
     * Imprime une instruction MIPS avec indentation.
     * 
     * @param s la Ligne MIPS.
     */
    public void inst(final String s) {
        if (s != null) {
            println("    " + s);
        }
    }

    /**
     * Imprime un commentaire MIPS avec indentation.
     * 
     * @param s le commentaire.
     */
    public void com(final String s) {
        if (s != null) {
            if (s.startsWith("--")) {
                println("    #  " + s);
            } else {
                println("    ## " + s);

            }
        }
    }

    /**
     * Imprime un label MIPS.
     * 
     * @param s le label MIPS.
     */
    public void label(final String s) {
        if (s != null) {
            println(s + ":");
        }
    }

    // Des instructions prédéfinies
    /**
     * Jump.
     * 
     * @param name le nom de label pour le saut.
     */
    public void jump(final String name) {
        inst("j    " + name);
    }

    /**
     * Jump Conditional. {@code if Not(r0) jumpToName}.
     * 
     * @param r0   le registre de condition.
     * @param name le label de saut
     */
    public void jumpIfNot(final MIPSRegister r0, final String name) {
        inst("beq  " + r0 + SEP + "$zero" + SEP + name);
    }

    /**
     * Jump in function. {@code $ra= ...}.
     * 
     * @param name le label de la fonction.
     */
    public void jumpIn(final String name) {
        inst("jal  " + name);
    }

    /**
     * Jump out function (return).
     */
    public void jumpOut() {
        inst("jr $ra");
    }

    /**
     * Load register with immediate.
     * 
     * @param r0        le registre à charger.
     * @param immediate la constante entière.
     */
    public void load(final MIPSRegister r0, final int immediate) {
        inst("li   " + r0 + SEP + immediate);
    }

    /**
     * Load register from indirect address.
     * 
     * @param r0     le registre à charger.
     * @param r1     le registre d'adresse.
     * @param offset l'offset de l'adresse.
     */
    public void loadOffset(final MIPSRegister r0, final int offset, final MIPSRegister r1) {
        inst("lw   " + r0 + SEP + offset + "(" + r1 + ")");
    }

    /**
     * Store register to indirect address.
     * 
     * @param r0     le registre à sauver.
     * @param r1     le registre d'adresse.
     * @param offset l'offset de l'adresse.
     */
    public void storeOffset(final MIPSRegister r0, final int offset, final MIPSRegister r1) {
        inst("sw   " + r0 + SEP + offset + "(" + r1 + ")");
    }

    /**
     * Copy : {@code r0 = r1 } .
     * 
     * @param r0 le registre à charger.
     * @param r1 le registre à sauver.
     */
    public void move(final MIPSRegister r0, final MIPSRegister r1) {
        inst("move " + r0 + SEP + r1);
        // pseudo MIPS pour : "addiu "+ r0 +SEP + r1 + SEP +"0"
    }

    /**
     * Oper : {@code r0 = r0 + r1 }.
     * 
     * @param r0 le registre opérande et résultat.
     * @param r1 le deuxième registre opérande.
     */
    public void plus(final MIPSRegister r0, final MIPSRegister r1) {
        inst("add  " + r0 + SEP + r0 + SEP + r1);
    }

    /**
     * Oper : {@code r0 = r0 + immediate }.
     * 
     * @param r0        le registre opérande et résultat.
     * @param immediate la constante entière.
     */
    public void plus(final MIPSRegister r0, final int immediate) {
        if (immediate == 0) {
            return;
        }
        inst("addi " + r0 + SEP + r0 + SEP + immediate);
    }

    /**
     * Oper : {@code r0 = r0 - r1 }.
     * 
     * @param r0 le registre opérande et résultat.
     * @param r1 le deuxième registre opérande.
     */
    public void moins(final MIPSRegister r0, final MIPSRegister r1) {
        inst("sub  " + r0 + SEP + r0 + SEP + r1);
    }

    /**
     * Oper : {@code r0 = r0 * r1 }.
     * 
     * @param r0 le registre opérande et résultat.
     * @param r1 le deuxième registre opérande.
     */
    public void fois(final MIPSRegister r0, final MIPSRegister r1) {
        inst("mult " + r0 + SEP + r1);
        inst("mflo " + r0);
    }

    /**
     * Oper : {@code r0 = 4 * r0 }.
     * 
     * @param r0 le registre opérande et résultat.
     */
    public void fois4(final MIPSRegister r0) {
        inst("sll  " + r0 + SEP + r0 + SEP + "2");
    }

    /**
     * Oper : {@code r0 = r0 && r1 }.
     * 
     * @param r0 le registre opérande et résultat.
     * @param r1 le deuxième registre opérande.
     */
    public void et(final MIPSRegister r0, final MIPSRegister r1) {
        inst("and  " + r0 + SEP + r0 + SEP + r1);
    }

    /**
     * Oper : {@code r0 = (r0 < r1) }.
     * 
     * @param r0 le registre opérande et résultat.
     * @param r1 le deuxième registre opérande.
     */
    public void inferieur(final MIPSRegister r0, final MIPSRegister r1) {
        inst("slt  " + r0 + SEP + r0 + SEP + r1);
    }

    /**
     * Oper : {@code r0 = ! r0 }.
     * 
     * @param r0 le registre opérande et résultat.
     */
    public void not(final MIPSRegister r0) {
        inst("seq  " + r0 + SEP + "$zero" + SEP + r0);
    }

    /**
     * Magic : inRange {@code r0 = ( 0 <= r1 < r2 ) }.
     * 
     * @param r0 le registre résultat.
     * @param r1 le registre opérande 1.
     * @param r2 le registre opérande 2.
     */
    public void inRange(final MIPSRegister r0, final MIPSRegister r1, final MIPSRegister r2) {
        inst("sltu " + r0 + SEP + r1 + SEP + r2);
    }
}
