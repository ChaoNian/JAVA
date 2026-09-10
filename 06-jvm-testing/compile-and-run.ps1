# 在 06-jvm-testing 目录：跑单元测试 + CompareApp 对照。
# JVM 极限演示（OOM / 栈溢出）见 .\run-jvm-demos.ps1
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "== java / mvn version =="
java -version
mvn -version

Write-Host "`n== mvn test =="
mvn -q test

Write-Host "`n== CompareApp =="
mvn -q exec:java
