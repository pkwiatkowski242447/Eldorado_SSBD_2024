cd ../../backend;
mvn clean verify -Ptest;
Start-Sleep -Seconds 5;
mvn clean;
cd ../scripts/win;