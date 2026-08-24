package dev.gaphunter.changelogfragmentcompanion.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class ChangelogFragmentNamingInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(ChangelogFragmentNamingInspection::class.java)
    }

    fun `test a well-named fragment file produces no warning`() {
        myFixture.configureFromExistingVirtualFile(
            myFixture.addFileToProject("changelog.d/123.feature.md", "Added real-time sync.").virtualFile,
        )

        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("Invalid changelog fragment") == true })
    }

    fun `test a malformed fragment file name produces a warning`() {
        myFixture.configureFromExistingVirtualFile(
            myFixture.addFileToProject("changelog.d/oops.md", "Added real-time sync.").virtualFile,
        )

        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("Invalid changelog fragment") == true })
    }

    fun `test a file outside changelog-d is never checked, even with a bad name`() {
        myFixture.configureFromExistingVirtualFile(
            myFixture.addFileToProject("notes/oops.md", "Added real-time sync.").virtualFile,
        )

        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("Invalid changelog fragment") == true })
    }
}
