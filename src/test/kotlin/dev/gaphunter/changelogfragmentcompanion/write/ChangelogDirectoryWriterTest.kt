package dev.gaphunter.changelogfragmentcompanion.write

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import dev.gaphunter.changelogfragmentcompanion.model.FragmentType
import java.nio.charset.StandardCharsets

class ChangelogDirectoryWriterTest : BasePlatformTestCase() {

    private fun projectRoot(): VirtualFile = myFixture.tempDirFixture.findOrCreateDir("fake-root")

    private fun <T> writeAction(block: () -> T): T =
        ApplicationManager.getApplication().runWriteAction<T> { block() }

    fun `test findOrCreateChangelogDir creates the directory when missing`() {
        val root = projectRoot()
        val dir = writeAction { ChangelogDirectoryWriter.findOrCreateChangelogDir(root) }

        assertTrue(dir.isDirectory)
        assertEquals(ChangelogDirectoryWriter.DIRECTORY_NAME, dir.name)
    }

    fun `test findOrCreateChangelogDir reuses an existing directory`() {
        val root = projectRoot()
        val first = writeAction { ChangelogDirectoryWriter.findOrCreateChangelogDir(root) }
        val second = writeAction { ChangelogDirectoryWriter.findOrCreateChangelogDir(root) }

        assertEquals(first, second)
    }

    fun `test writeFragment creates a file with the description as content`() {
        val root = projectRoot()
        val dir = writeAction { ChangelogDirectoryWriter.findOrCreateChangelogDir(root) }

        val file = writeAction { ChangelogDirectoryWriter.writeFragment(dir, "789", FragmentType.FEATURE, "Add real-time sync") }

        assertNotNull(file)
        assertEquals("789.feature.md", file!!.name)
        val content = String(file.contentsToByteArray(), StandardCharsets.UTF_8)
        assertTrue(content.contains("Add real-time sync"))
    }

    fun `test writeFragment never overwrites an existing fragment for the same issue and type`() {
        val root = projectRoot()
        val dir = writeAction { ChangelogDirectoryWriter.findOrCreateChangelogDir(root) }

        writeAction { ChangelogDirectoryWriter.writeFragment(dir, "1", FragmentType.BUGFIX, "Original description") }
        val second = writeAction { ChangelogDirectoryWriter.writeFragment(dir, "1", FragmentType.BUGFIX, "A different description") }

        assertNull(second)
        val existing = dir.findChild("1.bugfix.md")!!
        val content = String(existing.contentsToByteArray(), StandardCharsets.UTF_8)
        assertTrue(content.contains("Original description"))
    }
}
