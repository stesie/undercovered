package de.brokenpipe.dojo.undercovered.coverista;

import java.util.function.UnaryOperator;

import lombok.extern.java.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Log
public class CoveristaClassVisitor extends ClassVisitor {

	private final UnaryOperator<MethodVisitor> methodVisitorFactory;

	public CoveristaClassVisitor(final ClassVisitor writer, final UnaryOperator<MethodVisitor> methodVisitorFactory) {
		super(Opcodes.ASM9, writer);
		this.methodVisitorFactory = methodVisitorFactory;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String descriptor,
			final String signature, final String[] exceptions) {
		log.fine("visitMethod: " + name);
		return methodVisitorFactory.apply(super.visitMethod(access, name, descriptor, signature, exceptions));
	}
}
