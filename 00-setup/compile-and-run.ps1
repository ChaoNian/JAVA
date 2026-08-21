# 在 00-setup 目录下编译并运行两份练习。
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

Write-Host "== javac / java version =="
javac -version
java -version

if (Test-Path out) {
    Remove-Item -Recurse -Force out
}
New-Item -ItemType Directory -Path out | Out-Null

Write-Host "`n== Hello (no package) =="
javac -d out src\Hello.java
java -cp out Hello

Write-Host "`n== com.learn.intro.App =="
javac -d out src\com\learn\intro\Greeter.java src\com\learn\intro\App.java
java -cp out com.learn.intro.App
java -cp out com.learn.intro.App learner
