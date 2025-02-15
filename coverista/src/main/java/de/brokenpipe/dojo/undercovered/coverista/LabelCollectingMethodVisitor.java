package de.brokenpipe.dojo.undercovered.coverista;

import lombok.extern.java.Log;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Log
public class LabelCollectingMethodVisitor extends MethodVisitor {

	private final JumpLabelCollector jumpLabelCollector;
	private Integer lastLineNumber = null;

	public LabelCollectingMethodVisitor(final MethodVisitor methodVisitor,
			final JumpLabelCollector jumpLabelCollector) {
		super(Opcodes.ASM9, methodVisitor);
		this.jumpLabelCollector = jumpLabelCollector;
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		super.visitLineNumber(line, start);
		lastLineNumber = Integer.valueOf(line);
	}

	@Override
	public void visitFrame(final int type, final int numLocal, final Object[] local, final int numStack, final Object[] stack) {
		super.visitFrame(type, numLocal, local, numStack, stack);
		jumpLabelCollector.accept(lastLineNumber);
	}

}
