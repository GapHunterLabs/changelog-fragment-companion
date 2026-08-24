package dev.gaphunter.changelogfragmentcompanion.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import dev.gaphunter.changelogfragmentcompanion.model.FragmentType
import dev.gaphunter.changelogfragmentcompanion.write.ChangelogDirectoryWriter

/**
 * "New Changelog Fragment" (Tools menu): prompts for an issue/PR
 * number, a fragment type, and a one-line description, then writes
 * `changelog.d/<id>.<type>.md` -- so contributors never have to
 * remember the naming convention by hand. All the real logic
 * (validation, file naming, file writing) lives in
 * [ChangelogDirectoryWriter]/`FragmentFileNameParser`, fully unit
 * tested without a modal dialog in the loop -- this class is a thin,
 * deliberately untested UI shell (same split already proven by
 * `AddVariableToEnvExampleFix`/`EnvExampleWriter`).
 */
class NewChangelogFragmentAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val basePath = project.basePath
        val projectRoot = basePath?.let { LocalFileSystem.getInstance().findFileByPath(it) } ?: run {
            Messages.showErrorDialog(project, "Could not find the project root directory.", "New Changelog Fragment")
            return
        }

        val issueId = promptForIssueId(project) ?: return
        val type = promptForType(project) ?: return
        val description = promptForDescription(project) ?: return

        WriteCommandAction.runWriteCommandAction(project, "Create Changelog Fragment", null, {
            val changelogDir = ChangelogDirectoryWriter.findOrCreateChangelogDir(projectRoot)
            val written = ChangelogDirectoryWriter.writeFragment(changelogDir, issueId, type, description)
            if (written == null) {
                Messages.showWarningDialog(
                    project,
                    "A fragment for issue $issueId (${type.id}) already exists -- nothing written.",
                    "New Changelog Fragment",
                )
            }
        })
    }

    private fun promptForIssueId(project: com.intellij.openapi.project.Project): String? {
        while (true) {
            val input = Messages.showInputDialog(project, "Issue/PR number:", "New Changelog Fragment", null) ?: return null
            if (input.isNotBlank() && input.all { it.isDigit() }) return input
            Messages.showErrorDialog(project, "Issue/PR number must be numeric (e.g. \"123\").", "New Changelog Fragment")
        }
    }

    private fun promptForType(project: com.intellij.openapi.project.Project): FragmentType? {
        val labels = FragmentType.entries.map { "${it.id} -- ${it.label}" }.toTypedArray()
        val choice = Messages.showDialog(
            project,
            "Fragment type:",
            "New Changelog Fragment",
            labels,
            0,
            Messages.getQuestionIcon(),
        )
        if (choice < 0) return null
        return FragmentType.entries[choice]
    }

    private fun promptForDescription(project: com.intellij.openapi.project.Project): String? {
        val input = Messages.showInputDialog(project, "One-line description:", "New Changelog Fragment", null) ?: return null
        return input.ifBlank { null }
    }
}
