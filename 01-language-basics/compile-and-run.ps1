# 在 01-language-basics 目录下编译并运行全部练习。
$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "== javac / java version =="
javac -version
java -version

if (Test-Path out) {
    Remove-Item -Recurse -Force out
}
New-Item -ItemType Directory -Path out | Out-Null

$sources = Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName }

Write-Host "`n== compile all =="
javac -encoding UTF-8 -d out @sources

Write-Host "`n== Ex01EqualsVsEq =="
java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out com.learn.basics.Ex01EqualsVsEq

Write-Host "`n== Ex02ImmutableString =="
java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out com.learn.basics.Ex02ImmutableString

Write-Host "`n== Ex03PassByValue =="
java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out com.learn.basics.Ex03PassByValue

Write-Host "`n== Ex04Methods =="
java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out com.learn.basics.Ex04Methods

Write-Host "`n== BankApp =="
java -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out com.learn.basics.bank.BankApp
