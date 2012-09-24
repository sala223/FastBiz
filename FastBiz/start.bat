ECHO OFF
SET JAVA=java
SET CWD=%~dp0
PUSHD %CWD%
java -cp %CWD%/boot com.fastbiz.boot.main.Main
POPD  