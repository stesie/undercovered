package de.brokenpipe.dojo.undercovered.coverista;

import java.util.Set;

import lombok.extern.java.Log;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Log
public class CoveristaInstrumentingMethodVisitor extends MethodVisitor {

	private final Set<Integer> jumpLabels;
	private final String className;

	private Integer currentLineNumber = null;

	public CoveristaInstrumentingMethodVisitor(final MethodVisitor methodVisitor, final Set<Integer> jumpLabels,
			final String className) {
		super(Opcodes.ASM9, methodVisitor);
		this.jumpLabels = jumpLabels;
		this.className = className;
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
	public void visitFrame(final int type, final int numLocal, final Object[] local, final int numStack,
			final Object[] stack) {
		super.visitFrame(type, numLocal, local, numStack, stack);

		if (jumpLabels.contains(currentLineNumber)) {
			instrument(currentLineNumber);
		}
	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		super.visitMaxs(maxStack + 2, maxLocals);
	}

	private void instrument(final Integer line) {
		log.finer("instrument line: " + line);
		super.visitLdcInsn(className);
		push(line);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "de/brokenpipe/dojo/undercovered/coverista/Tracker", "track",
				"(Ljava/lang/String;I)V", false);
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
