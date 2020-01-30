::CMD script to compile code for java 8 JVM
GOTO EndComment
:ENDcomment

::Normalizer
javac -classpath .;"MOEAFramework-2.12\lib\*"  problems/Normalizer.java
::Stores Problem
javac -classpath .;"MOEAFramework-2.12\lib\*"  problems/storesProblem/ProblemDefBinaryNorm.java
javac -classpath .;"MOEAFramework-2.12\lib\*"  problems/storesProblem/ManualRunBinary.java
javac -classpath .;"MOEAFramework-2.12\lib\*"  problems/storesProblem/Haversine.java
::Blocks Problem
javac -classpath .;"MOEAFramework-2.12\lib\*"  problems/blocksProblem/ProblemDefBinaryNorm.java
javac -classpath .;"MOEAFramework-2.12\lib\*"  problems/blocksProblem/ManualRunBinary.java
