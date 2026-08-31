# 在 04-concurrency 目录下编译并运行并发对照示例。
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

Write-Host "`n== CompareApp =="
java "-Dstdout.encoding=UTF-8" "-Dstderr.encoding=UTF-8" -cp out com.learn.concurrency.CompareApp
