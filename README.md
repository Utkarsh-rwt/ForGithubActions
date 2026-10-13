# ForGithubActions
on: push
Trigger on every push to any branch


on: push: branches: [main, develop]
Trigger on push to specific branches


on: push: paths: - 'src/**' - '!docs/**'
Trigger only when src/ changes (ignore docs/)


on: pull_request: types: [opened, synchronize]
Trigger on PR open and new commits


on: schedule: - cron: '0 2 * * *'
Trigger daily at 2am UTC


on: workflow_dispatch
Manual trigger from GitHub UI


on: workflow_dispatch: inputs: environment: type: choice options: [staging, prod]
Manual trigger with dropdown input


on: release: types: [published]
Trigger when a release is published


Job Basics
jobs: build: runs-on: ubuntu-latest
Job running on latest Ubuntu


runs-on: [self-hosted, linux, x64]
Run on self-hosted runner with labels


needs: [test, lint]
Job waits for test and lint jobs to pass


if: github.ref == 'refs/heads/main'
Run job only on main branch


if: github.event_name == 'pull_request'
Run only on pull request events


if: failure()
Run only if previous step/job failed


continue-on-error: true
Don't fail workflow if this step fails


timeout-minutes: 30
Cancel job if it runs over 30 minutes


strategy: matrix: node: [18, 20, 22]
Run job for each Node.js version


strategy: fail-fast: false
Continue matrix jobs even if one fails


Steps & Actions
- uses: actions/checkout@v4
Checkout repository code


- uses: actions/checkout@v4 with: fetch-depth: 0
Full git history (needed for some tools)


- uses: actions/setup-node@v4 with: node-version: '20' cache: 'npm'
Setup Node.js with npm cache


- uses: actions/setup-python@v5 with: python-version: '3.11'
Setup Python


- name: Run tests run: | pip install -r requirements.txt pytest tests/ -v
Multi-line run step


- uses: actions/upload-artifact@v4 with: name: build-output path: dist/
Upload build output as artifact


- uses: actions/download-artifact@v4 with: name: build-output
Download artifact from previous job


- uses: actions/cache@v4 with: path: ~/.cache/pip key: ${{ runner.os }}-pip-${{ hashFiles('requirements.txt') }}
Cache pip dependencies


Secrets & Environment
env: DATABASE_URL: ${{ secrets.DATABASE_URL }}
Use a secret as environment variable


env: NODE_ENV: production PORT: 3000
Set environment variables for all steps


- name: Deploy env: API_KEY: ${{ secrets.API_KEY }}
Secret available only in this step


echo "MY_VAR=hello" >> $GITHUB_ENV
Set environment variable for subsequent steps


echo "VERSION=$(cat VERSION)" >> $GITHUB_ENV
Set dynamic env var from command output


echo "::add-mask::$SECRET_VALUE"
Mask a value in logs


${{ vars.MY_VARIABLE }}
Use a repository variable (non-secret)


${{ github.sha }}
Current commit SHA


${{ github.ref_name }}
Branch or tag name


${{ github.actor }}
User who triggered the workflow


Docker & Registry
- uses: docker/setup-buildx-action@v3
Setup Docker Buildx for multi-platform builds


- uses: docker/login-action@v3 with: registry: ghcr.io username: ${{ github.actor }} password: ${{ secrets.GITHUB_TOKEN }}
Login to GitHub Container Registry


- uses: docker/login-action@v3 with: username: ${{ secrets.DOCKERHUB_USERNAME }} password: ${{ secrets.DOCKERHUB_TOKEN }}
Login to Docker Hub


- uses: docker/build-push-action@v5 with: push: true tags: ghcr.io/${{ github.repository }}:${{ github.sha }}
Build and push Docker image


- uses: docker/build-push-action@v5 with: cache-from: type=gha cache-to: type=gha,mode=max
Build with GitHub Actions cache


- uses: docker/metadata-action@v5 id: meta with: images: ghcr.io/${{ github.repository }}
Auto-generate image tags (semver, sha, latest)


- uses: aws-actions/configure-aws-credentials@v4 with: role-to-assume: arn:aws:iam::123:role/GHARole aws-region: us-east-1
AWS OIDC auth (no long-lived credentials)


- uses: aws-actions/amazon-ecr-login@v2
Login to Amazon ECR after AWS credentials


Outputs & Conditionals
- id: version run: echo "tag=v1.2.3" >> $GITHUB_OUTPUT
Set step output


${{ steps.version.outputs.tag }}
Read step output in subsequent steps


outputs: image-tag: value: ${{ steps.build.outputs.tag }}
Expose job output to other jobs


${{ needs.build.outputs.image-tag }}
Read output from another job


if: contains(github.event.head_commit.message, '[skip ci]')
Skip if commit message contains [skip ci]


if: startsWith(github.ref, 'refs/tags/v')
Run only on version tags


if: github.event.pull_request.draft == false
Skip draft PRs


concurrency: group: ${{ github.workflow }}-${{ github.ref }} cancel-in-progress: true
Cancel in-progress runs on new push


Common Patterns
permissions: id-token: write contents: read
Required for OIDC (AWS/GCP auth without secrets)


permissions: packages: write contents: read
Required to push to GitHub Container Registry


- uses: actions/github-script@v7 with: script: | github.rest.issues.createComment({...})
Comment on PR using GitHub API


- uses: softprops/action-gh-release@v1 with: files: dist/*.tar.gz
Create GitHub release and attach files


- uses: peter-evans/create-pull-request@v6
Auto-create a PR from workflow changes


- run: echo "GITHUB_TOKEN=${{ secrets.GITHUB_TOKEN }}" | gh auth login --with-token
Authenticate gh CLI in workflow


- uses: slackapi/slack-github-action@v1 with: webhook: ${{ secrets.SLACK_WEBHOOK }}
Send Slack notification from workflow
