package dev.gaphunter.changelogfragmentcompanion.parse

import dev.gaphunter.changelogfragmentcompanion.model.FragmentFileNameResult
import dev.gaphunter.changelogfragmentcompanion.model.FragmentType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FragmentFileNameParserTest {

    @Test
    fun `a well-formed fragment file name parses to issue id and type`() {
        val result = FragmentFileNameParser.parse("123.feature.md")
        assertTrue(result is FragmentFileNameResult.Valid)
        result as FragmentFileNameResult.Valid
        assertEquals("123", result.issueId)
        assertEquals(FragmentType.FEATURE, result.type)
    }

    @Test
    fun `every recognized type parses correctly`() {
        for (type in FragmentType.entries) {
            val result = FragmentFileNameParser.parse("1.${type.id}.md")
            assertTrue("expected Valid for type ${type.id}", result is FragmentFileNameResult.Valid)
            assertEquals(type, (result as FragmentFileNameResult.Valid).type)
        }
    }

    @Test
    fun `a missing issue number is invalid`() {
        assertTrue(FragmentFileNameParser.parse("feature.md") is FragmentFileNameResult.Invalid)
    }

    @Test
    fun `an unrecognized type is invalid with a message naming the bad type`() {
        val result = FragmentFileNameParser.parse("123.typo.md")
        assertTrue(result is FragmentFileNameResult.Invalid)
        assertTrue((result as FragmentFileNameResult.Invalid).reason.contains("typo"))
    }

    @Test
    fun `the wrong extension is invalid`() {
        assertTrue(FragmentFileNameParser.parse("123.feature.txt") is FragmentFileNameResult.Invalid)
    }

    @Test
    fun `a non-numeric issue id is invalid`() {
        assertTrue(FragmentFileNameParser.parse("abc.feature.md") is FragmentFileNameResult.Invalid)
    }

    @Test
    fun `fileNameFor is the exact inverse of a successful parse`() {
        val name = FragmentFileNameParser.fileNameFor("456", FragmentType.BUGFIX)
        assertEquals("456.bugfix.md", name)
        val parsed = FragmentFileNameParser.parse(name) as FragmentFileNameResult.Valid
        assertEquals("456", parsed.issueId)
        assertEquals(FragmentType.BUGFIX, parsed.type)
    }
}
