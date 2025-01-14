package phase.e_codegen.access;

import phase.e_codegen.MIPSRegister;

/**
 * Accès MIPS par valeur de registre + offset.
 * 
 * En pratique, on utilise les registres suivants :
 * <ul>
 * <li>{@code $fp} pour les variables locales dans la trame (frame),
 * <li>{@code $a0} pour "this" et les champs de classe,
 * <li>{@code $gp} pour les variables globales,
 * <li>{@code $sp} pour les 4 premiers arguments d'une méthode.
 * </ul>
 * 
 * @author Pascal Hennequin
 */
public class AccessOff implements Access {
    /**
     * Le registre contenant l'adresse de base.
     */
    private final MIPSRegister register;
    /**
     * L'offset d'adresse en Octet.
     */
    private final Integer offset;

    /**
     * Construit l'accès à une variable à l'adresse {@code offset(register)}.
     * 
     * @param register l'adresse de base.
     * @param offset   l'offset par rapport à l'adresse de base.
     */
    public AccessOff(final MIPSRegister register, final Integer offset) {
        this.register = register;
        this.offset = offset;
    }

    @Override
    public String load(final MIPSRegister reg) {
        return "lw   " + reg + ", " + this.offset + '(' + this.register + ')';
    }

    @Override
    public String store(final MIPSRegister reg) {
        return "sw   " + reg + ", " + this.offset + '(' + this.register + ')';
    }

    @Override
    public String toString() {
        return this.offset + "(" + this.register.toString() + ")";
    }
}
