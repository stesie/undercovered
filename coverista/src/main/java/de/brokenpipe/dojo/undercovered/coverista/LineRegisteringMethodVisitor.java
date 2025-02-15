package de.brokenpipe.dojo.undercovered.coverista;

import de.brokenpipe.dojo.undercovered.coverista.tracking.ClassTracker;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LineRegisteringMethodVisitor extends MethodVisitor {

	private final ClassTracker classTracker;
	private final String methodName;
	private final String descriptor;

	protected LineRegisteringMethodVisitor(final MethodVisitor methodVisitor,
			final ClassTracker classTracker, final String methodName, final String descriptor) {
		super(Opcodes.ASM9, methodVisitor);
		this.classTracker = classTracker;
		this.methodName = methodName;
		this.descriptor = descriptor;
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		super.visitLineNumber(line, start);
		classTracker.trackLine(line, methodName, descriptor);
	}
}
