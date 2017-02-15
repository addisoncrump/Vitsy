#!/bin/bash

TARGET_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/.."
MVN_OPTIONS_PROMPT="Maven arguments? "
MSG_PROMPT="Commit message? "
BRANCH_PROMPT="Branch? "

cd "$TARGET_DIR"

mvn test
mvn surefire-report:report-only
mvn site -DgenerateReports=false

echo -n "$MSG_PROMPT"
read msg
echo -n "$BRANCH_PROMPT"
read branch

git show-ref --verify --quiet refs/heads/"$branch"
if [[ $? -ne 0 ]]; then
	git checkout -b "$branch"
else
	git checkout "$branch"
fi

git add -A
git commit -m "$msg"
git push origin "$branch"