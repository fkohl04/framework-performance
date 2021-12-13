export DURATION=60

userCounts=(10 50 100)
ports=(8081 8082 8083)

for port in "${ports[@]}"; do
  export SUT_URL="http://localhost:${port}"
  for userCount in "${userCounts[@]}"; do
    export USER_COUNT=$userCount
    gradle gatlingRun -Dgatling.core.outputDirectoryBaseName="$port-$userCount"
    sleep 30
  done
done
