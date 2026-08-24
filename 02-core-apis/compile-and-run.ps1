# 在 02-core-apis 目录下编译并运行当前已有练习。
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

function Invoke-Lesson($title, $mainClass) {
    Write-Host "`n== $title =="
    java "-Dstdout.encoding=UTF-8" "-Dstderr.encoding=UTF-8" -cp out $mainClass
}

Invoke-Lesson "Ex01CheckedVsUnchecked" "com.learn.apis.Ex01CheckedVsUnchecked"
Invoke-Lesson "Ex02TryWithResources" "com.learn.apis.Ex02TryWithResources"
Invoke-Lesson "Ex03ListSetMap" "com.learn.apis.Ex03ListSetMap"
Invoke-Lesson "Ex04HashMapKey" "com.learn.apis.Ex04HashMapKey"
Invoke-Lesson "Ex05Generics" "com.learn.apis.Ex05Generics"
Invoke-Lesson "Ex06JavaTime" "com.learn.apis.Ex06JavaTime"
Invoke-Lesson "CsvApp" "com.learn.apis.csv.CsvApp"
