<#
  SwiftWheels web için PostgreSQL kullanıcısını ve veritabanını oluşturur (varsa dokunmaz).
  Tablolar uygulama ilk açıldığında Hibernate tarafından oluşturulur.

  powershell -ExecutionPolicy Bypass -File scripts\setup-database.ps1 [-SuperUser postgres] [-AppPassword swiftwheels]
#>
param(
    [string]$PgBin = "",
    [string]$DbHost = "localhost",
    [int]$Port = 5432,
    [string]$SuperUser = "postgres",
    [string]$Database = "swiftwheels_web",
    [string]$AppUser = "swiftwheels",
    [string]$AppPassword = "swiftwheels"
)
$ErrorActionPreference = "Stop"
if (-not $PgBin) {
    $dir = Get-ChildItem "C:\Program Files\PostgreSQL" -Directory -ErrorAction SilentlyContinue |
        Sort-Object { [int]($_.Name -replace '\D', '') } -Descending | Select-Object -First 1
    if ($dir) { $PgBin = Join-Path $dir.FullName "bin" }
}
$psql = if ($PgBin) { Join-Path $PgBin "psql.exe" } else { "psql" }
$pw = $AppPassword.Replace("'", "''")
@"
DO `$`$ BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = '$AppUser') THEN
    CREATE ROLE "$AppUser" LOGIN PASSWORD '$pw';
  END IF;
END `$`$;
SELECT 'CREATE DATABASE "$Database" OWNER "$AppUser" ENCODING ''UTF8'''
WHERE NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = '$Database') \gexec
"@ | & $psql -h $DbHost -p $Port -U $SuperUser -d postgres -v ON_ERROR_STOP=1 -q
if ($LASTEXITCODE -ne 0) { throw "Veritabanı oluşturulamadı." }
Write-Host "Hazır: jdbc:postgresql://$DbHost`:$Port/$Database (kullanıcı: $AppUser)" -ForegroundColor Green
