<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Changelog Fragment Companion Changelog

## [Unreleased]

## [0.1.0]

### Added

- "New Changelog Fragment" action (Tools menu): prompts for an
  issue/PR number, a fragment type, and a one-line description, writes
  `changelog.d/<issue>.<type>.md`.
- Inspection flagging any file inside `changelog.d/` whose name doesn't
  match the convention.
- 5 default fragment types (feature/bugfix/doc/removal/misc), the
  towncrier convention.
- 100% local file convention, no external tool required, no network
  calls, no telemetry. Free.

[Unreleased]: https://github.com/GapHunterLabs/changelog-fragment-companion/compare/0.1.0...HEAD
[0.1.0]: https://github.com/GapHunterLabs/changelog-fragment-companion/commits/0.1.0
