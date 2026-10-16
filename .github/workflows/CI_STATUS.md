# CI Workflow Status

## Current status

The GitHub Actions CI workflow is intentionally disabled until the repository's Actions budget is restored.

GitHub reported:

```text
The job was not started because an Actions budget is preventing further use.
```

GitHub also displayed this runner image notice:

```text
The self-hosted label will migrate to Ubuntu 26 beginning October 19, 2026.
```

Reference:
https://github.com/actions/runner-images/issues/14748

## What happened

The project build and tests pass locally, but GitHub Actions jobs were rejected before any job step started because of the Actions budget. This is an account/repository quota issue, not a Java, Gradle, or project-code failure.

The last local verification was:

```bash
./gradlew test build javadoc
```

Result:

```text
BUILD SUCCESSFUL
```

## Disabled workflow

The workflow file was moved from:

```text
.github/workflows/ci.yml
```

to:

```text
.github/workflows/ci.yml.disabled
```

The workflow was also disabled through the GitHub Actions API so no new red runs are created while the budget is unavailable.

Commit:

```text
0bc565d ci: disable workflow until Actions budget is restored
```

## Re-enable procedure

After the Actions budget is restored:

```bash
mv .github/workflows/ci.yml.disabled .github/workflows/ci.yml
git add .github/workflows
git commit -m "ci: re-enable workflow"
git push origin base/clicker
gh workflow enable 375416790
```

Then trigger or wait for a new run and verify:

```bash
gh run list --repo EpitechBachelorPromo2028/B-OOP-301-STG-3-1-2dgame-6
gh run view <RUN_ID> --log-failed
```

## Workflow configuration preserved

The disabled workflow contains the improved configuration:

- Java 21 with Temurin
- Gradle dependency caching
- official Gradle setup action
- `./gradlew test build javadoc --no-daemon`
- test report upload on failure
- explicit branch filters
- minimal `contents: read` permission
- concurrency cancellation
- 10-minute job timeout

## Important note

Historical failed runs remain visible in GitHub Actions. They are not new failures and do not indicate that the current local build is broken.
