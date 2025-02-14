package de.brokenpipe.dojo.undercovered.coverista;

import org.objectweb.asm.MethodVisitor;

@FunctionalInterface
public interface MethodVisitorFactory {

	MethodVisitor apply(String className, String methodName, MethodVisitor methodVisitor);

}
