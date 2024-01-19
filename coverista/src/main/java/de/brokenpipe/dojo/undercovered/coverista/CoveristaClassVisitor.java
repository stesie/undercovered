package de.brokenpipe.dojo.undercovered.coverista;

import lombok.extern.java.Log;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Log
public class CoveristaClassVisitor extends ClassVisitor {

	public CoveristaClassVisitor(final ClassVisitor writer) {
		super(Opcodes.ASM9, writer);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String descriptor,
			final String signature, final String[] exceptions) {
		log.fine("visitMethod: " + name);
		return new CoveristaMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions));
	}
}
