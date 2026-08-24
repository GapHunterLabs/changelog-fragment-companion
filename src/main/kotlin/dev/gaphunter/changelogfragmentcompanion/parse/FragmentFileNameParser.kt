package dev.gaphunter.changelogfragmentcompanion.parse

import dev.gaphunter.changelogfragmentcompanion.model.FragmentFileNameResult
import dev.gaphunter.changelogfragmentcompanion.model.FragmentType

/**
 * Parses/validates a changelog fragment file name against the v0.1
 * convention: `<issue-number>.<type>.md` (e.g. `123.feature.md`) --
 * hand-rolled, no external towncrier/library dependency, same "static
 * text convention" discipline as every other file-naming check in this
 * catalog (`SchemaFilePairing` in `kafka-topic-schema-companion`).
 */
object FragmentFileNameParser {

    private val PATTERN = Regex("""^(\d+)\.([a-z]+)\.md$""")

    fun parse(fileName: String): FragmentFileNameResult {
        val match = PATTERN.matchEntire(fileName)
            ?: return FragmentFileNameResult.Invalid(
                "expected \"<issue-number>.<type>.md\" (e.g. \"123.feature.md\")",
            )
        val (issueId, typeId) = match.destructured
        val type = FragmentType.byId(typeId)
            ?: return FragmentFileNameResult.Invalid(
                "unrecognized type \"$typeId\" -- expected one of: ${FragmentType.entries.joinToString(", ") { it.id }}",
            )
        return FragmentFileNameResult.Valid(issueId, type)
    }

    /** Builds the real file name for a fragment, the inverse of [parse] -- the single place both the inspection and the "New Changelog Fragment" action agree on the exact naming convention. */
    fun fileNameFor(issueId: String, type: FragmentType): String = "$issueId.${type.id}.md"
}
