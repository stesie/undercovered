package de.brokenpipe.dojo.undercovered.coverista;

import lombok.extern.java.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Log
public class CoveristaClassVisitor extends ClassVisitor {

	private final MethodVisitorFactory methodVisitorFactory;
	private String className;

	public CoveristaClassVisitor(final ClassVisitor writer, final MethodVisitorFactory methodVisitorFactory) {
		super(Opcodes.ASM9, writer);
		this.methodVisitorFactory = methodVisitorFactory;
	}

	@Override
	public void visit(final int version, final int access, final String name, final String signature,
			final String superName, final String[] interfaces) {

		this.className = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String descriptor,
			final String signature, final String[] exceptions) {
		log.fine("visitMethod: " + name + ", descriptor: " + descriptor + ", signature: " + signature);
		return methodVisitorFactory.apply(className, name, descriptor,
				super.visitMethod(access, name, descriptor, signature, exceptions));
	}
}
