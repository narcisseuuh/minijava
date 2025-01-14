package phase.e_codegen;

/**
 * Concaténation d'un Runtime MIPS de MiniJAVA.
 * <ul>
 * <li>{@code _new_object: void *malloc(int size)}
 * <li>{@code equals: boolean Object.equals(Object x)}
 * <li>{@code _system_out_println: void System.out.println(int n)}
 * <li>{@code _system_exit: void System.exit(int status)}
 * </ul>
 * 
 * @author Pascal Hennequin
 */
public class LinkRuntime {
	/**
	 * Le "Writer" MIPS.
	 */
	private final MipsWriter mw;

	/**
	 * Construit le Runtime MIPS de MiniJAVA.
	 * 
	 * @param mipsWriter le "Writer" pour impression dans le fichier MIPS.
	 */
	public LinkRuntime(final MipsWriter mipsWriter) {
		this.mw = mipsWriter;
	}

	/**
	 * Ajoute le Runtime MIPS de MiniJAVA en fin de fichier.
	 */
	public void execute() {
		mw.println("#### RUNTIME MIPS ####");
		
		// MemAlloc
		mw.print("""
				_new_object:
				  ## "void *malloc(int size)"
				  ## IN  $a0 = number of bytes
				  ## OUT $v0 = allocated address
				    li   $v0, 9
				    syscall # sbrk
				  # initialize with zeros
				    move $t0, $a0
				    move $t1, $v0
				__LoopIn:  # do until $t0=0
				    beq  $t0, $zero, __LoopOut
				    sb   $zero, 0($t1)
				    addi $t1, $t1,  1
				    addi $t0, $t0, -1
				    j    __LoopIn
				__LoopOut:  # done
				    jr   $ra
				""");

		// Object.equals()
		mw.print("""
				equals:  ## boolean Object.equals(Object)
				    seq $v0, $a0, $a1
				    jr   $ra
				""");

		// System.out.println(int)
		mw.print("""
				_system_out_println:
				  ## void System.out.println(int x)
				  ## IN  $a0 = integer to print
				    li   $v0,  1
				    syscall # print int
				    li   $a0, 10 # char LineFeed
				    li   $v0, 11
				    syscall # print char
				    jr   $ra
				""");
		
		// System.exit(int)
		mw.print("""
				_system_exit:
				  ## void System.exit(int status)
				  ## IN  $a0 = exit status
				    move $a1, $a0
				    la   $a0, MsgExit
				    li   $v0,  4
				    syscall # print string
				    move $a0, $a1
				    li   $v0,  1
				    syscall # print int
				    li   $v0,  17
				    syscall # exit with status
				.data
				MsgExit: .asciiz "Exit status "
				.text
				""");
	}
}
