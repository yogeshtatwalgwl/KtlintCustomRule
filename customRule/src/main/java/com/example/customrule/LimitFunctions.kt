package com.example.customrule

import com.pinterest.ktlint.core.Rule
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes

/**
 * @author GWL
 */
class LimitFunctions : Rule("limit-function-rule") {
    companion object {
        const val MAX_LIMIT: Int = 20
    }

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == KtStubElementTypes.CLASS) {
            val importDirective = node.psi as KtClass
            val value = calcFunctions(importDirective)
            if (value > MAX_LIMIT)
                emit(
                    node.startOffset,
                    "Class ${importDirective.name} contains $value,this should not use more than $MAX_LIMIT functions.",
                    false
                )
        }
    }

    private fun calcFunctions(classOrObject: KtClassOrObject): Int =
        classOrObject.body?.declarations
            ?.filterIsInstance<KtNamedFunction>()
            ?.size ?: 0
}
