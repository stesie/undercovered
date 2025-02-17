package de.brokenpipe.dojo.undercovered.coverista;

import lombok.extern.java.Log;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Log
public class CoveristaMethodVisitor extends MethodVisitor {

	public CoveristaMethodVisitor(final MethodVisitor methodVisitor) {
		super(Opcodes.ASM9, methodVisitor);
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		log.finer("instrument line: " + line);
		super.visitLineNumber(line, start);

		super.visitMethodInsn(Opcodes.INVOKESTATIC, "de/brokenpipe/dojo/undercovered/coverista/Tracker", "track", "()V", false);
	}

}
