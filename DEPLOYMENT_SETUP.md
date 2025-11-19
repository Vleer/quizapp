# QuizApp Deployment Setup Guide

This guide explains how to set up the automated deployment pipeline for the QuizApp.

## Prerequisites

The GitHub Actions workflow (`.github/workflows/deploy-ghcr.yml`) requires a Personal Access Token (PAT) to push changes to the `infra` repository.

## Step 1: Create a Personal Access Token (PAT)

1. Go to GitHub **Settings** (click your profile picture -> Settings).
2. Scroll down to **Developer settings** (at the bottom of the left sidebar).
3. Click on **Personal access tokens** -> **Tokens (classic)**.
4. Click **Generate new token** -> **Generate new token (classic)**.
5. Give it a name (e.g., `QuizApp Deploy Token`).
6. **Expiration**: Set to "No expiration" or your preferred duration.
7. **Select Scopes**:
   - [x] `repo` (Full control of private repositories) - **Required** to push to the `infra` repo.
   - [x] `write:packages` (Upload packages to GitHub Package Registry) - *Optional but good practice, though the workflow uses the automatic `GITHUB_TOKEN` for this.*
8. Click **Generate token**.
9. **COPY THE TOKEN IMMEDIATELY**. You won't be able to see it again.

## Step 2: Add the Token to Secrets

1. Go to your **QuizApp** repository on GitHub.
2. Click on **Settings** (top tab).
3. In the left sidebar, click **Secrets and variables** -> **Actions**.
4. Click **New repository secret**.
5. **Name**: `INFRA_REPO_TOKEN`
6. **Secret**: Paste the token you copied in Step 1.
7. Click **Add secret**.

## Step 3: Verify Workflow Permissions

Ensure the workflow has permission to write packages (this is usually enabled by default, but good to check):

1. Go to **Settings** -> **Actions** -> **General**.
2. Under **Workflow permissions**, ensure "Read and write permissions" is selected.

## How it Works

1. **Push to Main**: When code is pushed to the `main` branch.
2. **Build & Push Images**: The workflow builds Docker images for `quiz-backend` and `quiz-frontend` and pushes them to GitHub Container Registry (GHCR).
3. **Update Manifests**:
   - It checks out the `infra` repository using the `INFRA_REPO_TOKEN`.
   - It updates `infra/apps/quizapp/deployment.yaml` with the new image tags (SHA).
   - It commits and pushes these changes back to the `infra` repository.
4. **ArgoCD Sync**: ArgoCD (running in your cluster) detects the change in the `infra` repo and updates the running application.
