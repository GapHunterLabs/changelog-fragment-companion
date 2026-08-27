package dev.gaphunter.changelogfragmentcompanion.inspection

import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import dev.gaphunter.changelogfragmentcompanion.model.FragmentFileNameResult
import dev.gaphunter.changelogfragmentcompanion.parse.FragmentFileNameParser
import dev.gaphunter.changelogfragmentcompanion.review.ReviewPrompt

/**
 * Flags any file directly inside a `changelog.d/` directory whose name
 * doesn't match the `<issue-number>.<type>.md` convention
 * ([FragmentFileNameParser]) -- catches a typo'd type, a missing issue
 * number, or the wrong extension before it ships in a release, instead
 * of a release script silently skipping a malformed fragment.
 *
 * **Scope, deliberate:** only files whose direct parent directory is
 * literally named `changelog.d` are checked -- a project using a
 * differently-named fragments directory isn't covered in v0.1, a real,
 * documented limitation, not a bug.
 */
class ChangelogFragmentNamingInspection : LocalInspectionTool() {

    override fun checkFile(file: PsiFile, manager: InspectionManager, isOnTheFly: Boolean): Array<ProblemDescriptor>? {
        val virtualFile = file.virtualFile ?: return null
        if (virtualFile.parent?.name != "changelog.d") return null

        val result = FragmentFileNameParser.parse(virtualFile.name)
        if (result is FragmentFileNameResult.Valid) return null

        val reason = (result as FragmentFileNameResult.Invalid).reason
        val anchor = leafOf(file)
        val problem = manager.createProblemDescriptor(
            anchor,
            TextRange(0, anchor.textLength),
            "Invalid changelog fragment file name: $reason",
            ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
            isOnTheFly,
        )

        ReviewPrompt.recordHit(file.project, virtualFile.path)

        return arrayOf(problem)
    }

    /** Leaf-anchored, never a composite node. */
    private fun leafOf(element: PsiElement): PsiElement {
        var current = element
        while (current.firstChild != null) current = current.firstChild
        return current
    }
}
