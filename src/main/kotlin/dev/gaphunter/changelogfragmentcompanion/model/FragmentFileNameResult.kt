package dev.gaphunter.changelogfragmentcompanion.model

sealed class FragmentFileNameResult {
    data class Valid(val issueId: String, val type: FragmentType) : FragmentFileNameResult()
    data class Invalid(val reason: String) : FragmentFileNameResult()
}
