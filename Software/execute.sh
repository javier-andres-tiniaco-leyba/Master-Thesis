# execution examples.
# Runs migth take long, NSGAII is the fastest one. 
# If desired, lower the number of executions in ManualRunBinary and recompile for faster results.

if [ 1 -eq 0 ]; then

java -classpath .:MOEAFramework-2.12/lib/* problems.blocksProblem.ManualRunBinary NSGAII 150 1.5 > blocksNSGAII1-50pz15mr1Mevals.csv
java -classpath .:MOEAFramework-2.12/lib/* problems.blocksProblem.ManualRunBinary SPEA2 100 1.0 > blocksSPEA2-100pz05mr1Mevals.csv
java -classpath .:MOEAFramework-2.12/lib/* problems.blocksProblem.ManualRunBinary SMS-EMOA 50 0.5 > blocksSMS-EMOA-50pz05mr1Mevals.csv

java -classpath .:MOEAFramework-2.12/lib/* problems.storesProblem.ManualRunBinary NSGAII 150 1.5 > storesNSGAII1-50pz15mr1Mevals.csv
java -classpath .:MOEAFramework-2.12/lib/* problems.storesProblem.ManualRunBinary SPEA2 100 1.0 > storesSPEA2-100pz05mr1Mevals.csv
java -classpath .:MOEAFramework-2.12/lib/* problems.storesProblem.ManualRunBinary SMS-EMOA 50 0.5 > storesSMS-EMOA-50pz05mr1Mevals.csv

fi

java -classpath .:MOEAFramework-2.12/lib/* problems.blocksProblem.ManualRunBinary NSGAII 50 1.5 > test.csv
