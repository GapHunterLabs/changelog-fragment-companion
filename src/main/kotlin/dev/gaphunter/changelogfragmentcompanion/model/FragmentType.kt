package dev.gaphunter.changelogfragmentcompanion.model

/**
 * The 5 fragment types this plugin recognizes -- the well-established
 * default type set used by towncrier (the real, existing news-fragment
 * tool this plugin's convention models itself on:
 * `changelog.d/<issue>.<type>.md`, one file per change, collected into
 * a real CHANGELOG.md at release time instead of every PR editing the
 * same file and fighting merge conflicts).
 *
 * **Stated honestly:** teams that configure towncrier (or an equivalent
 * tool) with a custom type list aren't covered in v0.1 -- this hardcodes
 * the common default set, a real, documented limitation.
 */
enum class FragmentType(val id: String, val label: String) {
    FEATURE("feature", "New feature"),
    BUGFIX("bugfix", "Bug fix"),
    DOC("doc", "Documentation"),
    REMOVAL("removal", "Deprecation / removal"),
    MISC("misc", "Miscellaneous");

    companion object {
        fun byId(id: String): FragmentType? = entries.firstOrNull { it.id == id }
    }
}
