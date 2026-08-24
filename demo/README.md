# Demo data for screenshots

`changelog.d/842.feature.md` (well-named) + `changelog.d/bad-name.md`
(deliberately broken) so the screenshot shows both a clean file and a
real warning.

## How to get the screenshot

1. `./gradlew runIde` from `changelog-fragment-companion`, open this
   `demo/` folder as the project.
2. Full Screen, open `changelog.d/bad-name.md` — an inline warning
   should appear. Also try **Tools > New Changelog Fragment** to show
   the creation dialog flow.
3. Screenshot with the warning visible (and/or the dialog), save into
   `changelog-fragment-companion/docs/screenshots/`. Close the sandbox.
