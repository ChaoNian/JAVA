# 故意触发 StackOverflowError / OutOfMemoryError，练读报错与 -Xmx。
# 这两个进程预期非 0 退出，不要当成脚本失败。
$ErrorActionPreference = "Continue"
Set-Location $PSScriptRoot
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "== compile main (skip tests) =="
mvn -q -DskipTests package
if ($LASTEXITCODE -ne 0) {
    throw "mvn package failed"
}

Write-Host "`n== StackOverflowDemo (-Xss256k) =="
java -Xss256k -cp target/classes com.learn.jvm.jvm.StackOverflowDemo
Write-Host "exit code: $LASTEXITCODE (expect non-zero)"

Write-Host "`n== OomDemo (-Xmx16m) =="
java -Xmx16m -cp target/classes com.learn.jvm.jvm.OomDemo
Write-Host "exit code: $LASTEXITCODE (expect non-zero)"

Write-Host "`nDone. Write your troubleshooting notes under ../notes/ if useful."
