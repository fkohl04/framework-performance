export DURATION=180

ports=(8081 8082 8083 8084 8085 8086 8087)

for port in "${ports[@]}"; do
  export SUT_URL="http://localhost:${port}"
  userCount=100
  export USER_COUNT=$userCount

  while gradle gatlingRun -Dgatling.core.outputDirectoryBaseName="$port-$userCount";
  do
    sleep 60
    (( userCount+=100 ))
    export USER_COUNT=$userCount
  done

  echo "Loadtest for $userCount users failed for port $port. Stopping testing of current and switching to next service."
  sleep 60
done

echo "Finished performance tests of ${#ports[@]} services with ${#userCounts[@]} user counts."
