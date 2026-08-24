package dev.gaphunter.changelogfragmentcompanion.write

import com.intellij.openapi.vfs.VirtualFile
import dev.gaphunter.changelogfragmentcompanion.model.FragmentType
import dev.gaphunter.changelogfragmentcompanion.parse.FragmentFileNameParser
import java.nio.charset.StandardCharsets

/**
 * Real file I/O for "New Changelog Fragment" -- separated from the
 * `AnAction` so it's directly testable without driving a modal dialog
 * (same split as `EnvExampleWriter`/`AddVariableToEnvExampleFix`).
 * Must run inside a write action / write command -- callers are
 * responsible for that, this object only does the VFS work.
 */
object ChangelogDirectoryWriter {

    const val DIRECTORY_NAME = "changelog.d"

    /** Finds `changelog.d/` directly under [projectRoot], creating it if it doesn't exist yet. */
    fun findOrCreateChangelogDir(projectRoot: VirtualFile): VirtualFile =
        projectRoot.findChild(DIRECTORY_NAME) ?: projectRoot.createChildDirectory(this, DIRECTORY_NAME)

    /**
     * Writes a new fragment file named per [FragmentFileNameParser.fileNameFor]
     * with [description] as its content. Returns null (does nothing) if a
     * fragment for this exact issue+type already exists -- never silently
     * overwrites an existing fragment.
     */
    fun writeFragment(changelogDir: VirtualFile, issueId: String, type: FragmentType, description: String): VirtualFile? {
        val fileName = FragmentFileNameParser.fileNameFor(issueId, type)
        if (changelogDir.findChild(fileName) != null) return null

        val file = changelogDir.createChildData(this, fileName)
        val content = if (description.endsWith("\n")) description else "$description\n"
        file.setBinaryContent(content.toByteArray(StandardCharsets.UTF_8))
        return file
    }
}
