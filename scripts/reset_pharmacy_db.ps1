# 重建 pharmacy_system 并导入种子数据（会删除原库）
# 用法: .\scripts\reset_pharmacy_db.ps1
# 默认使用环境变量 MYSQL_USER / MYSQL_PWD；不在仓库内写死数据库密码

$ErrorActionPreference = 'Stop'
# 脚本位于 <项目根>/scripts/，资源在 <项目根>/src/main/resources/
$projRoot = Split-Path -Parent $PSScriptRoot
$sqlDir = Join-Path $projRoot 'src\main\resources'
$user = if ($env:MYSQL_USER) { $env:MYSQL_USER } else { 'root' }
$pwd = if ($env:MYSQL_PWD) { $env:MYSQL_PWD } else { (Read-Host -Prompt 'MySQL password') }

Write-Host "Dropping and creating database pharmacy_system..."
& mysql -u $user -p$pwd --default-character-set=utf8mb4 -e "DROP DATABASE IF EXISTS pharmacy_system; CREATE DATABASE pharmacy_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
if ($LASTEXITCODE -ne 0) { throw 'mysql failed' }

Write-Host "Importing pharmacy_system.sql..."
cmd /c "mysql -u $user -p$pwd --default-character-set=utf8mb4 < `"$sqlDir\pharmacy_system.sql`""
if ($LASTEXITCODE -ne 0) { throw 'import pharmacy_system.sql failed' }

Write-Host "Importing audit_log.sql..."
cmd /c "mysql -u $user -p$pwd --default-character-set=utf8mb4 < `"$sqlDir\audit_log.sql`""
if ($LASTEXITCODE -ne 0) { throw 'import audit_log.sql failed' }

Write-Host "Done. Verifying counts..."
& mysql -u $user -p$pwd --default-character-set=utf8mb4 pharmacy_system -e @"
SELECT 'userinfo' t, COUNT(*) c FROM userinfo
UNION ALL SELECT 'category', COUNT(*) FROM category
UNION ALL SELECT 'drugs', COUNT(*) FROM drugs
UNION ALL SELECT 'kcxx', COUNT(*) FROM kcxx
UNION ALL SELECT 'purchase', COUNT(*) FROM purchase
UNION ALL SELECT 'sale', COUNT(*) FROM sale
UNION ALL SELECT 'audit_log', COUNT(*) FROM audit_log;
"@
