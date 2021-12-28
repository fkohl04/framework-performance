export DURATION=180

userCounts=(100 200 300 400 500 600 700 800 900 1000)
ports=(8081 8082 8083 8084 8085 8086)

for port in "${ports[@]}"; do
  export SUT_URL="http://localhost:${port}"
  for userCount in "${userCounts[@]}"; do
    export USER_COUNT=$userCount
    gradle gatlingRun -Dgatling.core.outputDirectoryBaseName="$port-$userCount"
    sleep 60
  done
done
