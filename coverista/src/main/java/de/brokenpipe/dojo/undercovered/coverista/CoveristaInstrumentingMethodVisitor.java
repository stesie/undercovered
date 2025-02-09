package de.brokenpipe.dojo.undercovered.coverista;

import java.util.Set;

import lombok.extern.java.Log;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Log
public class CoveristaInstrumentingMethodVisitor extends MethodVisitor {

	private final Set<Integer> jumpLabels;
	private Integer currentLineNumber = null;

	public CoveristaInstrumentingMethodVisitor(final MethodVisitor methodVisitor, final Set<Integer> jumpLabels) {
		super(Opcodes.ASM9, methodVisitor);
		this.jumpLabels = jumpLabels;
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		currentLineNumber = Integer.valueOf(line);
		super.visitLineNumber(line, start);

		if (!jumpLabels.contains(currentLineNumber)) {
			instrument(currentLineNumber);
		}
	}

	@Override
	public void visitFrame(final int type, final int numLocal, final Object[] local, final int numStack, final Object[] stack) {
		super.visitFrame(type, numLocal, local, numStack, stack);

		if (jumpLabels.contains(currentLineNumber)) {
			instrument(currentLineNumber);
		}
	}

	private void instrument(final Integer line) {
		log.finer("instrument line: " + line);
		push(line);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "de/brokenpipe/dojo/undercovered/coverista/Tracker", "track", "(I)V", false);
	}

	private void push(final Integer value) {
		if (value.intValue() <= Byte.MAX_VALUE) {
			super.visitIntInsn(Opcodes.BIPUSH, value.intValue());
		} else if (value.intValue() <= Short.MAX_VALUE) {
			super.visitIntInsn(Opcodes.SIPUSH, value.intValue());
		} else {
			super.visitLdcInsn(value);
		}
	}
}
