/*
 * Copyright (C) jdneo

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.shengchen.checkstyle.quickfix.design;

import com.shengchen.checkstyle.quickfix.BaseQuickFix;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.IRegion;

import java.util.List;
import java.util.stream.Collectors;

public class HideUtilityClassConstructorQuickFix extends BaseQuickFix {

    @Override
    public ASTVisitor getCorrectingASTVisitor(IRegion lineInfo, int markerStartOffset) {
        return new ASTVisitor() {

            @SuppressWarnings("unchecked")
            @Override
            public boolean visit(TypeDeclaration node) {
                if (containsPosition(node, markerStartOffset)) {
                    /* Find existing constructors */
                    final List<MethodDeclaration> existingConstructors =
                        (List<MethodDeclaration>) node.bodyDeclarations().stream()
                        .filter(n -> n instanceof MethodDeclaration && 
                        needsChanging(((MethodDeclaration) n)))
                        .collect(Collectors.toList());
                    if (!existingConstructors.isEmpty()) {
                        for (final MethodDeclaration existingConstructor : existingConstructors) {
                            final Modifier privateModifier = node.getAST().newModifier(ModifierKeyword.PRIVATE_KEYWORD);
                            existingConstructor.modifiers().add(privateModifier);
                            existingConstructor.modifiers().removeIf(m -> ((Modifier) m).isPublic());
                        }
                    } else {
                        /* Make a new private constructor */
                        final MethodDeclaration constructor = node.getAST().newMethodDeclaration();
                        /* We need to set source range for our text edits, but we can't put anything sensible */
                        constructor.setSourceRange(0, 0);
                        constructor.setConstructor(true);
                        constructor.setName(copy(node.getName()));
                        final Modifier privateModifier = node.getAST().newModifier(ModifierKeyword.PRIVATE_KEYWORD);
                        constructor.modifiers().add(privateModifier);
                        constructor.setBody(node.getAST().newBlock());

                        final List<ASTNode> bodyDeclarations = node.bodyDeclarations();
                        bodyDeclarations.add(methodInsertionIndex(bodyDeclarations), constructor);
                    }
                }
                return false;
            }
        };
    }

    /**
     * Returns the index at which to insert new constructors in a list
     * of body declarations.
     */
    private int methodInsertionIndex(List<ASTNode> bodyDeclarations) {
        int i = 0;
        for (final ASTNode node : bodyDeclarations) {
            if (node instanceof MethodDeclaration) {
                return i;
            }
            i++;
        }
        return i;
    }

    /**
     * Is the given method declaration a constructor that needs its modifiers changed?
     */
    private boolean needsChanging(MethodDeclaration method) {
        if (!method.isConstructor()) {
            return false;
        }

        final int modifiers = method.getModifiers();
        if (Modifier.isPrivate(modifiers) || Modifier.isProtected(modifiers)) {
            return false;
        }
        return true;
    }
}
