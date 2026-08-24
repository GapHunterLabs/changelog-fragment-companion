# Changelog Fragment Companion

Manages per-PR changelog fragments, the towncrier-style convention:
instead of every PR editing the same `CHANGELOG.md` and fighting merge
conflicts, each PR adds a small `changelog.d/<issue>.<type>.md` file,
later collected into a real changelog at release time.

## Why it exists

Every PR editing the same `CHANGELOG.md` file is a guaranteed,
recurring source of merge conflicts on any team above a couple of
contributors — a well-known enough problem that tools like towncrier
exist specifically to solve it with per-change fragment files. This
plugin brings the "create a fragment" and "did I forget one / did I
name it wrong" parts of that workflow into the IDE, without requiring
the external tool itself.

## Why built this way

- **New Changelog Fragment action + naming inspection, both built on
  the exact same naming convention** (`FragmentFileNameParser`), so
  there's a single source of truth for what a valid fragment file name
  looks like — the action can never write something the inspection
  would flag.
- **Real logic separated from the modal-dialog UI shell** —
  `ChangelogDirectoryWriter` does all the actual file I/O and is fully
  unit tested without driving a dialog, same split already proven by
  `env-var-missing-companion`'s `EnvExampleWriter`/`AddVariableToEnvExampleFix`.
- **100% local file convention, no towncrier install required.** No
  network call, no external tool dependency — just files on disk.

## v0.1 scope — stated honestly, not exhaustively

Hardcodes the common towncrier default type set
(feature/bugfix/doc/removal/misc) — a team with a custom type list
isn't covered yet. Only a directory literally named `changelog.d` is
checked — a differently-named fragments directory isn't found.

## Usage

**Tools > New Changelog Fragment** — prompts for an issue/PR number, a
type, and a one-line description, and writes the fragment file for you.
Open any file inside `changelog.d/` — a malformed name shows as an
inline warning.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
